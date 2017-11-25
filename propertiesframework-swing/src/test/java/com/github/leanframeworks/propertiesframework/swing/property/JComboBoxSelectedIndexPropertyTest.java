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
import org.junit.Before;
import org.junit.Test;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import static com.github.leanframeworks.propertiesframework.test.TestUtils.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @see JComboBoxSelectedIndexProperty
 */
public class JComboBoxSelectedIndexPropertyTest {

    private JComboBox comboBox;

    private JComboBoxSelectedIndexProperty property;

    private PropertyChangeListener<Integer> listenerMock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        // Create list model
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();

        comboBoxModel.addElement(1);
        comboBoxModel.addElement(2);
        comboBoxModel.addElement(3);

        // Create list
        comboBox = new JComboBox(comboBoxModel);

        // Create property
        property = new JComboBoxSelectedIndexProperty(comboBox);
        listenerMock = (PropertyChangeListener<Integer>) mock(PropertyChangeListener.class);
        property.addChangeListener(listenerMock);
    }

    @Test
    public void testDispose() {
        comboBox.setSelectedIndex(1);
        comboBox.setSelectedIndex(2);

        property.dispose();

        comboBox.setSelectedIndex(0);
        comboBox.setSelectedIndex(-1);

        property.dispose();
        property.dispose();
        property.dispose();

        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, 0, 1)));
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, 1, 2)));
        verifyNoMoreInteractions(listenerMock);
    }
}
