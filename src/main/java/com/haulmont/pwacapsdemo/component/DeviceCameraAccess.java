package com.haulmont.pwacapsdemo.component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.shared.ui.LoadMode;
import io.jmix.flowui.Notifications;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

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
     * Stop playing video from camera in selected <video/> element
     *
     * @param videoComponent video element
     */
    public void stopCamera(VideoComponent videoComponent) {
        videoComponent.getElement().executeJs("stopCamera()");
    }
}

