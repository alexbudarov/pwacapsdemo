package com.haulmont.pwacapsdemo.view.caraccidentcase;

import com.haulmont.pwacapsdemo.entity.CarAccidentCase;
import com.haulmont.pwacapsdemo.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "car-accident-cases", layout = MainView.class)
@ViewController(id = "CarAccidentCase.list")
@ViewDescriptor(path = "car-accident-case-list-view.xml")
@LookupComponent("carAccidentCasesDataGrid")
@DialogMode(width = "64em")
public class CarAccidentCaseListView extends StandardListView<CarAccidentCase> {
}