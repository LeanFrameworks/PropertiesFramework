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
import com.github.leanframeworks.propertiesframework.base.injection.impl.RequiredField;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RequireAccessibilityTest {

    private Injector injector;

    @Before
    public void setUp() {
        injector = new Injector();
    }

    @Test
    public void finalModifier() {
        // Final
        try {
            injector.registerInstanceOfClass(Final.class);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertTrue(injector.classToInstances.isEmpty());
        assertTrue(injector.idToExposedFields.isEmpty());
        assertTrue(injector.ownerToRequiredFields.isEmpty());

        // Static final
        try {
            injector.registerInstanceOfClass(StaticFinal.class);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertTrue(injector.classToInstances.isEmpty());
        assertTrue(injector.idToExposedFields.isEmpty());
        assertTrue(injector.ownerToRequiredFields.isEmpty());

        // (Non-static) non-final
        injector.registerInstanceOfClass(NonFinal.class);

        assertEquals(1, injector.classToInstances.size());
        assertTrue(injector.idToExposedFields.isEmpty());
        assertEquals(1, injector.ownerToRequiredFields.size());
        Collection<RequiredField> required = injector.ownerToRequiredFields.values().iterator().next();
        assertEquals(1, required.size());
        assertTrue(required.stream().allMatch(r -> "nonFinalProperty".equals(r.getId())));
    }

    @Test
    public void staticModifier() {
        // Static
        injector.registerInstanceOfClass(Static.class);

        assertEquals(1, injector.classToInstances.size());
        assertTrue(injector.idToExposedFields.isEmpty());
        assertEquals(1, injector.ownerToRequiredFields.size());
        Collection<RequiredField> required = injector.ownerToRequiredFields.values().iterator().next();
        assertEquals(1, required.size());
        assertTrue(required.stream().allMatch(r -> "staticProperty".equals(r.getId())));

        // Non-static
        injector.registerInstanceOfClass(NonStatic.class);

        assertEquals(2, injector.classToInstances.size());
        assertTrue(injector.idToExposedFields.isEmpty());
        assertEquals(2, injector.ownerToRequiredFields.size());
        assertTrue(injector.ownerToRequiredFields.values().stream().allMatch(rs -> rs.size() == 1));
        containsRequired("staticProperty");
        containsRequired("nonStaticProperty");
    }

    @Test
    public void propertiesInParentAndChildClass() {
        injector.registerInstanceOfClass(ChildClassWithDifferentID.class);

        assertEquals(1, injector.classToInstances.size());
        assertTrue(injector.idToExposedFields.isEmpty());
        assertEquals(1, injector.ownerToRequiredFields.size());
        assertTrue(injector.ownerToRequiredFields.values().stream().allMatch(rs -> rs.size() == 2));
        assertTrue(containsRequired("propertyInParent"));
        assertTrue(containsRequired("propertyInChild"));
    }

    private boolean containsRequired(String id) {
        return injector.ownerToRequiredFields.values().stream().anyMatch(rs ->
                rs.stream().anyMatch(r ->
                        id.equals(r.getId())
                )
        );
    }

    @Test
    public void registerClassWithoutInstantiation() {
        injector.registerClass(RegisterClassWithoutInstantiation.class);

        assertEquals(0, injector.classToInstances.size());
        assertTrue(injector.idToExposedFields.isEmpty());
        assertEquals(1, injector.ownerToRequiredFields.size());
        Collection<RequiredField> required = injector.ownerToRequiredFields.values().iterator().next();
        assertEquals(1, required.size());
        assertTrue(required.stream().allMatch(r -> "nonFinalProperty".equals(r.getId())));
    }

    private static class Final {

        @Require("finalProperty")
        private final ReadableProperty<String> property = null;
    }

    private static class NonFinal {

        @Require("nonFinalProperty")
        private ReadableProperty<String> property;
    }

    private static class Static {

        @Require("staticProperty")
        private ReadableProperty<String> property;
    }

    private static class NonStatic {

        @Require("nonStaticProperty")
        private ReadableProperty<String> property;
    }

    private static class StaticFinal {

        @Require("staticFinalProperty")
        private static final ReadableProperty<String> PROPERTY = null;
    }

    private static class ParentClassWithDifferentID {

        @Require("propertyInParent")
        private ReadableProperty<String> property1;
    }

    private static class ChildClassWithDifferentID extends ParentClassWithDifferentID {

        @Require("propertyInChild")
        private ReadableProperty<String> property2;
    }

    private static class RegisterClassWithoutInstantiation {

        @Require("nonFinalProperty")
        private static ReadableProperty<String> PROPERTY;
    }
}
