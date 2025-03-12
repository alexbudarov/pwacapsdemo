package com.haulmont.pwacapsdemo.view.caraccidentcase;


import com.haulmont.pwacapsdemo.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;

@Route(value = "cases/quick-create", layout = MainView.class)
@ViewController(id = "CaseQuickCreate")
@ViewDescriptor(path = "case-quick-create.xml")
public class CaseQuickCreateView extends StandardView {

    @Subscribe(id = "cancelBtn", subject = "singleClickListener")
    public void onCancelBtnClick(final ClickEvent<JmixButton> event) {
        close(StandardOutcome.DISCARD);
    }
}