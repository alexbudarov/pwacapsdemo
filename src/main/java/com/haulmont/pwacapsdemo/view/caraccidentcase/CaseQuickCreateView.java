package com.haulmont.pwacapsdemo.view.caraccidentcase;


import com.haulmont.pwacapsdemo.component.DeviceCameraAccess;
import com.haulmont.pwacapsdemo.component.GeoLocationAccess;
import com.haulmont.pwacapsdemo.component.VideoComponent;
import com.haulmont.pwacapsdemo.entity.CarAccidentCase;
import com.haulmont.pwacapsdemo.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import io.jmix.core.TimeSource;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "cases/quick-create", layout = MainView.class)
@ViewController(id = "CaseQuickCreate")
@ViewDescriptor(path = "case-quick-create.xml")
public class CaseQuickCreateView extends StandardView {

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

    @Subscribe(id = "cancelBtn", subject = "singleClickListener")
    public void onCancelBtnClick(final ClickEvent<JmixButton> event) {
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
            carAccidentCaseDc.getItem().setLatitude(c.latitude());
            carAccidentCaseDc.getItem().setLongitude(c.longitude());
        });

        startCamera();
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
}