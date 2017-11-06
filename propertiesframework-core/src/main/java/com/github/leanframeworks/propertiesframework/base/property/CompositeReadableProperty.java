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
import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.api.property.ValueChangeListener;
import com.github.leanframeworks.propertiesframework.base.utils.ValueUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * {@link ReadableProperty} gathering several other readable properties of the same type.
 * <p>
 * Whenever a sub-property changes, this composite property will trigger its change listeners. Also, reading the value
 * from this property will return the collection of values from all sub-properties.
 * <p>
 * The value returned by {@link #getValue()} will never be null, but it may very well be an empty collection.
 *
 * @param <R> Type of data that can be read from the sub-properties.
 */
public class CompositeReadableProperty<R> extends AbstractReadableProperty<Collection<R>> implements Disposable {

    /**
     * Sub-properties.
     */
    private final Collection<ReadableProperty<? extends R>> properties = new ArrayList<>();

    /**
     * Listener to changes in the sub-properties.
     */
    private final ValueChangeListener<R> changeAdapter = new ValueChangeAdapter();

    /**
     * Collection of current values of the sub-properties.
     */
    private Collection<R> values = Collections.emptyList();

    /**
     * Constructor.
     * <p>
     * The default value of this property will be an empty list. It will not be null. Also, the sub-properties will be
     * disposed whenever this property is disposed.
     */
    public CompositeReadableProperty() {
        super();
    }

    /**
     * Constructor specifying the sub-properties to be added.
     * <p>
     * The sub-properties will be disposed whenever this property is disposed.
     *
     * @param properties Sub-properties to be added.
     */
    public CompositeReadableProperty(Collection<ReadableProperty<R>> properties) {
        super();
        for (ReadableProperty<R> property : properties) {
            addProperty(property);
        }
    }

    /**
     * Constructor specifying the sub-properties to be added.
     * <p>
     * The sub-properties will be disposed whenever this property is disposed.
     *
     * @param properties Sub-properties to be added.
     */
    public CompositeReadableProperty(ReadableProperty<R>... properties) {
        super();
        for (ReadableProperty<R> property : properties) {
            addProperty(property);
        }
    }

    /**
     * @see AbstractReadableProperty#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();

        for (ReadableProperty<? extends R> property : properties) {
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
    public Collection<ReadableProperty<? extends R>> getProperties() {
        return new ArrayList<>(properties);
    }

    /**
     * Adds the specified sub-property.
     * <p>
     * This will trigger the change listeners.
     *
     * @param property Sub-property to be added.
     */
    public void addProperty(ReadableProperty<? extends R> property) {
        property.addValueChangeListener(changeAdapter);
        properties.add(property);
        updateFromProperties();
    }

    /**
     * Removes the specified sub-properties.
     * <p>
     * This will trigger the change listeners.
     *
     * @param property Sub-property to be removed.
     * @see #clear()
     */
    public void removeProperty(ReadableProperty<? extends R> property) {
        property.removeValueChangeListener(changeAdapter);
        properties.remove(property);
        updateFromProperties();
    }

    /**
     * Removes all sub-properties.
     *
     * @see #removeProperty(ReadableProperty)
     */
    public void clear() {
        properties.clear();
        updateFromProperties();
    }

    /**
     * {@inheritDoc}
     *
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Collection<R> getValue() {
        return values;
    }

    /**
     * Sets the new collection of values to be returned by {@link #getValue()}.
     *
     * @param values New collection of values.
     */
    private void setValue(Collection<R> values) {
        Collection<R> oldValues = this.values;
        this.values = values;
        maybeNotifyListeners(oldValues, values);
    }

    /**
     * Updates the current collection of values from the sub-properties and notifies the listeners.
     */
    private void updateFromProperties() {
        // Get value from all properties: use a new collection so that equals() returns false
        List<R> newValues = new ArrayList<>();
        for (ReadableProperty<? extends R> master : properties) {
            newValues.add(master.getValue());
        }

        // Notify slaves
        setValue(newValues);
    }

    /**
     * Listener to changes in the sub-properties.
     */
    private class ValueChangeAdapter implements ValueChangeListener<R> {

        /**
         * {@inheritDoc}
         *
         * @see ValueChangeListener#valueChanged(ReadableProperty, Object, Object)
         */
        @Override
        public void valueChanged(ReadableProperty<? extends R> property, R oldValue, R newValue) {
            if (!ValueUtils.areEqual(oldValue, newValue)) {
                updateFromProperties();
            }
        }
    }
}
