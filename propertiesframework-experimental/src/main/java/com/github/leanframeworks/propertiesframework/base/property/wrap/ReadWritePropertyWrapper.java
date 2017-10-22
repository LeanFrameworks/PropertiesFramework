/*-
 * #%L
 * PropertiesFramework :: Experiments
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

package com.github.leanframeworks.propertiesframework.base.property.wrap;

import com.github.leanframeworks.propertiesframework.api.common.Disposable;
import com.github.leanframeworks.propertiesframework.api.property.PropertyChange;
import com.github.leanframeworks.propertiesframework.api.property.PropertyChangeListener;
import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.api.property.ReadableWritableProperty;
import com.github.leanframeworks.propertiesframework.base.property.AbstractReadableProperty;

/**
 * Wrapper for properties (typically both readable/writable) to make them appear as readable/writable but without
 * exposing any API that is not strictly required by the {@link ReadableProperty}, {@link
 * com.github.leanframeworks.propertiesframework.api.property.WritableProperty} and {@link ReadableWritableProperty}
 * interfaces.
 * <p>
 * This can be useful, for example, to return a readable/writable property in a getter method that is actually a
 * sub-class readable/writable property internally with more methods that do not be exposed. The wrapper then forbids
 * the programmer to cast the returned property to the more specific types to control other behavior.
 *
 * @param <V> Type of data that can be read from and written to the wrapped property.
 */
public class ReadWritePropertyWrapper<V> extends AbstractReadableProperty<V>
        implements ReadableWritableProperty<V>, Disposable {

    /**
     * Wrapped property.
     */
    private final ReadableWritableProperty<V> wrappedProperty;

    /**
     * Listener to changes on the wrapped property.
     */
    private final PropertyChangeListener<V> changeAdapter = new ValueChangeForwarder();

    /**
     * Constructor specifying the property to be wrapped, typically a property that is both readable and writable.
     *
     * @param wrappedProperty Property to be wrapped.
     */
    public ReadWritePropertyWrapper(ReadableWritableProperty<V> wrappedProperty) {
        this.wrappedProperty = wrappedProperty;
        this.wrappedProperty.addChangeListener(changeAdapter);
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public V getValue() {
        return wrappedProperty.getValue();
    }

    /**
     * @see ReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(V value) {
        wrappedProperty.setValue(value);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        wrappedProperty.removeChangeListener(changeAdapter);
    }

    /**
     * Entity responsible for forwarding the change events from the wrapped property to the listeners of the read-only
     * wrapper.
     */
    private class ValueChangeForwarder implements PropertyChangeListener<V> {

        /**
         * @see PropertyChangeListener#propertyChanged(PropertyChange)
         */
        @Override
        public void propertyChanged(PropertyChange<? extends V> e) {
            maybeNotifyListeners(e.getOldValue(), e.getNewValue());
        }
    }
}
