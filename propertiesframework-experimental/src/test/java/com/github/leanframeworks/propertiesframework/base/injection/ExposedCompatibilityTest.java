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
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeRO;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeRW;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeWO;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleBooleanProperty;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleProperty;
import com.github.leanframeworks.propertiesframework.base.property.wrap.ReadOnlyPropertyWrapper;
import com.github.leanframeworks.propertiesframework.base.property.wrap.WriteOnlyPropertyWrapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ExposedCompatibilityTest {

    private Injector injector;

    @Before
    public void setUp() {
        injector = new Injector();
    }

    @Test
    public void allCompatibleTypes() {
        injector.registerInstanceOfClass(AllCompatibleTypes.class);

        assertEquals(1, injector.classToInstances.size());
        assertEquals(8, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("property1"));
        assertTrue(injector.idToExposedFields.containsKey("property2"));
        assertTrue(injector.idToExposedFields.containsKey("property3"));
        assertTrue(injector.idToExposedFields.containsKey("property4"));
        assertTrue(injector.idToExposedFields.containsKey("property5"));
        assertTrue(injector.idToExposedFields.containsKey("property6"));
        assertTrue(injector.idToExposedFields.containsKey("property7"));
        assertTrue(injector.idToExposedFields.containsKey("property8"));
        assertTrue(injector.ownerToRequiredFields.isEmpty());
    }

    @Test
    public void incompatibleWithRO() {
        incompatible(ROIncompatibleWithWO.class);
        incompatible(ROIncompatibleWithPrimitive.class);
        incompatible(ROIncompatibleWithOther.class);
    }

    @Test
    public void incompatibleWithWO() {
        incompatible(WOIncompatibleWithRO.class);
        incompatible(WOIncompatibleWithPrimitive.class);
        incompatible(WOIncompatibleWithOther.class);
    }

    @Test
    public void incompatibleWithRW() {
        incompatible(RWIncompatibleWithRO.class);
        incompatible(RWIncompatibleWithWO.class);
        incompatible(RWIncompatibleWithPrimitive.class);
        incompatible(RWIncompatibleWithOther.class);
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

        @ExposeRO("property1")
        private final ReadableProperty<Boolean> property1 = new SimpleProperty<>();

        @ExposeRO("property2")
        private final Object property2 = new SimpleProperty<>();

        @ExposeWO("property3")
        private final WritableProperty<Boolean> property3 = new SimpleProperty<>();

        @ExposeWO("property4")
        private final Object property4 = new SimpleProperty<>();

        @ExposeRW("property5")
        private final ReadableProperty<Boolean> property5 = new SimpleProperty<>();

        @ExposeRW("property6")
        private final WritableProperty<Boolean> property6 = new SimpleProperty<>();

        @ExposeRW("property7")
        private final ReadableWritableProperty<Boolean> property7 = new SimpleProperty<>();

        @ExposeRW("property8")
        private final Object property8 = new SimpleProperty<>();
    }

    private static class ROIncompatibleWithWO {

        @ExposeRO("property")
        private final WritableProperty<Boolean> property = new WriteOnlyPropertyWrapper<>(new SimpleBooleanProperty());
    }

    private static class ROIncompatibleWithPrimitive {

        @ExposeRO("property")
        private final boolean property = false;
    }

    private static class ROIncompatibleWithOther {

        @ExposeRO("property")
        private final Object property = new Object();
    }

    private static class WOIncompatibleWithRO {

        @ExposeWO("property")
        private final ReadableProperty<Boolean> property = new ReadOnlyPropertyWrapper<>(new SimpleBooleanProperty());
    }

    private static class WOIncompatibleWithPrimitive {

        @ExposeWO("property")
        private final boolean property = false;
    }

    private static class WOIncompatibleWithOther {

        @ExposeWO("property")
        private final Object property = new Object();
    }

    private static class RWIncompatibleWithRO {

        @ExposeRW("property")
        private final ReadableProperty<Boolean> property = new ReadOnlyPropertyWrapper<>(new SimpleBooleanProperty());
    }

    private static class RWIncompatibleWithWO {

        @ExposeRW("property")
        private final WritableProperty<Boolean> property = new WriteOnlyPropertyWrapper<>(new SimpleBooleanProperty());
    }

    private static class RWIncompatibleWithPrimitive {

        @ExposeRW("property")
        private final boolean property = false;
    }

    private static class RWIncompatibleWithOther {

        @ExposeRW("property")
        private final Object property = new Object();
    }
}
