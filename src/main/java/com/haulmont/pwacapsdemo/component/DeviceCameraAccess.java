package com.haulmont.pwacapsdemo.component;

import com.haulmont.pwacapsdemo.component.upload.InMemoryStreamVariable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.*;
import com.vaadin.flow.shared.ui.LoadMode;
import io.jmix.flowui.view.View;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.function.Consumer;

/**
 * See camera.js for client-side javascript code
 */
@Component
public class DeviceCameraAccess {

    public void injectDependencies() {
        Page page = UI.getCurrent().getPage();
        page.addJavaScript("js/camera.js", LoadMode.EAGER);
    }

    /**
     * Check whether the camera is supported.
     *
     * @param resultConsumer asynchronous callback receiving result (yes / no)
     */
    public void checkCameraSupported(Consumer<Boolean> resultConsumer) {
        Page page = UI.getCurrent().getPage();
        page.executeJs("return isCameraSupported()").then(jsonValue -> {
            boolean result = jsonValue.asBoolean();
            resultConsumer.accept(result);
        });
    }

    /**
     * Start playing video from camera in selected <video/> element
     *
     * @param videoComponent video element
     * @param useFrontCamera use front or back camera?
     */
    public void startCamera(VideoComponent videoComponent, boolean useFrontCamera) {
        videoComponent.getElement().executeJs("startCamera($0, $1)", videoComponent.getElement(), useFrontCamera);
    }

    /**
     * Stop playing video from camera in selected <video/> element, if it is active.
     *
     * @param videoComponent video element (not really necessary here)
     */
    public void stopCamera(VideoComponent videoComponent) {
        // call globally via page, to make it work even on page close
        UI.getCurrent().getPage().executeJs("stopCamera()");
    }

    /**
     * Take screenshot of the camera from selected <video/> element, and upload it to the server.
     *
     * @param videoComponent video element (not really necessary here)
     */
    public void uploadPhoto(VideoComponent videoComponent, URI uploadUri) {
        videoComponent.getElement().executeJs("takeAndUploadScreenshot($0, $1)",
                videoComponent.getElement(), uploadUri.toString());
    }

    /**
     * Register StreamReceiver for custom file uploading.
     * @param view view
     * @param listener listener to get uploading events
     *
     * @return registration object, needed for later unregistering
     */
    public StreamRegistration registerStreamReceiver(View<?> view, InMemoryStreamVariable.StreamingFinishedListener listener) {
        // see: https://vaadin.com/docs/latest/flow/advanced/stream-resources
        StreamVariable streamVariable = new InMemoryStreamVariable(listener);
        StreamReceiver streamReceiver = new StreamReceiver(view.getElement().getNode(), "upload", streamVariable);
        view.getElement().setAttribute("target", streamReceiver);

        StreamResourceRegistry resourceRegistry = VaadinSession.getCurrent().getResourceRegistry();
        return resourceRegistry.registerResource(streamReceiver);
    }
}

