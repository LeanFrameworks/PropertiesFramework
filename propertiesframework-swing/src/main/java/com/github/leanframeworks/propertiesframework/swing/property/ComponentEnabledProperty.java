/*
 * Copyright (c) 2017, LeanFrameworks
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.leanframeworks.propertiesframework.swing.property;

import java.awt.Component;

/**
 * Readable/writable property representing the enabled state of a {@link Component}.
 * <p>
 * It is possible to control the enabled state of the component by setting the value of this property or by calling the
 * {@link Component#setEnabled(boolean)} method of that component.
 * <p>
 * Please note that a plain {@link Component} does not a fire property change event when it gets enabled/disabled. A
 * Swing {@link javax.swing.JComponent} does. However, the type {@link Component} is used by this property instead of
 * {@link javax.swing.JComponent} for convenience.
 * <p>
 * Finally note that null values are not supported by this property.
 *
 * @see Component#isEnabled()
 * @see Component#setEnabled(boolean)
 */
public class ComponentEnabledProperty extends AbstractComponentProperty<Component, Boolean> {

    /**
     * @see AbstractComponentProperty#AbstractComponentProperty(java.awt.Component, String)
     */
    public ComponentEnabledProperty(Component component) {
        super(component, "enabled");
    }

    /**
     * @see AbstractComponentProperty#getPropertyValueFromComponent()
     */
    @Override
    protected Boolean getPropertyValueFromComponent() {
        return component.isEnabled();
    }

    /**
     * @see AbstractComponentProperty#setPropertyValueToComponent(Object)
     */
    @Override
    protected void setPropertyValueToComponent(Boolean value) {
        component.setEnabled(value);
    }
}
