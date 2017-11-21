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

package com.github.leanframeworks.propertiesframework.swing.property.wrap;

import com.github.leanframeworks.propertiesframework.api.property.ReadableWritableProperty;
import com.github.leanframeworks.propertiesframework.api.property.ValueChangeListener;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleBooleanProperty;
import com.github.leanframeworks.propertiesframework.base.transform.OrBooleanAggregator;
import org.junit.Test;

import javax.swing.SwingUtilities;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.github.leanframeworks.propertiesframework.base.binding.Binder.from;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @see InvokeLaterPropertyWrapper
 */
public class InvokeLaterPropertyWrapperTest {

    @Test
    public void testNoEventFired() throws InterruptedException {
        final CountDownLatch finished = new CountDownLatch(1);

        SwingUtilities.invokeLater(() -> {
            ReadableWritableProperty<Boolean> rolloverProperty1 = new SimpleBooleanProperty(false);
            ReadableWritableProperty<Boolean> rolloverProperty2 = new SimpleBooleanProperty(false);
            ReadableWritableProperty<Boolean> orRolloverProperty = new SimpleBooleanProperty(false);
            from(rolloverProperty1, rolloverProperty2).transform(new OrBooleanAggregator()).to(orRolloverProperty);

            final ReadableWritableProperty<Boolean> globalRolloverProperty = new SimpleBooleanProperty(false);
            from(new InvokeLaterPropertyWrapper<>(orRolloverProperty)).to(globalRolloverProperty);

            final ValueChangeListener<Boolean> rolloverListener = mock(ValueChangeListener.class);
            globalRolloverProperty.addChangeListener(rolloverListener);

            rolloverProperty1.setValue(true);
            assertTrue(orRolloverProperty.getValue());
            assertFalse(globalRolloverProperty.getValue());
            rolloverProperty1.setValue(false);
            assertFalse(orRolloverProperty.getValue());
            assertFalse(globalRolloverProperty.getValue());
            rolloverProperty2.setValue(true);
            assertTrue(orRolloverProperty.getValue());
            assertFalse(globalRolloverProperty.getValue());
            rolloverProperty2.setValue(false);
            assertFalse(orRolloverProperty.getValue());
            assertFalse(globalRolloverProperty.getValue());

            SwingUtilities.invokeLater(() -> {
                assertFalse(globalRolloverProperty.getValue());
                verifyZeroInteractions(rolloverListener);
                finished.countDown();
            });
        });

        finished.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testEventsFired() throws InterruptedException {
        final CountDownLatch finished = new CountDownLatch(1);

        SwingUtilities.invokeLater(() -> {
            final ReadableWritableProperty<Boolean> rolloverProperty1 = new SimpleBooleanProperty(false);
            final ReadableWritableProperty<Boolean> rolloverProperty2 = new SimpleBooleanProperty(false);
            final ReadableWritableProperty<Boolean> orRolloverProperty = new SimpleBooleanProperty(false);
            from(rolloverProperty1, rolloverProperty2).transform(new OrBooleanAggregator()).to(orRolloverProperty);

            final ReadableWritableProperty<Boolean> globalRolloverProperty = new SimpleBooleanProperty(false);
            from(new InvokeLaterPropertyWrapper<>(orRolloverProperty)).to(globalRolloverProperty);

            final ValueChangeListener<Boolean> rolloverListener = mock(ValueChangeListener.class);
            globalRolloverProperty.addChangeListener(rolloverListener);

            rolloverProperty1.setValue(true);
            assertTrue(orRolloverProperty.getValue());
            assertFalse(globalRolloverProperty.getValue());

            rolloverProperty1.setValue(false);
            assertFalse(orRolloverProperty.getValue());
            assertFalse(globalRolloverProperty.getValue());

            rolloverProperty2.setValue(true);
            assertTrue(orRolloverProperty.getValue());
            assertFalse(globalRolloverProperty.getValue());

            SwingUtilities.invokeLater(() -> {
                assertTrue(globalRolloverProperty.getValue());

                rolloverProperty2.setValue(false);
                assertFalse(orRolloverProperty.getValue());
                assertTrue(globalRolloverProperty.getValue());

                rolloverProperty1.setValue(true);
                assertTrue(orRolloverProperty.getValue());
                assertTrue(globalRolloverProperty.getValue());

                SwingUtilities.invokeLater(() -> {
                    assertTrue(globalRolloverProperty.getValue());

                    rolloverProperty1.setValue(false);
                    assertFalse(orRolloverProperty.getValue());
                    assertTrue(globalRolloverProperty.getValue());

                    SwingUtilities.invokeLater(() -> {
                        assertFalse(globalRolloverProperty.getValue());

                        verify(rolloverListener, times(1))
                                .valueChanged(globalRolloverProperty, false, true);
                        verify(rolloverListener, times(1))
                                .valueChanged(globalRolloverProperty, true, false);
                        verifyNoMoreInteractions(rolloverListener);

                        finished.countDown();
                    });
                });
            });
        });

        finished.await(5, TimeUnit.SECONDS);
    }
}
