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

// TODO Auto-instantiation or register static
// TODO Register instance (all tests)
public class ExposedIdentificationTest {

    private Injector injector;

    @Before
    public void setUp() {
        injector = new Injector();
    }

    @Test
    public void propertiesWithDifferentIDs() {
        injector.registerInstanceOfClass(PropertiesWithDifferentIDs.class);

        assertEquals(1, injector.classToInstances.size());
        assertEquals(2, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("property1"));
        assertTrue(injector.idToExposedFields.containsKey("property2"));
    }

    @Test
    public void propertiesWithSameIDs() {
        // In same class
        try {
            injector.registerInstanceOfClass(PropertiesWithSameIDs.class);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertTrue(injector.classToInstances.isEmpty());
        assertTrue(injector.idToExposedFields.isEmpty());

        // In same class hierarchy
        try {
            injector.registerInstanceOfClass(ChildClassWithSameID.class);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertTrue(injector.classToInstances.isEmpty());
        assertTrue(injector.idToExposedFields.isEmpty());

        // In different classes
        injector.registerInstanceOfClass(ClassPropertiesWithSameID1.class);

        assertEquals(1, injector.classToInstances.size());
        assertEquals(1, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("property"));

        try {
            injector.registerInstanceOfClass(ClassPropertiesWithSameID2.class);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertEquals(1, injector.classToInstances.size());
        assertEquals(1, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("property"));
    }

    @Test
    public void singlePropertyWithDifferentIDs() {
        injector.registerInstanceOfClass(SinglePropertyWithDifferentIDs.class);

        assertEquals(1, injector.classToInstances.size());
        assertEquals(2, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("property1"));
        assertTrue(injector.idToExposedFields.containsKey("property2"));
    }

    @Test
    public void propertyExposedMultipleTimes() {
        // Same property exposed with same ID
        try {
            injector.registerInstanceOfClass(RepeatedWithSameID.class);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertTrue(injector.classToInstances.isEmpty());
        assertTrue(injector.idToExposedFields.isEmpty());

        // Same property exposed with different IDs
        injector.registerInstanceOfClass(RepeatedWithDifferentIDs.class);

        assertEquals(1, injector.classToInstances.size());
        assertEquals(2, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("property1"));
        assertTrue(injector.idToExposedFields.containsKey("property2"));
    }

    @Test
    public void multipleRegisterOfSameClass() {
        injector.registerInstanceOfClass(ReadablePropertyType.class);

        assertEquals(1, injector.classToInstances.size());
        assertEquals(1, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("property"));

        try {
            injector.registerInstanceOfClass(ReadablePropertyType.class);
            fail();
        } catch (DiscoveryException e) {
            // Success
        }

        assertEquals(1, injector.classToInstances.size());
        assertEquals(1, injector.idToExposedFields.size());
        assertTrue(injector.idToExposedFields.containsKey("property"));
    }

    private static class ReadablePropertyType {

        @ExposeRO("property")
        private final ReadableProperty<Boolean> property = new SimpleBooleanProperty();
    }

    private static class PropertiesWithDifferentIDs {

        @ExposeRO("property1")
        private final ReadableProperty<Boolean> property1 = new SimpleBooleanProperty();

        @ExposeRO("property2")
        private final ReadableProperty<Boolean> property2 = new SimpleBooleanProperty();
    }

    private static class PropertiesWithSameIDs {

        @ExposeRO("property")
        private final ReadableProperty<Boolean> property1 = new SimpleBooleanProperty();

        @ExposeRO("anotherProperty")
        private final ReadableProperty<Boolean> property2 = new SimpleBooleanProperty();

        @ExposeRO("property")
        private final ReadableProperty<Boolean> property3 = new SimpleBooleanProperty();
    }

    private static class ClassPropertiesWithSameID1 {

        @ExposeRO("property")
        private final ReadableProperty<Boolean> property1 = new SimpleBooleanProperty();
    }

    private static class ClassPropertiesWithSameID2 {

        @ExposeRO("property")
        private final ReadableProperty<Boolean> property2 = new SimpleBooleanProperty();
    }

    private static class SinglePropertyWithDifferentIDs {

        @ExposeRO("property1")
        @ExposeRO("property2")
        private final ReadableProperty<Boolean> property = new SimpleBooleanProperty();
    }

    private static class ParentClassWithSameID {

        @ExposeRO("samePropertyInParentAndChild")
        private final ReadableProperty<String> property1 = new SimpleProperty<>();
    }

    private static class ChildClassWithSameID extends ParentClassWithSameID {

        @ExposeRO("samePropertyInParentAndChild")
        private final ReadableProperty<String> property2 = new SimpleProperty<>();
    }

    private static class RepeatedWithSameID {

        @ExposeRO("property")
        @ExposeRO("property")
        private final ReadableProperty<String> property = new SimpleProperty<>();
    }

    private static class RepeatedWithDifferentIDs {

        @ExposeRO("property1")
        @ExposeRO("property2")
        private final ReadableProperty<String> property = new SimpleProperty<>();
    }
}
