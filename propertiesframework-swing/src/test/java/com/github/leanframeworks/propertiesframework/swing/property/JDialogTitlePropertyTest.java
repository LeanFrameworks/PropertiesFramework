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

import javax.swing.JDialog;

import static com.github.leanframeworks.propertiesframework.test.TestUtils.matches;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see JDialogTitleProperty
 */
public class JDialogTitlePropertyTest {

    private static final String TITLE1 = "First Title";

    private static final String TITLE2 = "Second Title";

    @SuppressWarnings("unchecked")
    @Test
    public void testNullFromProperty() {
        JDialog dialog = new JDialog((JDialog) null, TITLE1);
        ReadableWritableProperty<String> titleProperty = new JDialogTitleProperty(dialog);
        PropertyChangeListener<String> listenerMock = (PropertyChangeListener<String>) mock(PropertyChangeListener.class);
        titleProperty.addChangeListener(listenerMock);

        assertEquals(TITLE1, titleProperty.getValue());
        titleProperty.setValue(null);
        assertEquals(null, dialog.getTitle());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(titleProperty, TITLE1, null)));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        JDialog dialog = new JDialog((JDialog) null, TITLE1);
        ReadableWritableProperty<String> titleProperty = new JDialogTitleProperty(dialog);
        PropertyChangeListener<String> listenerMock = (PropertyChangeListener<String>) mock(PropertyChangeListener.class);
        titleProperty.addChangeListener(listenerMock);

        assertEquals(TITLE1, titleProperty.getValue());
        titleProperty.setValue(TITLE2);
        assertEquals(TITLE2, dialog.getTitle());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(titleProperty, TITLE1, TITLE2)));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNullFromComponent() {
        JDialog dialog = new JDialog((JDialog) null, TITLE1);
        ReadableWritableProperty<String> titleProperty = new JDialogTitleProperty(dialog);
        PropertyChangeListener<String> listenerMock = (PropertyChangeListener<String>) mock(PropertyChangeListener.class);
        titleProperty.addChangeListener(listenerMock);

        assertEquals(TITLE1, titleProperty.getValue());
        dialog.setTitle(null);
        assertEquals(null, titleProperty.getValue());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(titleProperty, TITLE1, null)));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        JDialog dialog = new JDialog((JDialog) null, TITLE1);
        ReadableWritableProperty<String> titleProperty = new JDialogTitleProperty(dialog);
        PropertyChangeListener<String> listenerMock = (PropertyChangeListener<String>) mock(PropertyChangeListener.class);
        titleProperty.addChangeListener(listenerMock);

        assertEquals(TITLE1, titleProperty.getValue());
        dialog.setTitle(TITLE2);
        assertEquals(TITLE2, titleProperty.getValue());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(titleProperty, TITLE1, TITLE2)));
        verify(listenerMock).propertyChanged(any());
    }
}
