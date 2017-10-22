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

import com.github.leanframeworks.propertiesframework.base.injection.annotation.PostInject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// TODO Test no method
// TODO Test one method
// TODO Test several methods
// TODO Test methods from parent class
// TODO Test static/non-static/final/non-final methods
// TODO Test extended methods with and without annotation
// TODO Test method call order
// TODO Test when something/nothing required
// TODO Test with params, without params, with return, without return
// TODO Test call PostInject method immediately when no required field
// TODO Test PostInject method override with PostInject
// TODO Test PostInject method override without PostInject
// TODO Test non-PostInject method override with PostInject
// TODO Test with 'this' as parameter
// TODO Test abstract method
// TODO Test method from interface
// TODO Test default from interface
public class PostInjectAccessibilityTest {

    private static List<String> methodCalls;

    private Injector injector;

    @Before
    public void setUp() {
        injector = new Injector();
        methodCalls = new ArrayList<>();
    }

    @Test
    public void finalModifier() {
        injector.registerInstanceOfClass(FinalMethods.class);

        assertTrue(methodCalls.contains("finalPrivate"));
        assertTrue(methodCalls.contains("finalProtected"));
        assertTrue(methodCalls.contains("finalPublic"));
        assertEquals(3, methodCalls.size());
    }

    @Test
    public void noFinalModifier() {
        injector.registerInstanceOfClass(NonFinalMethods.class);

        assertTrue(methodCalls.contains("private"));
        assertTrue(methodCalls.contains("protected"));
        assertTrue(methodCalls.contains("public"));
        assertEquals(3, methodCalls.size());
    }

    @Test
    public void inheritedMethods() {
        injector.registerInstanceOfClass(ChildMethods.class);

        assertTrue(methodCalls.contains("parentPrivate"));
        assertTrue(methodCalls.contains("childPrivate"));
        assertFalse(methodCalls.contains("parentProtected"));
        assertTrue(methodCalls.contains("childProtected"));
        assertFalse(methodCalls.contains("parentPublic"));
        assertTrue(methodCalls.contains("childPublic"));
        assertEquals(4, methodCalls.size());
    }

//    @Test
//    public void staticModifier() {
//        // Static
//        injector.registerInstanceOfClass(ExposeAccessibilityTest.Static.class);
//
//        assertEquals(1, injector.classToInstances.size());
//        assertEquals(1, injector.idToExposedFields.size());
//        assertTrue(injector.idToExposedFields.containsKey("staticProperty"));
//
//        // Non-static
//        injector.registerInstanceOfClass(ExposeAccessibilityTest.NonStatic.class);
//
//        assertEquals(2, injector.classToInstances.size());
//        assertEquals(2, injector.idToExposedFields.size());
//        assertTrue(injector.idToExposedFields.containsKey("nonStaticProperty"));
//    }
//
//    @Test
//    public void propertiesInParentAndChildClass() {
//        injector.registerInstanceOfClass(ExposeAccessibilityTest.ChildClassWithDifferentID.class);
//
//        assertEquals(1, injector.classToInstances.size());
//        assertEquals(2, injector.idToExposedFields.size());
//        assertTrue(injector.idToExposedFields.containsKey("propertyInParent"));
//        assertTrue(injector.idToExposedFields.containsKey("propertyInChild"));
//    }
//
//    @Test
//    public void registerClassWithoutInstantiation() {
//        try {
//            injector.registerClass(ExposeAccessibilityTest.RegisterClassWithoutInstantiation.class);
//            fail();
//        } catch (DiscoveryException e) {
//            // Success
//        }
//
//        assertEquals(0, injector.classToInstances.size());
//        assertTrue(injector.idToExposedFields.isEmpty());
//    }

    private static class FinalMethods {

        @PostInject
        private final void initPrivate() {
            methodCalls.add("finalPrivate");
        }

        @PostInject
        protected final void initProtected() {
            methodCalls.add("finalProtected");
        }

        @PostInject
        public final void initPublic() {
            methodCalls.add("finalPublic");
        }
    }

    private static class NonFinalMethods {

        @PostInject
        private void initPrivate() {
            methodCalls.add("private");
        }

        @PostInject
        protected void initProtected() {
            methodCalls.add("protected");
        }

        @PostInject
        public void initPublic() {
            methodCalls.add("public");
        }
    }

    private static class ParentMethods {

        @PostInject
        private void initPrivate() {
            methodCalls.add("parentPrivate");
        }

        @PostInject
        protected void initProtected() {
            methodCalls.add("parentProtected");
        }

        @PostInject
        public void initPublic() {
            methodCalls.add("parentPublic");
        }
    }

    private static class ChildMethods extends ParentMethods {

        @PostInject
        private void initPrivate() {
            methodCalls.add("childPrivate");
        }

        @PostInject
        @Override
        protected void initProtected() {
            methodCalls.add("childProtected");
        }

        @PostInject
        @Override
        public void initPublic() {
            methodCalls.add("childPublic");
        }
    }
}
