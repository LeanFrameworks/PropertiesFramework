/*-
 * #%L
 * PropertiesFramework :: Swing Support
 * %%
 * Copyright (C) 2017 LeanFrameworks
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package com.github.leanframeworks.propertiesframework.swing.property;

import com.github.leanframeworks.propertiesframework.api.property.PropertyChange;
import com.github.leanframeworks.propertiesframework.api.property.PropertyChangeListener;
import com.github.leanframeworks.propertiesframework.api.property.ReadableWritableProperty;
import org.junit.Test;

import javax.swing.JComponent;
import javax.swing.JLabel;

import static com.github.leanframeworks.propertiesframework.test.TestUtils.matches;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see JComponentToolTipTextProperty
 */
public class JComponentToolTipTextPropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNullFromProperty() {
        JComponent component = new JLabel();
        component.setToolTipText("Tooltip");
        ReadableWritableProperty<String> toolTipProperty = new JComponentToolTipTextProperty(component);
        PropertyChangeListener<String> listenerMock = (PropertyChangeListener<String>) mock(PropertyChangeListener.class);
        toolTipProperty.addChangeListener(listenerMock);

        assertEquals("Tooltip", toolTipProperty.getValue());
        toolTipProperty.setValue(null);
        assertEquals(null, component.getToolTipText());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(toolTipProperty, "Tooltip", null)));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        JComponent component = new JLabel();
        component.setToolTipText("Tooltip");
        ReadableWritableProperty<String> toolTipProperty = new JComponentToolTipTextProperty(component);
        PropertyChangeListener<String> listenerMock = (PropertyChangeListener<String>) mock(PropertyChangeListener.class);
        toolTipProperty.addChangeListener(listenerMock);

        assertEquals("Tooltip", toolTipProperty.getValue());
        toolTipProperty.setValue("Another tooltip");
        assertEquals("Another tooltip", component.getToolTipText());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(toolTipProperty, "Tooltip", "Another tooltip")));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNullFromComponent() {
        JComponent component = new JLabel();
        component.setToolTipText("Tooltip");
        ReadableWritableProperty<String> toolTipProperty = new JComponentToolTipTextProperty(component);
        PropertyChangeListener<String> listenerMock = (PropertyChangeListener<String>) mock(PropertyChangeListener.class);
        toolTipProperty.addChangeListener(listenerMock);

        assertEquals("Tooltip", toolTipProperty.getValue());
        component.setToolTipText(null);
        assertEquals(null, toolTipProperty.getValue());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(toolTipProperty, "Tooltip", null)));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        JComponent component = new JLabel();
        component.setToolTipText("Tooltip");
        ReadableWritableProperty<String> toolTipProperty = new JComponentToolTipTextProperty(component);
        PropertyChangeListener<String> listenerMock = (PropertyChangeListener<String>) mock(PropertyChangeListener.class);
        toolTipProperty.addChangeListener(listenerMock);

        assertEquals("Tooltip", toolTipProperty.getValue());
        component.setToolTipText("Another tooltip");
        assertEquals("Another tooltip", toolTipProperty.getValue());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(toolTipProperty, "Tooltip", "Another tooltip")));
        verify(listenerMock).propertyChanged(any());
    }
}
