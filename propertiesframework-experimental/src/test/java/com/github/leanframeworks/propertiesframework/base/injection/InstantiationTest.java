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

import com.github.leanframeworks.propertiesframework.base.injection.support.instantiation.OuterWithDefaultConstr;
import com.github.leanframeworks.propertiesframework.base.injection.support.instantiation.OuterWithNoArgConstr;
import com.github.leanframeworks.propertiesframework.base.injection.support.instantiation.OuterWithPrivateConstr;
import com.github.leanframeworks.propertiesframework.base.injection.support.instantiation.OuterWithPublicConstr;
import com.github.leanframeworks.propertiesframework.base.injection.support.instantiation.OuterWithVarArgConstr;
import com.github.leanframeworks.propertiesframework.base.injection.support.instantiation.OuterWithoutNoArgConstr;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class InstantiationTest {

    private Injector injector;

    @Before
    public void setUp() {
        injector = new Injector();
    }

    @Test
    public void privatePublicStaticNonStaticInner() {
        // Public non static
        try {
            injector.registerInstanceOfClass(PublicInner.class);
            fail("Expected NoSuchMethodException to be thrown");
        } catch (InstantiationException e) {
            // Success
        }

        // Public static
        injector.registerInstanceOfClass(PublicStaticInner.class);

        // Private non-static
        try {
            injector.registerInstanceOfClass(PrivateInner.class);
            fail("Expected InstantiationException to be thrown");
        } catch (InstantiationException e) {
            // Success
        }

        // Private static
        injector.registerInstanceOfClass(PrivateStaticInner.class);
    }

    @Test
    public void innerWithDifferentConstructors() {
        // Default constructor
        injector.registerInstanceOfClass(InnerWithDefaultConstr.class);

        // Constructor with no argument
        injector.registerInstanceOfClass(InnerWithNoArgConstr.class);

        // Constructor with varargs
        try {
            injector.registerInstanceOfClass(InnerWithVarArgConstr.class);
            fail("Expected InstantiationException to be thrown");
        } catch (InstantiationException e) {
            // Success
        }

        // Constructor with argument
        try {
            injector.registerInstanceOfClass(InnerWithoutNoArgConstr.class);
            fail("Expected InstantiationException to be thrown");
        } catch (InstantiationException e) {
            // Success
        }

        // Public constructor
        injector.registerInstanceOfClass(InnerWithPublicConstr.class);
        injector.registerInstanceOfClass(OuterWithPublicConstr.class);

        // Private constructor
        injector.registerInstanceOfClass(InnerWithPrivateConstr.class);
        injector.registerInstanceOfClass(OuterWithPrivateConstr.class);
    }

    @Test
    public void testOuterClassInstantiation() {
        // Different constructors
        injector.registerInstanceOfClass(OuterWithDefaultConstr.class);
        injector.registerInstanceOfClass(OuterWithNoArgConstr.class);
        try {
            injector.registerInstanceOfClass(OuterWithVarArgConstr.class);
            fail("Expected InstantiationException to be thrown");
        } catch (InstantiationException e) {
            // Success
        }
        try {
            injector.registerInstanceOfClass(OuterWithoutNoArgConstr.class);
            fail("Expected InstantiationException to be thrown");
        } catch (InstantiationException e) {
            // Success
        }
    }

    public static class PublicStaticInner {
        // Nothing
    }

    private static class PrivateStaticInner {
        // Nothing
    }

    public static class InnerWithDefaultConstr {
        // Nothing
    }

    public static class InnerWithNoArgConstr {

        public InnerWithNoArgConstr() {
            // Nothing
        }
    }

    public static class InnerWithVarArgConstr {

        public InnerWithVarArgConstr(String... something) {
            // Nothing
        }
    }

    public static class InnerWithoutNoArgConstr {

        public InnerWithoutNoArgConstr(String something) {
            // Nothing
        }
    }

    public static class InnerWithPublicConstr {

        public InnerWithPublicConstr() {
            // Nothing
        }
    }

    public static class InnerWithPrivateConstr {

        private InnerWithPrivateConstr() {
            // Nothing
        }
    }

    public class PublicInner {
        // Nothing
    }

    private class PrivateInner {
        // Nothing
    }
}
