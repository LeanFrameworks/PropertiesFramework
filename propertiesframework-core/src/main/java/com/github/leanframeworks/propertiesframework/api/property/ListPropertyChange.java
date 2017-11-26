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
import java.util.List;

/**
 * Entity representing a change in a list property.
 *
 * @param <R> Type of values in the list property.
 */
public class ListPropertyChange<R> {

    /**
     * List property that has changed.
     */
    private ReadableListProperty<? extends R> source;

    /**
     * Start index of the change.
     */
    private int startIndex;

    /**
     * Old values that have been replaced or removed.
     */
    private List<? extends R> oldValues;

    /**
     * New values that have been added or that are replacing the old values.
     */
    private List<? extends R> newValues;

    /**
     * Constructor.
     *
     * @param source     List property that has changed.
     * @param startIndex Start index of the change.
     * @param oldValues  Old values that have been replaced or removed.
     * @param newValues  New Values that have been added or that are replacing the old values.
     */
    public ListPropertyChange(ReadableListProperty<? extends R> source,
                              int startIndex,
                              List<? extends R> oldValues,
                              List<? extends R> newValues) {
        this.source = source;
        this.startIndex = startIndex;
        if (oldValues == null) {
            this.oldValues = Collections.emptyList();
        } else {
            this.oldValues = Collections.unmodifiableList(oldValues);
        }
        if (newValues == null) {
            this.newValues = Collections.emptyList();
        } else {
            this.newValues = Collections.unmodifiableList(newValues);
        }
    }

    /**
     * Gets the list property that has changed.
     *
     * @return List property that has changed.
     */
    public ReadableListProperty<? extends R> getSource() {
        return source;
    }

    /**
     * Gets the index of the first value that has been added, replaced or removed.
     *
     * @return Start index of the change.
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * Gets the old values that have been replaced or removed.
     * <p>
     * If values have only been added, the returned list will be empty. It is guaranteed that the returned value will
     * not be null.
     *
     * @return Values that have been replaced or removed, or empty list in case values have only been added.
     * @see #valuesReplaced()
     * @see #valuesRemoved()
     */
    public List<? extends R> getOldValues() {
        return oldValues;
    }

    /**
     * Gets the new values that have been added or that are replacing the old values.
     * <p>
     * If values have only been removed, the returned list will be empty. It is guaranteed that the returned value will
     * not be null.
     *
     * @return Values that have been added or that are replacing the old values, or empty list in case values have only
     * been removed.
     * @see #valuesAdded()
     * @see #valuesReplaced()
     */
    public List<? extends R> getNewValues() {
        return newValues;
    }

    /**
     * States whether this change represents an addition of one or several values.
     * <p>
     * It is guaranteed that exactly one of the values*() methods will return true. All others will return false.
     *
     * @return True if this change represents an addition of values, false if it is a replacement or a removal.
     * @see #getNewValues()
     */
    public boolean valuesAdded() {
        return oldValues.isEmpty();
    }

    /**
     * States whether this change represents a replacement of one or several values.
     * <p>
     * It is guaranteed that exactly one of the values*() methods will return true. All others will return false.
     *
     * @return True if this change represents a replacement of values, false if it is an addition or a removal.
     * @see #getOldValues()
     * @see #getNewValues()
     */
    public boolean valuesReplaced() {
        return !oldValues.isEmpty() && !newValues.isEmpty();
    }

    /**
     * States whether this change represents a removal of one or several values.
     * <p>
     * It is guaranteed that exactly one of the values*() methods will return true. All others will return false.
     *
     * @return True if this change represents a removal of values, false if it is an addition or a replacement.
     * @see #getOldValues()
     */
    public boolean valuesRemoved() {
        return newValues.isEmpty();
    }
}
