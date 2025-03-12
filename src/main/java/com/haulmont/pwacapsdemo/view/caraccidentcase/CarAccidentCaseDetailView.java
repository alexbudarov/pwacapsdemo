package com.haulmont.pwacapsdemo.view.caraccidentcase;

import com.haulmont.pwacapsdemo.entity.CarAccidentCase;
import com.haulmont.pwacapsdemo.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "car-accident-cases/:id", layout = MainView.class)
@ViewController(id = "CarAccidentCase.detail")
@ViewDescriptor(path = "car-accident-case-detail-view.xml")
@EditedEntityContainer("carAccidentCaseDc")
public class CarAccidentCaseDetailView extends StandardDetailView<CarAccidentCase> {
}