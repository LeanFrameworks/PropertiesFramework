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
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @see AndBooleanAggregator
 */
public class AndBooleanAggregatorTest {

    @Test
    public void testNonNull() {
        Aggregator<Boolean, Boolean> transformer = new AndBooleanAggregator();

        // All true
        List<Boolean> booleans = new ArrayList<>();
        booleans.add(true);
        booleans.add(true);
        booleans.add(true);

        assertTrue(transformer.transform(booleans));

        // All false
        booleans.clear();
        booleans.add(false);
        booleans.add(false);
        booleans.add(false);

        assertFalse(transformer.transform(booleans));

        // Some true, some false
        booleans.clear();
        booleans.add(true);
        booleans.add(false);
        booleans.add(true);

        assertFalse(transformer.transform(booleans));
    }

    @Test
    public void testNullCollection() {
        Aggregator<Boolean, Boolean> transformer = new AndBooleanAggregator();
        assertTrue(transformer.transform(null));

        transformer = new AndBooleanAggregator(true, AndBooleanAggregator.DEFAULT_NULL_ELEMENT_VALUE);
        assertTrue(transformer.transform(null));

        transformer = new AndBooleanAggregator(Boolean.FALSE, AndBooleanAggregator.DEFAULT_NULL_ELEMENT_VALUE);
        assertFalse(transformer.transform(null));

        transformer = new AndBooleanAggregator(null, AndBooleanAggregator.DEFAULT_NULL_ELEMENT_VALUE);
        assertEquals(null, transformer.transform(null));
    }

    @Test
    public void testNullElement() {
        List<Boolean> booleans = new ArrayList<>();
        booleans.add(true);
        booleans.add(true);
        booleans.add(null);

        Aggregator<Boolean, Boolean> transformer = new AndBooleanAggregator();
        assertFalse(transformer.transform(booleans));

        transformer = new AndBooleanAggregator(AndBooleanAggregator.DEFAULT_EMPTY_COLLECTION_VALUE, Boolean.TRUE);
        assertTrue(transformer.transform(booleans));

        transformer = new AndBooleanAggregator(AndBooleanAggregator.DEFAULT_EMPTY_COLLECTION_VALUE, false);
        assertFalse(transformer.transform(booleans));

        transformer = new OrBooleanAggregator(OrBooleanAggregator.DEFAULT_EMPTY_COLLECTION_VALUE, null);
        assertTrue(transformer.transform(booleans));
    }
}
