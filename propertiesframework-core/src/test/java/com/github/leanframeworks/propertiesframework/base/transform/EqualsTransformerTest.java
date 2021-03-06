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

import com.github.leanframeworks.propertiesframework.api.transform.Transformer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @see EqualsTransformer
 */
public class EqualsTransformerTest {

    @Test
    public void testNull() {
        Transformer<Object, Boolean> transformer = new EqualsTransformer(null);
        assertTrue(transformer.transform(null));
        assertFalse(transformer.transform(new Object()));
    }

    @Test
    public void testFloatNaN() {
        Transformer<Object, Boolean> transformer = new EqualsTransformer(Float.NaN);
        assertTrue(transformer.transform(Float.NaN));
        assertFalse(transformer.transform(0.5f));
    }

    @Test
    public void testDoubleNaN() {
        Transformer<Object, Boolean> transformer = new EqualsTransformer(Double.NaN);
        assertTrue(transformer.transform(Double.NaN));
        assertFalse(transformer.transform(0.5));
    }

    @Test
    public void testFloatAndDoubleNaN() {
        Transformer<Object, Boolean> transformer = new EqualsTransformer(Double.NaN);
        assertTrue(transformer.transform(Float.NaN));

        transformer = new EqualsTransformer(Float.NaN);
        assertTrue(transformer.transform(Double.NaN));
    }

    @Test
    public void testNonNullStrings() {
        String ref = "Test";
        String value1 = "Test";
        String value2 = "test";

        Transformer<Object, Boolean> transformer = new EqualsTransformer(ref);
        assertTrue(transformer.transform(value1));
        assertFalse(transformer.transform(value2));
    }
}
