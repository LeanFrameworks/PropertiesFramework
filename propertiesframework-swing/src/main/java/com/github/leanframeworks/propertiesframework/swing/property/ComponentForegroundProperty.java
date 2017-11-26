/*-
 * #%L
 * PropertiesFramework :: Swing Support
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

package com.github.leanframeworks.propertiesframework.swing.property;

import java.awt.Color;
import java.awt.Component;

/**
 * Readable/writable property representing the foreground color of a {@link Component}.
 * <p>
 * It is possible to control the foreground color of the component by setting the value of this property or by calling
 * the {@link Component#setForeground(Color)} method of that component.
 * <p>
 * Finally, note that null values are supported by this property.
 *
 * @see Component#getForeground()
 * @see Component#setForeground(Color)
 * @see Component#isForegroundSet()
 */
public class ComponentForegroundProperty extends AbstractComponentProperty<Component, Color> {

    /**
     * {@inheritDoc}
     */
    public ComponentForegroundProperty(Component component) {
        super(component, "foreground");
    }

    /**
     * @see AbstractComponentProperty#getPropertyValueFromComponent()
     */
    @Override
    protected Color getPropertyValueFromComponent() {
        return component.getForeground();
    }

    /**
     * @see AbstractComponentProperty#setPropertyValueToComponent(Object)
     */
    @Override
    protected void setPropertyValueToComponent(Color value) {
        component.setForeground(value);
    }
}
