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

import javax.swing.JLabel;
import java.awt.event.KeyEvent;

import static com.github.leanframeworks.propertiesframework.test.TestUtils.matches;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see JLabelDisplayedMnemonicProperty
 */
public class JLabelDisplayedMnemonicPropertyTest {

    private static final Integer MNEMONIC1 = KeyEvent.VK_C;

    private static final Integer MNEMONIC2 = KeyEvent.VK_O;

    @Test
    public void testInitialValueFromLabel() {
        JLabel label = new JLabel();
        ReadableWritableProperty<Integer> mnemonicProperty = new JLabelDisplayedMnemonicProperty(label);

        assertEquals(KeyEvent.VK_UNDEFINED, label.getDisplayedMnemonic());
        assertEquals(Integer.valueOf(KeyEvent.VK_UNDEFINED), mnemonicProperty.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNullFromProperty() {
        JLabel label = new JLabel();
        label.setDisplayedMnemonic(MNEMONIC1);
        ReadableWritableProperty<Integer> mnemonicProperty = new JLabelDisplayedMnemonicProperty(label);
        PropertyChangeListener<Integer> listenerMock = (PropertyChangeListener<Integer>) mock(PropertyChangeListener.class);
        mnemonicProperty.addChangeListener(listenerMock);

        assertEquals(MNEMONIC1, mnemonicProperty.getValue());
        mnemonicProperty.setValue(null);
        assertEquals(KeyEvent.VK_UNDEFINED, label.getDisplayedMnemonic());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(mnemonicProperty, MNEMONIC1, KeyEvent.VK_UNDEFINED)));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        JLabel label = new JLabel();
        label.setDisplayedMnemonic(MNEMONIC1);
        ReadableWritableProperty<Integer> mnemonicProperty = new JLabelDisplayedMnemonicProperty(label);
        PropertyChangeListener<Integer> listenerMock = (PropertyChangeListener<Integer>) mock(PropertyChangeListener.class);
        mnemonicProperty.addChangeListener(listenerMock);

        assertEquals(MNEMONIC1, mnemonicProperty.getValue());
        mnemonicProperty.setValue(MNEMONIC2);
        assertEquals(MNEMONIC2, Integer.valueOf(label.getDisplayedMnemonic()));

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(mnemonicProperty, MNEMONIC1, MNEMONIC2)));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNoDisplayedMnemonicFromComponent() {
        JLabel label = new JLabel();
        label.setDisplayedMnemonic(MNEMONIC1);
        ReadableWritableProperty<Integer> mnemonicProperty = new JLabelDisplayedMnemonicProperty(label);
        PropertyChangeListener<Integer> listenerMock = (PropertyChangeListener<Integer>) mock(PropertyChangeListener.class);
        mnemonicProperty.addChangeListener(listenerMock);

        assertEquals(MNEMONIC1, mnemonicProperty.getValue());
        label.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        assertEquals(Integer.valueOf(KeyEvent.VK_UNDEFINED), mnemonicProperty.getValue());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(mnemonicProperty, MNEMONIC1, KeyEvent.VK_UNDEFINED)));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        JLabel label = new JLabel();
        label.setDisplayedMnemonic(MNEMONIC1);
        ReadableWritableProperty<Integer> mnemonicProperty = new JLabelDisplayedMnemonicProperty(label);
        PropertyChangeListener<Integer> listenerMock = (PropertyChangeListener<Integer>) mock(PropertyChangeListener.class);
        mnemonicProperty.addChangeListener(listenerMock);

        assertEquals(MNEMONIC1, mnemonicProperty.getValue());
        label.setDisplayedMnemonic(MNEMONIC2);
        assertEquals(MNEMONIC2, mnemonicProperty.getValue());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(mnemonicProperty, MNEMONIC1, MNEMONIC2)));
        verify(listenerMock).propertyChanged(any());
    }
}
