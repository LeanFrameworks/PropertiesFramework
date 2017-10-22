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

package com.github.leanframeworks.propertiesframework.base.injection.impl;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// TODO Test generics
public class ReflectionUtilsTest {

    private static boolean childOverridesParent(String methodName) throws NoSuchMethodException {
        return ReflectionUtils.overrides(
                getMethod(Parent.class, methodName),
                getMethod(Child.class, methodName));
    }

    private static boolean childOverridesInterface(String methodName) throws NoSuchMethodException {
        return ReflectionUtils.overrides(
                getMethod(Interface.class, methodName),
                getMethod(Child.class, methodName));
    }

    private static Method getMethod(Class<?> clazz, String name) {
        Method found = null;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                found = method;
            }
        }

        return found;
    }

    @Test
    public void testStatic() throws NoSuchMethodException {
        assertFalse(childOverridesParent("initPrivateStatic"));
        assertFalse(childOverridesParent("initProtectedStatic"));
        assertFalse(childOverridesParent("initPublicStatic"));
    }

    @Test
    public void testNonStatic() throws NoSuchMethodException {
        assertFalse(childOverridesParent("initPrivate"));
        assertTrue(childOverridesParent("initProtected"));
        assertTrue(childOverridesParent("initPublic"));
    }

    @Test
    public void testReturnTypes() throws NoSuchMethodException {
        assertTrue(childOverridesParent("sameReturnType"));
        assertTrue(childOverridesParent("differentReturnTypes"));
    }

    @Test
    public void testParameters() throws NoSuchMethodException {
        assertTrue(childOverridesParent("noParameter"));
        assertTrue(childOverridesParent("sameParameterTypes"));
        assertFalse(childOverridesParent("differentParameterTypes1"));
        assertFalse(childOverridesParent("differentParameterTypes2"));
        assertFalse(childOverridesParent("differentParameterTypes3"));
        assertTrue(childOverridesParent("varargs"));
    }

    @Test
    public void testAbstract() throws NoSuchMethodException {
        assertTrue(childOverridesParent("abstractMethod"));
    }

    @Test
    public void testInterface() throws NoSuchMethodException {
        assertTrue(childOverridesInterface("nonDefaultInterfaceMethod"));
        assertTrue(childOverridesInterface("defaultInterfaceMethod"));
    }

    private interface Interface {

        void nonDefaultInterfaceMethod();

        default void defaultInterfaceMethod() {
        }
    }

    private static abstract class Parent {

        /////////
        // STATIC
        /////////

        private static void initPrivateStatic() {
        }

        protected static void initProtectedStatic() {
        }

        public static void initPublicStatic() {
        }

        /////////////
        // NON-STATIC
        /////////////

        private void initPrivate() {
        }

        protected void initProtected() {
        }

        public void initPublic() {
        }

        ///////////////
        // RETURN TYPES
        ///////////////

        public Number sameReturnType() {
            return null;
        }

        public Number differentReturnTypes() {
            return null;
        }

        /////////////
        // PARAMETERS
        /////////////

        public void noParameter() {
        }

        public void sameParameterTypes(Number first, String second) {
        }

        public void differentParameterTypes1(Number first, String second) {
        }

        public void differentParameterTypes2(Number first, String second) {
        }

        public void differentParameterTypes3(Number first, String second) {
        }

        public void varargs(Number... varargs) {
        }

        ///////////
        // ABSTRACT
        ///////////

        public abstract void abstractMethod();
    }

    private static class Child extends Parent implements Interface {

        /////////
        // STATIC
        /////////

        private static void initPrivateStatic() {
        }

        protected static void initProtectedStatic() {
        }

        public static void initPublicStatic() {
        }

        /////////////
        // NON-STATIC
        /////////////

        private void initPrivate() {
        }

        @Override
        protected void initProtected() {
        }

        @Override
        public void initPublic() {
        }

        ///////////////
        // RETURN TYPES
        ///////////////

        @Override
        public Number sameReturnType() {
            return null;
        }

        @Override
        public Integer differentReturnTypes() {
            return null;
        }

        /////////////
        // PARAMETERS
        /////////////

        @Override
        public void noParameter() {
        }

        @Override
        public void sameParameterTypes(Number first, String second) {
        }

        public void differentParameterTypes1(Integer first, String second) {
        }

        public void differentParameterTypes2(Object first, String second) {
        }

        public void differentParameterTypes3(String first, String second) {
        }

        @Override
        public void varargs(Number... varargs) {
        }

        ///////////
        // ABSTRACT
        ///////////

        @Override
        public void abstractMethod() {
        }

        ////////////
        // INTERFACE
        ////////////

        @Override
        public void nonDefaultInterfaceMethod() {
        }

        @Override
        public void defaultInterfaceMethod() {
        }
    }
}
