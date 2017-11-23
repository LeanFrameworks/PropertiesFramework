/*
 * Copyright (c) 2017, LeanFrameworks
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.leanframeworks.propertiesframework.swing.property;

import com.github.leanframeworks.propertiesframework.api.property.PropertyChange;
import com.github.leanframeworks.propertiesframework.api.property.PropertyChangeListener;
import com.github.leanframeworks.propertiesframework.api.property.ReadableWritableProperty;
import org.junit.Test;

import javax.swing.JToggleButton;

import static com.github.leanframeworks.propertiesframework.test.TestUtils.matches;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @see JToggleButtonSelectedProperty
 */
public class JToggleButtonSelectedPropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        JToggleButton component = new JToggleButton();
        ReadableWritableProperty<Boolean> property = new JToggleButtonSelectedProperty(component);
        PropertyChangeListener<Boolean> listenerMock = (PropertyChangeListener<Boolean>) mock(PropertyChangeListener.class);
        property.addChangeListener(listenerMock);

        assertFalse(property.getValue());
        assertFalse(component.isSelected());
        property.setValue(true);
        assertTrue(component.isSelected());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, false, true)));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        JToggleButton component = new JToggleButton();
        component.setSelected(true);
        ReadableWritableProperty<Boolean> property = new JToggleButtonSelectedProperty(component);
        PropertyChangeListener<Boolean> listenerMock = (PropertyChangeListener<Boolean>) mock(PropertyChangeListener.class);
        property.addChangeListener(listenerMock);

        assertTrue(property.getValue());
        component.setSelected(false);
        assertFalse(property.getValue());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, true, false)));
        verify(listenerMock).propertyChanged(any());
    }

    @Test
    public void testDispose() {
        JToggleButton component = new JToggleButton();
        JToggleButtonSelectedProperty property = new JToggleButtonSelectedProperty(component);
        PropertyChangeListener<Boolean> listener = mock(PropertyChangeListener.class);
        property.addChangeListener(listener);

        component.setSelected(true);
        component.setSelected(false);

        property.dispose();

        component.setSelected(true);
        component.setSelected(false);

        property.dispose();
        property.dispose();
        property.dispose();

        verify(listener).propertyChanged(matches(new PropertyChange<>(property, false, true)));
        verify(listener).propertyChanged(matches(new PropertyChange<>(property, true, false)));
        verifyNoMoreInteractions(listener);
    }
}
