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
import org.junit.Before;
import org.junit.Test;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import static com.github.leanframeworks.propertiesframework.test.TestUtils.matches;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @see JTableEditingProperty
 */
public class JTableEditingPropertyTest {

    private JTable table;

    private JTableEditingProperty property;

    private PropertyChangeListener<Boolean> listenerMock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        tableModel.addColumn("A");
        tableModel.addColumn("B");
        tableModel.addColumn("C");

        tableModel.addRow(new Object[]{1, 2, 3});
        tableModel.addRow(new Object[]{4, 5, 6});
        tableModel.addRow(new Object[]{7, 8, 9});

        // Create table
        table = new JTable(tableModel);

        // Create property
        property = new JTableEditingProperty(table);
        listenerMock = (PropertyChangeListener<Boolean>) mock(PropertyChangeListener.class);
        property.addChangeListener(listenerMock);
    }

    @Test
    public void testEditing() {
        assertFalse(property.getValue());
        table.editCellAt(1, 1);
        assertTrue(property.getValue());

        table.getCellEditor().cancelCellEditing();
        assertFalse(property.getValue());

        // Check fired events
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, false, true)));
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, true, false)));
        verify(listenerMock, times(2)).propertyChanged(any());
    }

    @Test
    public void testDispose() {
        table.editCellAt(1, 1);
        table.getCellEditor().cancelCellEditing();

        property.dispose();

        table.editCellAt(0, 0);
        table.getCellEditor().cancelCellEditing();
        table.editCellAt(0, 1);
        table.getCellEditor().cancelCellEditing();

        property.dispose();
        property.dispose();

        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, false, true)));
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, true, false)));
        verifyNoMoreInteractions(listenerMock);
    }
}
