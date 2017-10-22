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
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleProperty;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ExposeAccessibilityTest {

    private Injector injector;

    @Before
    public void setUp() {
        injector = new Injector();
    }

    @Test
    public void finalModifier() {
        // Final
        injector.registerInstanceOfClass(Final.class);

        assertEquals(1, injector.classToInstances.size());
        assertEquals(1, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("finalProperty"));

        // Non-final
        try {
            injector.registerInstanceOfClass(NonFinal.class);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertEquals(1, injector.classToInstances.size());
        assertEquals(1, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("finalProperty"));
        assertTrue(!injector.idToExposedFields.containsKey("nonFinalProperty"));
    }

    @Test
    public void staticModifier() {
        // Static
        injector.registerInstanceOfClass(Static.class);

        assertEquals(1, injector.classToInstances.size());
        assertEquals(1, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("staticProperty"));

        // Non-static
        injector.registerInstanceOfClass(NonStatic.class);

        assertEquals(2, injector.classToInstances.size());
        assertEquals(2, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("nonStaticProperty"));
    }

    @Test
    public void propertiesInParentAndChildClass() {
        injector.registerInstanceOfClass(ChildClassWithDifferentID.class);

        assertEquals(1, injector.classToInstances.size());
        assertEquals(2, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("propertyInParent"));
        assertTrue(injector.idToExposedFields.containsKey("propertyInChild"));
    }

    @Test
    public void registerClassWithoutInstantiation() {
        try {
            injector.registerClass(RegisterClassWithoutInstantiation.class);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertEquals(0, injector.classToInstances.size());
        assertTrue(injector.idToExposedFields.isEmpty());
    }

    private static class Final {

        @ExposeRO("finalProperty")
        private final ReadableProperty<String> property = new SimpleProperty<>();
    }

    private static class NonFinal {

        @ExposeRO("nonFinalProperty")
        private ReadableProperty<String> property = new SimpleProperty<>();
    }

    private static class Static {

        @ExposeRO("staticProperty")
        private final ReadableProperty<String> property = new SimpleProperty<>();
    }

    private static class NonStatic {

        @ExposeRO("nonStaticProperty")
        private final ReadableProperty<String> property = new SimpleProperty<>();
    }

    private static class ParentClassWithDifferentID {

        @ExposeRO("propertyInParent")
        private final ReadableProperty<String> property1 = new SimpleProperty<>();
    }

    private static class ChildClassWithDifferentID extends ParentClassWithDifferentID {

        @ExposeRO("propertyInChild")
        private final ReadableProperty<String> property2 = new SimpleProperty<>();
    }

    private static class RegisterClassWithoutInstantiation {

        @ExposeRO("finalProperty")
        private static final ReadableProperty<String> PROPERTY = new SimpleProperty<>();

        @ExposeRO("nonFinalProperty")
        private static ReadableProperty<String> NON_FINAL_PROPERTY = new SimpleProperty<>();
    }
}
