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

package com.github.leanframeworks.propertiesframework.base.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author arnoud
 */
public class ValueUtilsTest {

    @Test
    public void nullReferencesAreEqualTo() {
        assertTrue(ValueUtils.areEqual(null, null));
    }

    @Test
    public void floatNanAreEqualTo() {
        assertTrue(ValueUtils.areEqual(Float.NaN, Float.NaN));
    }

    @Test
    public void doubleNanAreEqualTo() {
        assertTrue(ValueUtils.areEqual(Double.NaN, Double.NaN));
    }

    @Test
    public void floatNanEqualToDoubleNaN() {
        assertTrue(ValueUtils.areEqual(Double.NaN, Float.NaN));
    }

    @Test
    public void someStringAreEqualTo() {
        final String value1 = "Test";
        final String value2 = "Test";
        final String value3 = "test";

        assertEquals(value1.equals(value2), ValueUtils.areEqual(value1, value2));
        assertEquals(value1.equals(value2), ValueUtils.areEqual(value2, value1));
        assertEquals(value3.equals(value2), ValueUtils.areEqual(value3, value2));
    }
}
