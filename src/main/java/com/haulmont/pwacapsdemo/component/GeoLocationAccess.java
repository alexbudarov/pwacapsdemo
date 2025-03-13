package com.haulmont.pwacapsdemo.component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.shared.ui.LoadMode;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.view.View;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.function.Consumer;

/**
 * Bean to be used in Views that need obtaining current geographical location of the client.
 */
@Component
public class GeoLocationAccess {

    public GeoLocationAccess(Notifications notifications) {
        this.notifications = notifications;
    }

    private final Notifications notifications;

    public void injectDependencies() {
        Page page = UI.getCurrent().getPage();
        page.addJavaScript("js/geolocation.js", LoadMode.EAGER);
    }

    public void requestGeoLocation(View view, Consumer<Coordinates> locationConsumer) {
        view.getElement().addEventListener("geo-location-obtained", e -> {
                    double lat = e.getEventData().getNumber("event.lat");
                    double lon = e.getEventData().getNumber("event.lon");

                    locationConsumer.accept(new Coordinates(lat, lon));
                })
                .addEventData("event.lat")
                .addEventData("event.lon");

        view.getElement().addEventListener("geo-location-inaccessible", e -> {
                    String message = e.getEventData().getString("event.message");
                    notifications.create(message)
                            .withType(Notifications.Type.WARNING)
                            .show();
                })
                .addEventData("event.message");


        // see https://vaadin.com/docs/v14/flow/element-api/client-server-rpc#executejs-method
        view.getElement().executeJs("requestGeoLocation($0)", view.getElement());
    }

    /**
     * Register watcher to obtain new position each time when it changes.
     * See: https://developer.mozilla.org/en-US/docs/Web/API/Geolocation/watchPosition
     *
     * @param view target view
     * @param locationConsumer consumer of timestamped coordinates
     */
    public void watchGeoLocationChanges(View view, Consumer<TimedCoordinates> locationConsumer) {
        view.getElement().addEventListener("geo-location-obtained", e -> {
                    double lat = e.getEventData().getNumber("event.lat");
                    double lon = e.getEventData().getNumber("event.lon");
                    long timestampMillis = (long) e.getEventData().getNumber("event.timestamp");
                    Instant ts = Instant.ofEpochMilli(timestampMillis);

                    locationConsumer.accept(new TimedCoordinates(lat, lon, ts));
                })
                .addEventData("event.lat")
                .addEventData("event.lon")
                .addEventData("event.timestamp");

        view.getElement().addEventListener("geo-location-inaccessible", e -> {
                    String message = e.getEventData().getString("event.message");
                    notifications.create(message)
                            .withType(Notifications.Type.WARNING)
                            .show();
                })
                .addEventData("event.message");


        // see https://vaadin.com/docs/v14/flow/element-api/client-server-rpc#executejs-method
        view.getElement().executeJs("watchGeolocationChanges($0)", view.getElement());
    }

    /**
     * Stop geolocation watcher if it was registered previously.
     * See https://developer.mozilla.org/en-US/docs/Web/API/Geolocation/clearWatch
     */
    public void stopWatchGeoLocationChanges() {
        Page page = UI.getCurrent().getPage();
        page.executeJs("stopWatchGeolocationChanges()");
    }
}
