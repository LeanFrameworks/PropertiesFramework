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

package com.github.leanframeworks.propertiesframework.base.property.wrap;

import com.github.leanframeworks.propertiesframework.api.common.Disposable;
import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.api.property.ValueChangeListener;
import com.github.leanframeworks.propertiesframework.base.property.AbstractReadableProperty;

/**
 * Abstract implementation of a wrapper for a readable property.
 */
public abstract class AbstractReadablePropertyWrapper<R> extends AbstractReadableProperty<R> implements Disposable {

    /**
     * Listener to the wrapped property changes.
     *
     * @see #wrappedPropertyValueChanged(ReadableProperty, Object, Object)
     */
    protected final ValueChangeListener<? super R> wrappedPropertyAdapter;

    /**
     * Wrapped property.
     */
    protected ReadableProperty<? extends R> wrappedProperty;

    /**
     * Constructor specifying the property to be wrapped.
     * <p>
     * The wrapped property will be disposed whenever this property is disposed.
     *
     * @param wrappedProperty Property to be wrapped.
     */
    public AbstractReadablePropertyWrapper(ReadableProperty<? extends R> wrappedProperty) {
        super();
        this.wrappedProperty = wrappedProperty;
        this.wrappedPropertyAdapter = new ValueChangeAdapter();
        this.wrappedProperty.addChangeListener(wrappedPropertyAdapter);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (wrappedProperty != null) {
            wrappedProperty.removeChangeListener(wrappedPropertyAdapter);
            if (wrappedProperty instanceof Disposable) {
                ((Disposable) wrappedProperty).dispose();
            }
            wrappedProperty = null;
        }
    }

    /**
     * Called whenever the wrapper property value changed.
     * <p>
     * The concrete implementations should then process this change, for instance, by transforming the values, filtering
     * the changes before notifying the listeners, etc.
     *
     * @param property Wrapper property value.
     * @param oldValue Old value.
     * @param newValue New value.
     */
    protected abstract void wrappedPropertyValueChanged(ReadableProperty<? extends R> property, R oldValue, R newValue);

    /**
     * Listener to changes of the value of the wrapped property.
     */
    private class ValueChangeAdapter implements ValueChangeListener<R> {

        /**
         * @see ValueChangeListener#valueChanged(ReadableProperty, Object, Object)
         */
        @Override
        public void valueChanged(ReadableProperty<? extends R> property, R oldValue, R newValue) {
            wrappedPropertyValueChanged(property, oldValue, newValue);
        }
    }
}
