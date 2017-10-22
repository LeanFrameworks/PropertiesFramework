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
import com.github.leanframeworks.propertiesframework.base.injection.annotation.Require;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleBooleanProperty;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleProperty;
import com.github.leanframeworks.propertiesframework.base.injection.impl.RequiredField;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

// TODO Test static fields, initialized when declared or in constructor
// TODO Test field initialized at the moment of injection
public class RequireInitializationTest {

    private Injector injector;

    @Before
    public void setUp() {
        injector = new Injector();
    }

    @Test
    public void discoveryOfFieldsHavingAValue() {
        // Not initialized
        injector.registerInstanceOfClass(NotInitialized.class);

        assertEquals(1, injector.classToInstances.size());
        assertTrue(injector.idToExposedFields.isEmpty());
        assertEquals(1, injector.ownerToRequiredFields.size());
        Collection<RequiredField> required = injector.ownerToRequiredFields.values().iterator().next();
        assertEquals(1, required.size());
        assertTrue(required.stream().allMatch(r -> "notInitialized".equals(r.getId())));

        // Initialized when declared
        try {
            injector.registerInstanceOfClass(InitializedWhenDeclared.class);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertEquals(1, injector.classToInstances.size());
        assertTrue(injector.idToExposedFields.isEmpty());
        assertEquals(1, injector.ownerToRequiredFields.size());
        required = injector.ownerToRequiredFields.values().iterator().next();
        assertEquals(1, required.size());
        assertTrue(required.stream().allMatch(r -> "notInitialized".equals(r.getId())));

        // Initialized in constructor
        try {
            injector.registerInstanceOfClass(InitializedInConstructor.class);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertEquals(1, injector.classToInstances.size());
        assertTrue(injector.idToExposedFields.isEmpty());
        assertEquals(1, injector.ownerToRequiredFields.size());
        required = injector.ownerToRequiredFields.values().iterator().next();
        assertEquals(1, required.size());
        assertTrue(required.stream().allMatch(r -> "notInitialized".equals(r.getId())));
    }


    private static class NotInitialized {

        @Require("notInitialized")
        private ReadableProperty<String> property = null;
    }

    private static class InitializedWhenDeclared {

        @Require("initializedWhenDeclared")
        private ReadableProperty<String> property = new SimpleProperty<>();
    }

    private static class InitializedInConstructor {

        @Require("initializedInConstructor")
        private ReadableProperty<Boolean> property;

        public InitializedInConstructor() {
            property = new SimpleBooleanProperty();
        }
    }
}
