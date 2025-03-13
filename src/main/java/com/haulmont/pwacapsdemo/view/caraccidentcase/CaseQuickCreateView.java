package com.haulmont.pwacapsdemo.view.caraccidentcase;


import com.haulmont.pwacapsdemo.component.DeviceCameraAccess;
import com.haulmont.pwacapsdemo.component.GeoLocationAccess;
import com.haulmont.pwacapsdemo.component.VideoComponent;
import com.haulmont.pwacapsdemo.component.upload.InMemoryStreamVariable;
import com.haulmont.pwacapsdemo.entity.CarAccidentCase;
import com.haulmont.pwacapsdemo.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.core.TimeSource;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;

@Route(value = "cases/quick-create", layout = MainView.class)
@ViewController(id = "CaseQuickCreate")
@ViewDescriptor(path = "case-quick-create.xml")
public class CaseQuickCreateView extends StandardView implements InMemoryStreamVariable.StreamingFinishedListener {

    private final static Logger log = LoggerFactory.getLogger(CaseQuickCreateView.class);

    @Autowired
    private GeoLocationAccess geoLocationAccess;
    @Autowired
    private DeviceCameraAccess deviceCameraAccess;
    @ViewComponent
    private InstanceContainer<CarAccidentCase> carAccidentCaseDc;
    @ViewComponent
    private DataContext dataContext;
    @Autowired
    private TimeSource timeSource;
    @Autowired
    private FileStorage fileStorage;
    @ViewComponent
    private Span cameraCheckStatus;
    @ViewComponent
    private Div videoContainer;
    @ViewComponent
    private JmixCheckbox frontCamera;
    @ViewComponent
    private JmixButton stopCameraBtn;

    private VideoComponent videoComponent;
    private boolean cameraStarted;
    private StreamRegistration streamRegistration;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private MessageBundle messageBundle;
    @Autowired
    private ViewValidation viewValidation;

    @Subscribe(id = "cancelBtn", subject = "singleClickListener")
    public void onCancelBtnClick(final ClickEvent<JmixButton> event) {
        // remove uploaded photo if it won't be saved
        if (getEditedItem().getPhoto() != null) {
            fileStorage.removeFile(getEditedItem().getPhoto());
            getEditedItem().setPhoto(null);
        }

        close(StandardOutcome.DISCARD);
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        geoLocationAccess.injectDependencies();
        deviceCameraAccess.injectDependencies();

        CarAccidentCase item = dataContext.create(CarAccidentCase.class);
        item.setAccidentDate(timeSource.now().toOffsetDateTime());
        carAccidentCaseDc.setItem(item);
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        geoLocationAccess.requestGeoLocation(this, c -> {
            getEditedItem().setLatitude(c.latitude());
            getEditedItem().setLongitude(c.longitude());
        });

        startCamera();

        streamRegistration = deviceCameraAccess.registerStreamReceiver(this, this);
    }

    private void startCamera() {
        deviceCameraAccess.checkCameraSupported(supported -> {
            if (supported) {
                deviceCameraAccess.startCamera(videoComponent, frontCamera.getValue());
                cameraStarted = true;
            } else {
                cameraCheckStatus.setText("Camera is not supported :(");
                cameraCheckStatus.setVisible(true);
                stopCameraBtn.setEnabled(false);
            }
        });

        createVideo();
        videoContainer.add(videoComponent);
    }

    private void createVideo() {
        videoComponent = new VideoComponent("cameraVideo", true);
        videoComponent.getStyle().setHeight("auto");
        videoComponent.getStyle().setMaxWidth("100%");
    }

    @Subscribe(id = "stopCameraBtn", subject = "singleClickListener")
    public void onStopCameraBtnClick(final ClickEvent<JmixButton> event) {
        if (cameraStarted) {
            deviceCameraAccess.stopCamera(videoComponent);
        } else {
            deviceCameraAccess.startCamera(videoComponent, frontCamera.getValue());
        }
        cameraStarted = !cameraStarted;
    }

    @Override
    public void streamingSucceeded(InputStream fileContent, String fileName, String mimeType) {
        FileRef photoFileRef = fileStorage.saveStream(fileName, fileContent);
        getEditedItem().setPhoto(photoFileRef);
        notifications.create(messageBundle.getMessage("photo.upload.success"))
                .withType(Notifications.Type.DEFAULT)
                .show();
    }

    private CarAccidentCase getEditedItem() {
        return carAccidentCaseDc.getItem();
    }

    @Override
    public void streamingFailed(Exception exception) {
        log.warn("Photo upload failed", exception);

        notifications.create(messageBundle.getMessage("photo.upload.fail"))
                .withType(Notifications.Type.WARNING)
                .show();
    }

    @Subscribe
    public void onBeforeClose(final BeforeCloseEvent event) {
        unregisterPhotoUploadStreaming();
        deviceCameraAccess.stopCamera(videoComponent);
    }

    private void unregisterPhotoUploadStreaming() {
        if (streamRegistration != null) {
            streamRegistration.unregister();
            streamRegistration = null;
        }
    }

    @Subscribe(id = "uploadPhotoShot", subject = "singleClickListener")
    public void onUploadPhotoShotClick(final ClickEvent<JmixButton> event) {
        deviceCameraAccess.uploadPhoto(videoComponent, streamRegistration.getResourceUri());
    }

    @Subscribe(id = "createBtn", subject = "singleClickListener")
    public void onCreateBtnClick(final ClickEvent<JmixButton> event) {
        // validate
        ValidationErrors validationErrors = viewValidation.validateUiComponents(this);
        if (getEditedItem().getPhoto() == null) {
            validationErrors.add(messageBundle.getMessage("photo.required"));
        }
        if (!validationErrors.isEmpty()) {
            viewValidation.showValidationErrors(validationErrors);
            return;
        }

        commitAndClose();
    }

    private void commitAndClose() {
        dataContext.save(false);
        notifications.create(messageBundle.getMessage("case.quickCreated"))
                        .withType(Notifications.Type.SUCCESS)
                                .show();
        close(StandardOutcome.SAVE);
    }
}