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

import java.lang.reflect.Method;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isStatic;

public final class ReflectionUtils {

    /**
     * Private constructor for utility class.
     */
    private ReflectionUtils() {
        // Nothing to be done
    }

    public static boolean overrides(Method parent, Method child) {
        return checkDeclaringClass(parent, child) &&
                checkName(parent, child) &&
                checkStaticModifier(parent, child) &&
                checkPrivateModifier(parent, child) &&
                checkReturnTypes(parent, child) &&
                checkParameterTypes(parent, child);
    }

    /**
     * Checks if child class is a sub-class of parent class.
     * <p>
     * A child class can be assigned to a reference of a parent class. And a class cannot override itself.
     *
     * @param parent Method in parent class.
     * @param child  Method in child class.
     * @return
     */
    private static boolean checkDeclaringClass(Method parent, Method child) {
        return parent.getDeclaringClass().isAssignableFrom(child.getDeclaringClass()) &&
                !parent.getDeclaringClass().equals(child.getDeclaringClass());
    }

    /**
     * Checks if method names are the same.
     *
     * @param parent Method in parent class.
     * @param child  Method in child class.
     * @return
     */
    private static boolean checkName(Method parent, Method child) {
        return child.getName().equals(parent.getName());
    }

    /**
     * Checks whether methods are non-static.
     * <p>
     * Static methods cannot really be overridden.
     *
     * @param parent Method in parent class.
     * @param child  Method in child class.
     * @return
     */
    private static boolean checkStaticModifier(Method parent, Method child) {
        return !isStatic(parent.getModifiers()) && !isPrivate(child.getModifiers());
    }

    /**
     * Checks whether methods are not private.
     * <p>
     * Private methods cannot be overridden.
     *
     * @param parent Method in parent class.
     * @param child  Method in child class.
     * @return
     */
    private static boolean checkPrivateModifier(Method parent, Method child) {
        return !isPrivate(parent.getModifiers()) && !isPrivate(child.getModifiers());
    }

    /**
     * Checks whether return type of child method extends return type of parent method.
     * <p>
     * Method of child class can return a sub-type of the type returned by the parent class.
     *
     * @param parent Method in parent class.
     * @param child  Method in child class.
     * @return
     */
    private static boolean checkReturnTypes(Method parent, Method child) {
        return parent.getReturnType().isAssignableFrom(child.getReturnType());
    }

    /**
     * Checks whether the parameter types are the same.
     *
     * @param parent Method in parent class.
     * @param child  Method in child class.
     * @return
     */
    private static boolean checkParameterTypes(Method parent, Method child) {
        boolean result;

        Class<?>[] parentParams = parent.getParameterTypes();
        Class<?>[] childParams = child.getParameterTypes();

        if (childParams.length == parentParams.length) {
            result = true;
            for (int i = 0; i < childParams.length; i++) {
                if (childParams[i] != parentParams[i]) {
                    result = false;
                    break;
                }
            }
        } else {
            result = false;
        }

        return result;
    }
}
