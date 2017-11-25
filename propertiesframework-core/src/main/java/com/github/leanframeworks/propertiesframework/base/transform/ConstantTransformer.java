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

package com.github.leanframeworks.propertiesframework.base.transform;

import com.github.leanframeworks.propertiesframework.api.property.ReadableWritableProperty;
import com.github.leanframeworks.propertiesframework.api.transform.Transformer;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleProperty;

/**
 * Transformer always returning the specific value.
 *
 * @param <O> Type of the output object after transformation.
 */
public class ConstantTransformer<O> implements Transformer<Object, O> {

    /**
     * Property holding the constant output value.
     */
    private final ReadableWritableProperty<O> outputProperty;

    /**
     * Constructor using null as the output constant.
     *
     * @see #getOutputProperty()
     */
    public ConstantTransformer() {
        this(new SimpleProperty<>(null));
    }

    /**
     * Constructor specifying the constant output value.
     *
     * @param output Constant output value.
     * @see #getOutputProperty()
     */
    public ConstantTransformer(O output) {
        this(new SimpleProperty<>(output));
    }

    /**
     * Constructor specifying a property holding the constant output value.
     *
     * @param outputProperty Property holding the constant output value.
     * @see #getOutputProperty()
     */
    public ConstantTransformer(ReadableWritableProperty<O> outputProperty) {
        this.outputProperty = outputProperty;
    }

    /**
     * Gets the property holding the constant output value.
     *
     * @return Property holding the constant output value.
     */
    public ReadableWritableProperty<O> getOutputProperty() {
        return outputProperty;
    }

    /**
     * @see Transformer#transform(Object)
     */
    @Override
    public O transform(Object input) {
        return outputProperty.getValue();
    }
}
