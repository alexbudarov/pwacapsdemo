package com.haulmont.pwacapsdemo.view.caraccidentcase;


import com.haulmont.pwacapsdemo.component.GeoLocationAccess;
import com.haulmont.pwacapsdemo.entity.CarAccidentCase;
import com.haulmont.pwacapsdemo.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.TimeSource;
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
    @ViewComponent
    private InstanceContainer<CarAccidentCase> carAccidentCaseDc;
    @ViewComponent
    private DataContext dataContext;
    @Autowired
    private TimeSource timeSource;

    @Subscribe(id = "cancelBtn", subject = "singleClickListener")
    public void onCancelBtnClick(final ClickEvent<JmixButton> event) {
        close(StandardOutcome.DISCARD);
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        geoLocationAccess.injectDependencies();

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
    }
}