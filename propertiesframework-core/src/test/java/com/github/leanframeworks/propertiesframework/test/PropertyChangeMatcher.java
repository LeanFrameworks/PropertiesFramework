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

package com.github.leanframeworks.propertiesframework.test;

import com.github.leanframeworks.propertiesframework.api.property.PropertyChange;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

import java.util.Collection;

import static com.github.leanframeworks.propertiesframework.base.utils.ValueUtils.areEqual;
import static com.github.leanframeworks.propertiesframework.test.TestUtils.haveEqualElements;

public class PropertyChangeMatcher<T> extends ArgumentMatcher<PropertyChange<T>> {

    private final PropertyChange<T> refEvent;

    private final boolean sameOrderIfCollection;

    public PropertyChangeMatcher(PropertyChange<T> refEvent) {
        this(refEvent, true);
    }

    public PropertyChangeMatcher(PropertyChange<T> refEvent, boolean sameOrderIfCollection) {
        super();
        this.refEvent = refEvent;
        this.sameOrderIfCollection = sameOrderIfCollection;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(Object actualEvent) {
        boolean match = false;

        if (actualEvent instanceof PropertyChange<?>) {
            match = areEqual(refEvent.getSource(), ((PropertyChange<?>) actualEvent).getSource()) &&
                    areValuesOrCollectionsEqual(refEvent.getOldValue(), ((PropertyChange<?>) actualEvent).getOldValue()) &&
                    areValuesOrCollectionsEqual(refEvent.getNewValue(), ((PropertyChange<?>) actualEvent).getNewValue());
        }

        return match;
    }

    private boolean areValuesOrCollectionsEqual(Object value1, Object value2) {
        return areEqual(value1, value2) ||
                ((value1 instanceof Collection<?>) &&
                        (value2 instanceof Collection<?>) &&
                        value1.getClass().equals(value2.getClass()) &&
                        haveEqualElements((Collection<?>) value1, (Collection<?>) value2, sameOrderIfCollection)
                );
    }

    @Override
    public void describeTo(Description description) {
        // Do nothing
    }
}
