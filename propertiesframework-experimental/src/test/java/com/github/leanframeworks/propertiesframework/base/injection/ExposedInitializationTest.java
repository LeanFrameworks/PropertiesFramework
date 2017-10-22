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
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeRO;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleBooleanProperty;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleProperty;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

// TODO Test static fields, initialized when declared or in constructor
public class ExposedInitializationTest {

    private Injector injector;

    @Before
    public void setUp() {
        injector = new Injector();
    }

    @Test
    public void momentOfInitialization() {
        // Never initialized
        try {
            injector.registerInstanceOfClass(NeverInitialized.class);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertTrue(injector.classToInstances.isEmpty());
        assertTrue(injector.idToExposedFields.isEmpty());

        // Initialized when declared
        injector.registerInstanceOfClass(InitializedWhenDeclared.class);

        assertEquals(1, injector.classToInstances.size());
        assertEquals(1, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("propertyInitializedWhenDeclared"));

        // Initialized in constructor
        injector.registerInstanceOfClass(InitializedInConstructor.class);

        assertEquals(2, injector.classToInstances.size());
        assertEquals(2, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("propertyInitializedInConstructor"));
    }


    private static class NeverInitialized {

        @ExposeRO("propertyNeverInitialized")
        private final Object property = null;
    }

    private static class InitializedWhenDeclared {

        @ExposeRO("propertyInitializedWhenDeclared")
        private final ReadableProperty<String> property = new SimpleProperty<>();
    }

    private static class InitializedInConstructor {

        @ExposeRO("propertyInitializedInConstructor")
        private final Object property;

        public InitializedInConstructor() {
            property = new SimpleBooleanProperty();
        }
    }
}
