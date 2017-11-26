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

package com.github.leanframeworks.propertiesframework.base.transform;

import com.github.leanframeworks.propertiesframework.api.transform.Aggregator;

import java.util.Collection;

/**
 * Transforms a collection of boolean values into a single boolean value by aggregating with the boolean OR operator.
 *
 * @see Aggregator
 */
public class OrBooleanAggregator implements Aggregator<Boolean, Boolean> {

    /**
     * Default boolean value to be used when transforming an empty or null collection.
     * <p>
     * This value can be null.
     */
    public static final Boolean DEFAULT_EMPTY_COLLECTION_VALUE = true;

    /**
     * Default boolean value to be used when transforming a null value from the collection.
     * <p>
     * This value can be null.
     */
    public static final Boolean DEFAULT_NULL_ELEMENT_VALUE = false;

    /**
     * Boolean value to be used when transforming an empty collection.
     * <p>
     * This value can be null.
     */
    private final Boolean emptyCollectionValid;

    /**
     * Boolean value to be used when transforming a null value from the collection.
     * <p>
     * If this value is set to null, then the element will be ignored from the aggregation.
     */
    private final Boolean nullElementValid;

    /**
     * Constructor using default boolean values for empty and null collections, and null elements.
     *
     * @see #DEFAULT_EMPTY_COLLECTION_VALUE
     * @see #DEFAULT_NULL_ELEMENT_VALUE
     */
    public OrBooleanAggregator() {
        this(DEFAULT_EMPTY_COLLECTION_VALUE, DEFAULT_NULL_ELEMENT_VALUE);
    }

    /**
     * Constructor specifying the boolean values for empty and null collections, and null elements.
     *
     * @param emptyCollectionValue Value for empty and null collections.
     * @param nullElementValue     Value for null elements in the transformed collection.<br>
     *                             If this value is set to null, then the element will be ignored from the aggregation.
     */
    public OrBooleanAggregator(Boolean emptyCollectionValue, Boolean nullElementValue) {
        this.emptyCollectionValid = emptyCollectionValue;
        this.nullElementValid = nullElementValue;
    }

    /**
     * @see Aggregator#transform(Object)
     */
    @Override
    public Boolean transform(Collection<Boolean> elements) {
        Boolean aggregation = false;

        if ((elements == null) || elements.isEmpty()) {
            aggregation = emptyCollectionValid;
        } else {
            for (Boolean element : elements) {
                Boolean result = element;
                if (result == null) {
                    result = nullElementValid;
                }

                // Ignore if null
                if ((result != null) && result) {
                    aggregation = true;
                    break;
                }
            }
        }

        return aggregation;
    }
}
