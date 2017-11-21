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

package com.github.leanframeworks.propertiesframework.api.property;

/**
 * Entity representing a change in a property.
 *
 * @param <R> Type of value in the property.
 */
public class PropertyChange<R> {

    /**
     * Property that has changed.
     */
    private ReadableProperty<? extends R> source;

    /**
     * Old value in the property.
     */
    private R oldValue;

    /**
     * New value in the property.
     */
    private R newValue;

    /**
     * Constructor.
     *
     * @param source   Property that has changed.
     * @param oldValue Old value in the property.
     * @param newValue New Value in the property.
     */
    public PropertyChange(ReadableProperty<? extends R> source, R oldValue, R newValue) {
        this.source = source;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Gets the property that has changed.
     *
     * @return Property that has changed.
     */
    public ReadableProperty<? extends R> getSource() {
        return source;
    }

    /**
     * Gets the old value.
     * <p>
     * It is guaranteed that the old value does not equal to the new value.
     *
     * @return Old value.
     * @see #getNewValue()
     */
    public R getOldValue() {
        return oldValue;
    }

    /**
     * Gets the new value.
     * <p>
     * It is guaranteed that the old value does not equal to the new value.
     *
     * @return New value.
     * @see #getOldValue()
     */
    public R getNewValue() {
        return newValue;
    }
}