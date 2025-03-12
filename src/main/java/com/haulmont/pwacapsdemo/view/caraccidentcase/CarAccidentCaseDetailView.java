package com.haulmont.pwacapsdemo.view.caraccidentcase;

import com.haulmont.pwacapsdemo.component.GeoLocationAccess;
import com.haulmont.pwacapsdemo.entity.CarAccidentCase;
import com.haulmont.pwacapsdemo.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "car-accident-cases/:id", layout = MainView.class)
@ViewController(id = "CarAccidentCase.detail")
@ViewDescriptor(path = "car-accident-case-detail-view.xml")
@EditedEntityContainer("carAccidentCaseDc")
public class CarAccidentCaseDetailView extends StandardDetailView<CarAccidentCase> {
    @Autowired
    private GeoLocationAccess geoLocationAccess;

    @Subscribe
    public void onInit(final InitEvent event) {
        geoLocationAccess.injectDependencies();
    }

    @Subscribe
    public void onInitEntity(final InitEntityEvent<CarAccidentCase> event) {
        geoLocationAccess.requestGeoLocation(this, c -> {
            getEditedEntity().setLatitude(c.latitude());
            getEditedEntity().setLongitude(c.longitude());
        });
    }
}