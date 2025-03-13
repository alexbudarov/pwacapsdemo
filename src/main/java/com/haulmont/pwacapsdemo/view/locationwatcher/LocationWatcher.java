package com.haulmont.pwacapsdemo.view.locationwatcher;


import com.haulmont.pwacapsdemo.component.GeoLocationAccess;
import com.haulmont.pwacapsdemo.view.main.MainView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "location-watcher", layout = MainView.class)
@ViewController(id = "LocationWatcherView")
@ViewDescriptor(path = "location-watcher.xml")
public class LocationWatcher extends StandardView {
    @Autowired
    private GeoLocationAccess geoLocationAccess;

    @ViewComponent
    private JmixTextArea logArea;
    @ViewComponent
    private Span coordsSpan;

    @Subscribe
    public void onInit(final InitEvent event) {
        geoLocationAccess.injectDependencies();
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        geoLocationAccess.watchGeoLocationChanges(this, coords -> {
            String text = String.format("lat: %.5f, lon: %.5f, time: %s",
                    coords.latitude(), coords.longitude(), coords.timestamp().toString()
            );
            coordsSpan.setText(text);

            logArea.setValue(text + "\n" + (logArea.getValue() != null ? logArea.getValue() : ""));
        });
    }

    @Subscribe
    public void onBeforeClose(final BeforeCloseEvent event) {
        geoLocationAccess.stopWatchGeoLocationChanges();
    }
}