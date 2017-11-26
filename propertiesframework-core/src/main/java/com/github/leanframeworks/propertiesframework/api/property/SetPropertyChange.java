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

package com.github.leanframeworks.propertiesframework.api.property;

import java.util.Collections;
import java.util.Set;

/**
 * Entity representing a change in a set property.
 *
 * @param <R> Type of values in the set property.
 */
public class SetPropertyChange<R> {

    /**
     * Set property that has changed.
     */
    private ReadableSetProperty<? extends R> source;

    /**
     * Old values that have been removed.
     */
    private Set<? extends R> oldValues;

    /**
     * New values that have been added.
     */
    private Set<? extends R> newValues;

    /**
     * Constructor.
     *
     * @param source    Set property that has changed.
     * @param oldValues Old values that have been removed.
     * @param newValues New Values that have been added.
     */
    public SetPropertyChange(ReadableSetProperty<? extends R> source,
                             Set<? extends R> oldValues,
                             Set<? extends R> newValues) {
        this.source = source;
        if (oldValues == null) {
            this.oldValues = Collections.emptySet();
        } else {
            this.oldValues = Collections.unmodifiableSet(oldValues);
        }
        if (newValues == null) {
            this.newValues = Collections.emptySet();
        } else {
            this.newValues = Collections.unmodifiableSet(newValues);
        }
    }

    /**
     * Gets the set property that has changed.
     *
     * @return Set property that has changed.
     */
    public ReadableSetProperty<? extends R> getSource() {
        return source;
    }

    /**
     * Gets the old values that have been removed.
     * <p>
     * If values have only been added, the returned set will be empty. It is guaranteed that the returned value will not
     * be null.
     * <p>
     * At least one of the sets of old and new values will contain at least one value. It is guaranteed that a same
     * value will not be present in both sets.
     *
     * @return Values that have been removed, or empty set in case values have only been added.
     * @see #valuesRemoved()
     * @see #getNewValues()
     */
    public Set<? extends R> getOldValues() {
        return oldValues;
    }

    /**
     * Gets the new values that have been added.
     * <p>
     * If values have only been removed, the returned set will be empty. It is guaranteed that the returned value will
     * not be null.
     * <p>
     * At least one of the sets of old and new values will contain at least one value. It is guaranteed that a same
     * value will not be present in both sets.
     *
     * @return Values that have been added, or empty set in case values have only been removed.
     * @see #valuesAdded()
     * @see #getOldValues()
     */
    public Set<? extends R> getNewValues() {
        return newValues;
    }

    /**
     * States whether this change represents an addition of one or several values.
     *
     * @return True if this change represents an addition of values, false otherwise.
     * @see #getNewValues()
     */
    public boolean valuesAdded() {
        return oldValues.isEmpty();
    }

    /**
     * States whether this change represents a removal of one or several values.
     *
     * @return True if this change represents a removal of values, false otherwise.
     * @see #getOldValues()
     */
    public boolean valuesRemoved() {
        return newValues.isEmpty();
    }
}
