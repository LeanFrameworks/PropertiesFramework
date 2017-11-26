/*-
 * #%L
 * PropertiesFramework :: Core
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

package com.github.leanframeworks.propertiesframework.base.property;

import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.api.property.PropertyChangeListener;

/**
 * Readable property holding a constant value.
 * <p>
 * Note that no listener will ever be notified since the value will not be able to change.
 * <p>
 * Finally note that if the passed value is an object whose internal state can change (e.g. a collection where items can
 * be added/modified/removed), no listeners will notified. The constance in this case is about the reference. Such a use
 * case is obviously not the purpose of this class.
 *
 * @param <R> Type of data that can be read from this property.
 */
public class ConstantProperty<R> implements ReadableProperty<R> {

    /**
     * Constant value.
     */
    private final R value;

    /**
     * Constructor.
     *
     * @param value Constant value.
     */
    public ConstantProperty(R value) {
        this.value = value;
    }

    /**
     * Does nothing because the listener will never be triggered, because this property's value will never change.
     *
     * @see ReadableProperty#addChangeListener(PropertyChangeListener)
     */
    @Override
    public void addChangeListener(PropertyChangeListener<? super R> listener) {
        // Nothing to be done
    }

    /**
     * Does nothing because no listener was actually added.
     *
     * @see ReadableProperty#removeChangeListener(PropertyChangeListener)
     * @see #addChangeListener(PropertyChangeListener)
     */
    @Override
    public void removeChangeListener(PropertyChangeListener<? super R> listener) {
        // Nothing to be done
    }

    /**
     * @see ReadableProperty#getValue()
     */
    @Override
    public R getValue() {
        return value;
    }
}
