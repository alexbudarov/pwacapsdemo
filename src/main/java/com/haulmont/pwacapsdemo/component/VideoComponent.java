package com.haulmont.pwacapsdemo.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;

/**
 * Component representing &lt;video/&gt; HTML element.
 * See https://vaadin.com/docs/latest/flow/create-ui/creating-components/basic
 */
@Tag("video")
public class VideoComponent extends Component {
    public VideoComponent(String id, boolean autoplay) {
        getElement().setProperty("id", id);
        getElement().setProperty("autoplay", autoplay);
    }
}
