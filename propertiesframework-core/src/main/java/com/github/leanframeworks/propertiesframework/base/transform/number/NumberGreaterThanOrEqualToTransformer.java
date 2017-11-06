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

package com.github.leanframeworks.propertiesframework.base.transform.number;

import com.github.leanframeworks.propertiesframework.api.transform.Transformer;

/**
 * Transformer checking whether the input data, being a number, is greater than or equal to a specific value.
 * <p>
 * Note that if the input data and the specified value are both null or both NaN, they will be considered equal.
 * Everything is considered bigger than null.
 *
 * @see Transformer
 */
public class NumberGreaterThanOrEqualToTransformer implements Transformer<Number, Boolean> {

    /**
     * Value to which the data is to be compared.
     */
    private Number minimumValue = null;

    /**
     * Constructor.
     */
    public NumberGreaterThanOrEqualToTransformer() {
        // Nothing to be done
    }

    /**
     * Constructor specifying the value to which the data is to be compared.
     *
     * @param minimumValue Value to which the data is to be compared.
     */
    public NumberGreaterThanOrEqualToTransformer(Number minimumValue) {
        setMinimumValue(minimumValue);
    }

    /**
     * Gets the value to which the data is compared.
     *
     * @return Value to which the data is compared.
     */
    public Number getMinimumValue() {
        return minimumValue;
    }

    /**
     * Sets the value to which the data is to be compared.
     *
     * @param minimumValue Value to which the data is to be compared.
     */
    public void setMinimumValue(Number minimumValue) {
        this.minimumValue = minimumValue;
    }

    /**
     * @see Transformer#transform(Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Boolean transform(Number input) {
        boolean valid;

        if ((input == null) && (minimumValue == null)) {
            valid = true;
        } else if (input == null) {
            // exactValue is not null, everything is bigger than null
            valid = false;
        } else if (minimumValue == null) {
            // input is not null, everything is bigger than null
            valid = true;
        } else if (input instanceof Comparable<?>) {
            // Both are not null
            valid = (((Comparable) input).compareTo(minimumValue) >= 0);
        } else if (minimumValue instanceof Comparable<?>) {
            // Both are not null
            valid = (((Comparable) minimumValue).compareTo(input) <= 0);
        } else {
            // Both are not null
            valid = input.equals(minimumValue);
        }

        return valid;
    }
}
