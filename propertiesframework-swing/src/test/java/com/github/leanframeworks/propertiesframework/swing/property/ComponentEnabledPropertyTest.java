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

import com.github.leanframeworks.propertiesframework.api.property.ReadableWritableProperty;
import com.github.leanframeworks.propertiesframework.api.property.ValueChangeListener;
import org.junit.Test;

import javax.swing.JLabel;
import java.awt.Component;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see ComponentEnabledProperty
 */
public class ComponentEnabledPropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        Component component = new JLabel();
        ReadableWritableProperty<Boolean> property = new ComponentEnabledProperty(component);
        ValueChangeListener<Boolean> listenerMock = (ValueChangeListener<Boolean>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertTrue(property.getValue());
        assertTrue(component.isEnabled());
        property.setValue(false);
        assertFalse(component.isEnabled());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, true, false);
        verify(listenerMock).valueChanged(any(ComponentEnabledProperty.class), anyBoolean(), anyBoolean());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        Component component = new JLabel();
        ReadableWritableProperty<Boolean> property = new ComponentEnabledProperty(component);
        ValueChangeListener<Boolean> listenerMock = (ValueChangeListener<Boolean>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertTrue(property.getValue());
        component.setEnabled(false);
        assertFalse(property.getValue());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, true, false);
        verify(listenerMock).valueChanged(any(ComponentEnabledProperty.class), anyBoolean(), anyBoolean());
    }
}
