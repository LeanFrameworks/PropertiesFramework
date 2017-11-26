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

import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import static com.github.leanframeworks.propertiesframework.test.TestUtils.matches;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @see JTextComponentTextProperty
 */
public class JTextComponentTextPropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromPropertyWithoutInitialText() throws BadLocationException {
        JTextComponent component = new JTextField();
        ReadableWritableProperty<String> property = new JTextComponentTextProperty(component);
        PropertyChangeListener<String> listenerMock = (PropertyChangeListener<String>) mock(PropertyChangeListener.class);
        property.addChangeListener(listenerMock);

        assertEquals("", component.getDocument().getText(0, component.getDocument().getLength()));
        assertEquals("", component.getText());
        assertEquals("", property.getValue());
        property.setValue("new text");
        assertEquals("new text", component.getText());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, "", "new text")));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponentWithoutInitialText() throws BadLocationException {
        JTextComponent component = new JTextField();
        ReadableWritableProperty<String> property = new JTextComponentTextProperty(component);
        PropertyChangeListener<String> listenerMock = (PropertyChangeListener<String>) mock(PropertyChangeListener.class);
        property.addChangeListener(listenerMock);

        assertEquals("", component.getDocument().getText(0, component.getDocument().getLength()));
        assertEquals("", component.getText());
        assertEquals("", property.getValue());
        component.setText("new text");
        assertEquals("new text", property.getValue());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, "", "new text")));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromPropertyWithInitialText() throws BadLocationException {
        JTextComponent component = new JTextField("initial text");
        ReadableWritableProperty<String> property = new JTextComponentTextProperty(component);
        PropertyChangeListener<String> listenerMock = (PropertyChangeListener<String>) mock(PropertyChangeListener.class);
        property.addChangeListener(listenerMock);

        assertEquals("initial text", component.getDocument().getText(0, component.getDocument().getLength()));
        assertEquals("initial text", component.getText());
        assertEquals("initial text", property.getValue());
        property.setValue("new text");
        assertEquals("new text", component.getText());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, "initial text", "new text")));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponentWithInitialText() throws BadLocationException {
        JTextComponent component = new JTextField("initial text");
        ReadableWritableProperty<String> property = new JTextComponentTextProperty(component);
        PropertyChangeListener<String> listenerMock = (PropertyChangeListener<String>) mock(PropertyChangeListener.class);
        property.addChangeListener(listenerMock);

        assertEquals("initial text", component.getDocument().getText(0, component.getDocument().getLength()));
        assertEquals("initial text", component.getText());
        assertEquals("initial text", property.getValue());
        component.setText("new text");
        assertEquals("new text", property.getValue());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, "", "new text")));
        verify(listenerMock, times(2)).propertyChanged(any());
    }

    @Test
    public void testDispose() {
        JTextComponent component = new JTextField("initial text");
        JTextComponentTextProperty property = new JTextComponentTextProperty(component);
        PropertyChangeListener<String> listener = mock(PropertyChangeListener.class);
        property.addChangeListener(listener);

        component.setText("new text");
        component.setText("another one");

        property.dispose();

        component.setText("yet another text");
        component.setText("");

        property.dispose();
        property.dispose();
        property.dispose();

        verify(listener).propertyChanged(matches(new PropertyChange<>(property, "initial text", "")));
        verify(listener).propertyChanged(matches(new PropertyChange<>(property, "", "new text")));
        verify(listener).propertyChanged(matches(new PropertyChange<>(property, "new text", "")));
        verify(listener).propertyChanged(matches(new PropertyChange<>(property, "", "another one")));
        verifyNoMoreInteractions(listener);
    }
}
