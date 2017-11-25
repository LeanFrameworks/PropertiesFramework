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

package com.github.leanframeworks.propertiesframework.base.property;

import com.github.leanframeworks.propertiesframework.api.common.Disposable;
import com.github.leanframeworks.propertiesframework.api.property.WritableProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * {@link WritableProperty} gathering several other writable properties of the same type.
 * <p>
 * Whenever the value of this composite property is set, it will set the same value on all the sub-properties.
 *
 * @param <W> Type of data that can be set on the sub-properties.
 */
public class CompositeWritableProperty<W> implements WritableProperty<W>, Disposable {

    /**
     * Sub-properties.
     */
    private final Collection<WritableProperty<? super W>> properties = new ArrayList<>();

    /**
     * Last value set.
     */
    private W value = null;

    /**
     * Constructor setting the initial value to null.
     */
    public CompositeWritableProperty() {
        super();
    }

    /**
     * Constructor specifying the initial value.
     *
     * @param value Initial value.
     */
    public CompositeWritableProperty(W value) {
        super();
        setValue(value);
    }

    /**
     * Constructor specifying the sub-properties to be added, and setting the initial value to null.
     *
     * @param properties Sub-properties to be added.
     */
    public CompositeWritableProperty(Collection<WritableProperty<? super W>> properties) {
        super();
        this.properties.addAll(properties);
        setValue(value);
    }

    /**
     * Constructor specifying the initial value and the sub-properties to be added.
     *
     * @param value      Initial value.
     * @param properties Sub-properties to be added.
     */
    public CompositeWritableProperty(W value, Collection<WritableProperty<W>> properties) {
        super();
        this.properties.addAll(properties);
        setValue(value);
    }

    /**
     * Constructor specifying the sub-properties to be added.
     *
     * @param properties Sub-properties to be added.
     */
    @SafeVarargs
    public CompositeWritableProperty(WritableProperty<? super W>... properties) {
        super();
        Collections.addAll(this.properties, properties);
        setValue(value);
    }

    /**
     * Constructor specifying the initial value and the sub-properties to be added.
     *
     * @param value      Initial value.
     * @param properties Sub-properties to be added.
     */
    @SafeVarargs
    public CompositeWritableProperty(W value, WritableProperty<? super W>... properties) {
        super();
        Collections.addAll(this.properties, properties);
        setValue(value);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        for (WritableProperty<? super W> property : properties) {
            if (property instanceof Disposable) {
                ((Disposable) property).dispose();
            }
        }
        properties.clear();
    }

    /**
     * Gets all sub-properties.
     *
     * @return Collection containing all sub-properties.
     */
    public Collection<WritableProperty<? super W>> getProperties() {
        return new ArrayList<>(properties);
    }

    /**
     * Adds the specified sub-property.
     *
     * @param property Sub-property to be added.
     */
    public void addProperty(WritableProperty<? super W> property) {
        properties.add(property);
        setValue(value);
    }

    /**
     * Removes the specified sub-property.
     *
     * @param property Sub-property to be removed.
     */
    public void removeProperty(WritableProperty<? super W> property) {
        properties.remove(property);
    }

    /**
     * Removes all sub-properties.
     *
     * @see #removeProperty(WritableProperty)
     */
    public void clear() {
        properties.clear();
    }

    /**
     * @see WritableProperty#setValue(Object)
     */
    @Override
    public void setValue(W value) {
        this.value = value;

        for (WritableProperty<? super W> property : properties) {
            property.setValue(value);
        }
    }
}
