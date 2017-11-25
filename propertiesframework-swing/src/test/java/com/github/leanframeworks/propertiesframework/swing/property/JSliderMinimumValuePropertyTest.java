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

import javax.swing.JSlider;

import static com.github.leanframeworks.propertiesframework.test.TestUtils.matches;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see JSliderMinimumValueProperty
 */
public class JSliderMinimumValuePropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        JSlider slider = new JSlider();
        slider.setMinimum(10);
        ReadableWritableProperty<Integer> minValueProperty = new JSliderMinimumValueProperty(slider);
        PropertyChangeListener<Integer> listenerMock = mock(PropertyChangeListener.class);
        minValueProperty.addChangeListener(listenerMock);

        assertEquals(10, minValueProperty.getValue().intValue());
        minValueProperty.setValue(14);
        assertEquals(14, slider.getMinimum());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(minValueProperty, 10, 14)));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        JSlider slider = new JSlider();
        slider.setMinimum(13);
        ReadableWritableProperty<Integer> minValueProperty = new JSliderMinimumValueProperty(slider);
        PropertyChangeListener<Integer> listenerMock = mock(PropertyChangeListener.class);
        minValueProperty.addChangeListener(listenerMock);

        assertEquals(13, minValueProperty.getValue().intValue());
        slider.setMinimum(88);
        assertEquals(88, minValueProperty.getValue().intValue());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(minValueProperty, 13, 88)));
        verify(listenerMock).propertyChanged(any());
    }
}
