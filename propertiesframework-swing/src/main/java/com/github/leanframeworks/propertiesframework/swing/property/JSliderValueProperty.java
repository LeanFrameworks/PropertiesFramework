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

import com.github.leanframeworks.propertiesframework.api.common.Disposable;
import com.github.leanframeworks.propertiesframework.base.property.AbstractReadableWritableProperty;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Readable/writable property representing the current value bean property of a {@link JSlider}.
 * <p>
 * It is possible to control the current value of the slider by setting the value of this property or by calling the
 * {@link JSlider#setMinimum(int)} method of that slider.
 *
 * @see JSlider#getValue()
 * @see JSlider#setValue(int)
 */
public class JSliderValueProperty extends AbstractReadableWritableProperty<Integer> implements Disposable {

    /**
     * Component to track the value of.
     */
    protected final JSlider slider;

    /**
     * Value tracker.
     */
    private final SliderValueAdapter sliderValueAdapter = new SliderValueAdapter();

    /**
     * Current property value.
     */
    private Integer value = null;

    /**
     * Flag indicating whether the {@link #setValue(Object)} call is due to a property change event.
     */
    private boolean updatingFromComponent = false;

    /**
     * Constructor specifying the component for which the property applies.
     *
     * @param slider Component whose property is to be tracked.
     */
    public JSliderValueProperty(JSlider slider) {
        super();

        // Hook to component
        this.slider = slider;

        // Hook to component
        slider.addChangeListener(sliderValueAdapter);

        // Set initial value
        value = slider.getValue();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        // Unhook from component
        slider.removeChangeListener(sliderValueAdapter);
    }

    /**
     * @see AbstractReadableWritableProperty#getValue()
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(Integer value) {
        if (!isNotifyingListeners()) {
            if (updatingFromComponent) {
                Integer oldValue = this.value;
                this.value = value;
                maybeNotifyListeners(oldValue, this.value);
            } else {
                slider.setValue(value);
            }
        }
    }

    /**
     * Value tracker.
     */
    private class SliderValueAdapter implements ChangeListener {

        /**
         * @see ChangeListener#stateChanged(ChangeEvent)
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            updatingFromComponent = true;
            setValue(slider.getValue());
            updatingFromComponent = false;
        }
    }
}
