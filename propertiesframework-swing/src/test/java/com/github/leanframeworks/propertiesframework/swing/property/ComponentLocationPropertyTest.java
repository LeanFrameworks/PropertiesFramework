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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;

import static com.github.leanframeworks.propertiesframework.test.TestUtils.matches;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @see ComponentLocationProperty
 */
public class ComponentLocationPropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        JFrame window = new JFrame();
        Container contentPane = new JPanel(null);
        window.setContentPane(contentPane);
        Component component = new JLabel();
        contentPane.add(component);

        ReadableWritableProperty<Point> property = new ComponentLocationProperty(component);
        PropertyChangeListener<Point> listenerMock = (PropertyChangeListener<Point>) mock(PropertyChangeListener.class);
        property.addChangeListener(listenerMock);

        assertEquals(new Point(0, 0), property.getValue());
        assertEquals(new Point(0, 0), component.getLocation());
        property.setValue(new Point(11, 12));
        // Wait a little bit because the location may be applied asynchronously
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(new Point(11, 12), component.getLocation());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, new Point(0, 0), new Point(11, 12))));
        verify(listenerMock).propertyChanged(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        JFrame window = new JFrame();
        Container contentPane = new JPanel(null);
        window.setContentPane(contentPane);
        Component component = new JLabel();
        contentPane.add(component);

        ReadableWritableProperty<Point> property = new ComponentLocationProperty(component);
        PropertyChangeListener<Point> listenerMock = (PropertyChangeListener<Point>) mock(PropertyChangeListener.class);
        property.addChangeListener(listenerMock);

        assertEquals(new Point(0, 0), property.getValue());
        assertEquals(new Point(0, 0), component.getLocation());
        component.setLocation(new Point(13, 14));
        // Wait a little bit because the location may be applied asynchronously
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(new Point(13, 14), property.getValue());

        // Check exactly one event fired
        verify(listenerMock).propertyChanged(matches(new PropertyChange<>(property, new Point(0, 0), new Point(13, 14))));
        verify(listenerMock).propertyChanged(any());
    }

    @Test
    public void testDispose() {
        JFrame frame = new JFrame();
        frame.setLocation(0, 0);
        ComponentLocationProperty property = new ComponentLocationProperty(frame);
        PropertyChangeListener<Point> listener = mock(PropertyChangeListener.class);
        property.addChangeListener(listener);

        frame.setLocation(10, 10);
        // Wait a little bit because the location may be applied asynchronously
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        frame.setLocation(20, 20);
        // Wait a little bit because the location may be applied asynchronously
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        property.dispose();

        frame.setLocation(30, 30);
        // Wait a little bit because the location may be applied asynchronously
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        frame.setLocation(40, 40);
        // Wait a little bit because the location may be applied asynchronously
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        property.dispose();
        property.dispose();

        verify(listener).propertyChanged(matches(new PropertyChange<>(property, new Point(0, 0), new Point(10, 10))));
        verify(listener).propertyChanged(matches(new PropertyChange<>(property, new Point(10, 10), new Point(20, 20))));
        verifyNoMoreInteractions(listener);
    }
}
