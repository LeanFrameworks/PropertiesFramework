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

import com.github.leanframeworks.propertiesframework.api.common.Disposable;
import com.github.leanframeworks.propertiesframework.base.property.AbstractReadableProperty;
import com.github.leanframeworks.propertiesframework.base.utils.ValueUtils;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Read-only property representing the focused state of a component.
 * <p>
 * This property is not writable because of the difficulty of remaining platform-independent and because focus handling
 * is asynchronous: setting the property value to true would not guarantee that the component will be focused and it
 * would also not be immediate either.
 *
 * @see Component#isFocusOwner()
 */
public class ComponentFocusedProperty extends AbstractReadableProperty<Boolean> implements Disposable {

    /**
     * Focus tracker.
     */
    private class FocusAdapter implements FocusListener {

        /**
         * @see FocusListener#focusGained(FocusEvent)
         */
        @Override
        public void focusGained(FocusEvent e) {
            setValue(true);
        }

        /**
         * @see FocusListener#focusLost(FocusEvent)
         */
        @Override
        public void focusLost(FocusEvent e) {
            setValue(false);
        }
    }

    /**
     * Flag stating whether the component is focused or not.
     */
    private boolean focused = false;

    /**
     * Component whose focus is to be tracked.
     */
    private final Component component;

    /**
     * Focus tracker.
     */
    private final FocusListener focusAdapter = new FocusAdapter();

    /**
     * Constructor specifying the component whose focus is to be tracked.
     *
     * @param component Component whose focus is to be tracked.
     */
    public ComponentFocusedProperty(Component component) {
        this.component = component;
        this.component.addFocusListener(focusAdapter);

        // Set initial value
        focused = component.isFocusOwner();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        component.removeFocusListener(focusAdapter);
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Boolean getValue() {
        return focused;
    }

    /**
     * Sets the value of this property.
     *
     * @param focused True if the component is focused, false otherwise.
     */
    private void setValue(boolean focused) {
        if (!ValueUtils.areEqual(this.focused, focused)) {
            boolean oldValue = this.focused;
            this.focused = focused;
            maybeNotifyListeners(oldValue, focused);
        }
    }
}
