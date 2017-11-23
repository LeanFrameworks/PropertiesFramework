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
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.github.leanframeworks.propertiesframework.test.TestUtils.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @see ComponentFocusedProperty
 */
public class ComponentFocusedPropertyTest {

    private JFrame frame;

    private JButton otherButton;

    private JButton buttonUnderTest;

    private ComponentFocusedProperty propertyUnderTest;

    private PropertyChangeListener<Boolean> listener;

    private CountDownLatch focusLatch;

    @Test
    public void testDispose() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            frame = new JFrame();
            JPanel contentPane = new JPanel(new BorderLayout());

            otherButton = new TestButton("Other button");

            buttonUnderTest = new TestButton("Button under test");
            propertyUnderTest = new ComponentFocusedProperty(buttonUnderTest);
            listener = mock(PropertyChangeListener.class);
            propertyUnderTest.addChangeListener(listener);

            contentPane.add(otherButton, BorderLayout.NORTH);
            contentPane.add(buttonUnderTest, BorderLayout.CENTER);
            frame.setContentPane(contentPane);
            frame.pack();
            frame.setVisible(true);
        });

        focus(buttonUnderTest);
        focus(otherButton);

        SwingUtilities.invokeAndWait(() -> propertyUnderTest.dispose());

        focus(buttonUnderTest);
        focus(otherButton);
        focus(buttonUnderTest);
        focus(otherButton);

        SwingUtilities.invokeAndWait(() -> {
            propertyUnderTest.dispose();
            propertyUnderTest.dispose();
        });

        verify(listener).propertyChanged(matches(new PropertyChange<>(propertyUnderTest, false, true)));
        verify(listener).propertyChanged(matches(new PropertyChange<>(propertyUnderTest, true, false)));
        verifyNoMoreInteractions(listener);

        SwingUtilities.invokeAndWait(() -> frame.dispose());
    }

    private void focus(final JComponent component) throws InvocationTargetException, InterruptedException {
        focusLatch = new CountDownLatch(2);
        SwingUtilities.invokeAndWait(component::requestFocus);
        focusLatch.await(5, TimeUnit.SECONDS);
        focusLatch = null;
    }

    private class TestButton extends JButton {

        public TestButton(String text) {
            super(text);
        }

        @Override
        protected void processFocusEvent(FocusEvent e) {
            super.processFocusEvent(e);
            if (focusLatch != null) {
                focusLatch.countDown();
            }
        }
    }
}
