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

package com.github.leanframeworks.propertiesframework.base.binding;

import com.github.leanframeworks.propertiesframework.api.property.ReadableWritableProperty;
import com.github.leanframeworks.propertiesframework.api.property.ValueChangeListener;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleBooleanProperty;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleIntegerProperty;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleProperty;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleStringProperty;
import com.github.leanframeworks.propertiesframework.base.transform.AndBooleanAggregator;
import com.github.leanframeworks.propertiesframework.base.transform.ToStringTransformer;
import org.junit.Test;

import static com.github.leanframeworks.propertiesframework.base.binding.Binder.from;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see Binder
 */
public class BinderTest {

    @Test
    public void testMasterToSlave() {
        SimpleProperty<Integer> master = new SimpleProperty<>(5);
        SimpleProperty<Integer> slave = new SimpleProperty<>(0);
        from(master).to(slave);

        assertEquals(Integer.valueOf(5), master.getValue());
        assertEquals(master.getValue(), slave.getValue());

        master.setValue(8);

        assertEquals(Integer.valueOf(8), master.getValue());
        assertEquals(master.getValue(), slave.getValue());
    }

    @Test
    public void testMasterToSlaveWithTransformation() {
        SimpleProperty<Integer> master = new SimpleProperty<>(5);
        SimpleProperty<String> slave = new SimpleProperty<>("0");
        from(master).transform(new ToStringTransformer()).to(slave);

        assertEquals(Integer.valueOf(5), master.getValue());
        assertEquals("5", slave.getValue());

        master.setValue(8);

        assertEquals(Integer.valueOf(8), master.getValue());
        assertEquals("8", slave.getValue());
    }

    @Test
    public void testMasterToSlaveWithAggregation() {
        SimpleBooleanProperty master1 = new SimpleBooleanProperty(true);
        SimpleBooleanProperty master2 = new SimpleBooleanProperty(false);
        SimpleBooleanProperty master3 = new SimpleBooleanProperty(false);
        SimpleBooleanProperty slave = new SimpleBooleanProperty();
        from(master1, master2, master3).transform(new AndBooleanAggregator()).to(slave);

        assertEquals(false, slave.getValue());

        master2.setValue(true);
        master3.setValue(true);

        assertEquals(true, slave.getValue());
    }

    @Test
    public void testMasterToSlaveToMaster() {
        SimpleIntegerProperty first = new SimpleIntegerProperty(5);
        SimpleIntegerProperty second = new SimpleIntegerProperty(4);

        // The following should not result in an StackOverflowError
        from(first).to(second);
        from(second).to(first);

        first.setValue(12);
        assertEquals(Integer.valueOf(12), second.getValue());

        second.setValue(36);
        assertEquals(Integer.valueOf(36), first.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEventsFireWithOneSlave() {
        ReadableWritableProperty<String> master = new SimpleStringProperty("New value");
        ReadableWritableProperty<String> slave = new SimpleStringProperty("Initial value");

        ValueChangeListener<String> slaveListenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        slave.addValueChangeListener(slaveListenerMock);

        from(master).to(slave);

        // Check exactly one event fired
        verify(slaveListenerMock).valueChanged(slave, "Initial value", "New value");
        verify(slaveListenerMock).valueChanged(any(SimpleStringProperty.class), anyString(), anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEventsFiredWithSeveralSaves() {
        ReadableWritableProperty<String> master = new SimpleStringProperty("New value");
        ReadableWritableProperty<String> slave1 = new SimpleStringProperty("Initial value 1");
        ReadableWritableProperty<String> slave2 = new SimpleStringProperty("Initial value 2");

        ValueChangeListener<String> slave1ListenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        slave1.addValueChangeListener(slave1ListenerMock);
        ValueChangeListener<String> slave2ListenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        slave2.addValueChangeListener(slave2ListenerMock);

        from(master).to(slave1, slave2);

        // Check exactly one event fired for each slave
        verify(slave1ListenerMock).valueChanged(slave1, "Initial value 1", "New value");
        verify(slave1ListenerMock).valueChanged(any(SimpleStringProperty.class), anyString(), anyString());
        verify(slave2ListenerMock).valueChanged(slave2, "Initial value 2", "New value");
        verify(slave2ListenerMock).valueChanged(any(SimpleStringProperty.class), anyString(), anyString());
    }
}
