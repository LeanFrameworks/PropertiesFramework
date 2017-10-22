/*-
 * #%L
 * PropertiesFramework :: Experiments
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

package com.github.leanframeworks.propertiesframework.base.injection.converter;

import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.api.property.ReadableWritableProperty;
import com.github.leanframeworks.propertiesframework.api.property.WritableProperty;
import com.github.leanframeworks.propertiesframework.base.action.RunnableWrapper;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeRO;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeRW;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeWO;
import com.github.leanframeworks.propertiesframework.base.property.wrap.ReadOnlyPropertyWrapper;
import com.github.leanframeworks.propertiesframework.base.property.wrap.ReadWritePropertyWrapper;
import com.github.leanframeworks.propertiesframework.base.property.wrap.WriteOnlyPropertyWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

public class DefaultPropertyConverter extends AbstractConverter {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPropertyConverter.class);

    public DefaultPropertyConverter() {
        supportedExposures.put(ExposeRO.class,
                of(ReadableProperty.class).collect(toSet()));
        supportedExposures.put(ExposeWO.class,
                of(WritableProperty.class).collect(toSet()));
        supportedExposures.put(ExposeRW.class,
                of(ReadableProperty.class, WritableProperty.class).collect(toSet()));

        supportedConversions.put(ReadableProperty.class,
                of(ReadableProperty.class).collect(toSet()));
        supportedConversions.put(WritableProperty.class,
                of(WritableProperty.class).collect(toSet()));
        supportedConversions.put(ReadableWritableProperty.class,
                of(ReadableProperty.class, WritableProperty.class, ReadableWritableProperty.class).collect(toSet()));
    }

    @Override
    public <T> T convert(Object from, Class<T> to) {
        T value;

        // TODO Can we check for the generic type?
        // Most-specific check first
        if (ReadableWritableProperty.class.isAssignableFrom(to)) {
            ReadableWritableProperty valueToBeWrapped = (ReadableWritableProperty) from;
            value = (T) new ReadWritePropertyWrapper(valueToBeWrapped);
        } else if (ReadableProperty.class.isAssignableFrom(to)) {
            ReadableProperty valueToBeWrapped = (ReadableProperty) from;
            value = (T) new ReadOnlyPropertyWrapper(valueToBeWrapped);
        } else if (WritableProperty.class.isAssignableFrom(to)) {
            WritableProperty valueToBeWrapped = (WritableProperty) from;
            value = (T) new WriteOnlyPropertyWrapper(valueToBeWrapped);
        } else if (Runnable.class.isAssignableFrom(to)) {
            Runnable valueToBeWrapped = (Runnable) from;
            value = (T) new RunnableWrapper(valueToBeWrapped);
        } else {
            // TODO Throw exception, log error, log warning or ignore?
            LOGGER.error("Cannot convert value '{}' to class '{}'", from, to);
            value = null;
        }

        return value;
    }
}
