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

import com.github.leanframeworks.propertiesframework.api.property.PropertyChange;
import com.github.leanframeworks.propertiesframework.api.property.PropertyChangeListener;
import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.base.property.wrap.AbstractReadablePropertyWrapper;

import javax.swing.SwingUtilities;

/**
 * Wrapper for {@link ReadableProperty} that postpones the notifications of the {@link
 * PropertyChangeListener}s later on the EDT.
 * <p>
 * Note that this property wrapper and the wrapped property are meant to be used on the EDT only.
 *
 * @param <R> Type of data that can be read from this property and the wrapped property.
 */
public class InvokeLaterPropertyWrapper<R> extends AbstractReadablePropertyWrapper<R> {

    /**
     * Last value notified.
     * <p>
     * It must be accessed on the EDT.
     */
    private R value = null;

    /**
     * {@inheritDoc}
     */
    public InvokeLaterPropertyWrapper(ReadableProperty<R> wrappedProperty) {
        super(wrappedProperty);
        assert SwingUtilities.isEventDispatchThread();
        value = wrappedProperty.getValue();
    }

    @Override
    public void dispose() {
        assert SwingUtilities.isEventDispatchThread();
        super.dispose();
    }

    /**
     * @see AbstractReadablePropertyWrapper#wrappedPropertyChanged(PropertyChange)
     */
    @Override
    protected void wrappedPropertyChanged(PropertyChange<? extends R> e) {
        assert SwingUtilities.isEventDispatchThread();
        SwingUtilities.invokeLater(() -> {
            if (wrappedProperty != null) {
                R oldValue1 = value;
                value = wrappedProperty.getValue();
                maybeNotifyListeners(oldValue1, value);
            }
        });
    }

    /**
     * @see AbstractReadablePropertyWrapper#getValue()
     */
    @Override
    public R getValue() {
        assert SwingUtilities.isEventDispatchThread();
        return value;
    }
}
