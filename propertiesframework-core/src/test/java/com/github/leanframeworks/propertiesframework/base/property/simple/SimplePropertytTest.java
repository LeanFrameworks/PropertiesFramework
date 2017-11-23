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

package com.github.leanframeworks.propertiesframework.base.property.simple;

import com.github.leanframeworks.propertiesframework.api.property.PropertyChange;
import com.github.leanframeworks.propertiesframework.api.property.PropertyChangeListener;
import org.junit.Test;

import static com.github.leanframeworks.propertiesframework.test.TestUtils.matches;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @see SimpleProperty
 */
public class SimplePropertytTest {

    @Test
    public void testInitialValue() {
        SimpleProperty<Integer> property = new SimpleProperty<>();
        assertEquals(null, property.getValue());

        property = new SimpleProperty<>(5);
        assertEquals(Integer.valueOf(5), property.getValue());
    }

    @Test
    public void testReadWrite() {
        SimpleProperty<Double> property = new SimpleProperty<>();

        property.setValue(8.2);
        assertEquals(Double.valueOf(8.2), property.getValue());

        property.setValue(234.3245);
        assertEquals(Double.valueOf(234.3245), property.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testValueChangeEvent() {
        SimpleProperty<Integer> property = new SimpleProperty<>();
        PropertyChangeListener<Integer> listenerMock = (PropertyChangeListener<Integer>) mock(PropertyChangeListener.class);

        property.addChangeListener(listenerMock);
        property.setValue(3);
        property.setValue(4);

        // Check exactly two events fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange(property, null, 3)));
        verify(listenerMock).propertyChanged(matches(new PropertyChange(property, 3, 4)));
        verify(listenerMock, times(2)).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInhibitAndValueChangeEvent() {
        SimpleProperty<Integer> property = new SimpleProperty<>(null);
        PropertyChangeListener<Integer> listenerMock = (PropertyChangeListener<Integer>) mock(PropertyChangeListener.class);
        property.addChangeListener(listenerMock);

        property.setInhibited(true);
        property.setValue(3);
        property.setValue(4);
        property.setInhibited(false);

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange(property, null, 4)));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInhibitAndNoValueChangeEvent() {
        SimpleProperty<Integer> property = new SimpleProperty<>(null);
        PropertyChangeListener<Integer> listenerMock = (PropertyChangeListener<Integer>) mock(PropertyChangeListener.class);
        property.addChangeListener(listenerMock);

        property.setInhibited(true);
        property.setValue(3);
        property.setValue(4);
        property.setValue(null);
        property.setInhibited(false);

        // Check no event fired
        verify(listenerMock, never()).propertyChanged(any());
    }
}
