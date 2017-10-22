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

package com.github.leanframeworks.propertiesframework.base.injection;

import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.api.property.ReadableWritableProperty;
import com.github.leanframeworks.propertiesframework.api.property.WritableProperty;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.Require;
import com.github.leanframeworks.propertiesframework.base.injection.impl.RequiredField;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

// Cannot test if field is already annotated with Require because the annotation is not repeatable.
// Cannot test if field is also exposed, because field must be initialized to be exposed, but must not be initialized if
// it is required.
//
// TODO Test final
public class RequireCompatibilityTest {

    private Injector injector;

    @Before
    public void setUp() {
        injector = new Injector();
    }

    @Test
    public void compatibleTypes() {
        injector.registerInstanceOfClass(AllCompatibleTypes.class);

        assertEquals(1, injector.classToInstances.size());
        assertTrue(injector.idToExposedFields.isEmpty());
        assertEquals(1, injector.ownerToRequiredFields.size());
        Collection<RequiredField> required = injector.ownerToRequiredFields.values().iterator().next();
        assertEquals(4, required.size());
        assertTrue(required.stream().allMatch(r -> "property".equals(r.getId())));
    }

    @Test
    public void incompatibleTypes() {
        incompatible(IncompatibleWithPrimitive.class);
        incompatible(IncompatibleWithOther.class);
    }

    private void incompatible(Class<?> classToRegister) {
        try {
            injector.registerInstanceOfClass(classToRegister);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertTrue(injector.classToInstances.isEmpty());
        assertTrue(injector.idToExposedFields.isEmpty());
        assertTrue(injector.ownerToRequiredFields.isEmpty());
    }

    private static class AllCompatibleTypes {

        @Require("property")
        private ReadableProperty<Boolean> property1;

        @Require("property")
        private WritableProperty<Boolean> property2;

        @Require("property")
        private ReadableWritableProperty<Boolean> property3;

        @Require("property")
        private Object property4;
    }

    private static class IncompatibleWithPrimitive {

        @Require("property")
        private boolean property;
    }

    private static class IncompatibleWithOther {

        @Require("property")
        private String property;
    }
}
