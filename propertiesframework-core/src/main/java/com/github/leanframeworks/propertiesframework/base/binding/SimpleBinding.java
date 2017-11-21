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

package com.github.leanframeworks.propertiesframework.base.binding;

import com.github.leanframeworks.propertiesframework.api.common.Disposable;
import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.api.property.ValueChangeListener;
import com.github.leanframeworks.propertiesframework.api.property.WritableProperty;
import com.github.leanframeworks.propertiesframework.api.transform.Transformer;
import com.github.leanframeworks.propertiesframework.base.property.CompositeWritableProperty;

import java.util.Collection;

/**
 * Simple implementation of a binding between master properties and slave properties.
 * <p>
 * It is typically created using the {@link Binder}.
 *
 * @param <MO> Type of data that can be read from master properties.
 * @param <SI> Type of data that can be written to master properties.
 * @see Binder
 * @see com.github.leanframeworks.propertiesframework.base.property.CompositeReadableProperty
 * @see CompositeWritableProperty
 * @see com.github.leanframeworks.propertiesframework.base.transform.ChainedTransformer
 */
public class SimpleBinding<MO, SI> implements Disposable {

    /**
     * Listener to master property changes and updating the slave property.
     */
    private final MasterAdapter masterAdapter = new MasterAdapter();

    /**
     * Master (possibly composite) that is part of the binding.
     */
    private ReadableProperty<? extends MO> master;

    /**
     * Transformer (possible composite) to be part of the binding.
     */
    private Transformer<? super MO, ? extends SI> transformer;

    /**
     * Slave (possibly composite) that is part of the binding.
     */
    private WritableProperty<? super SI> slave;

    /**
     * Constructor specifying the master property, the transformers and the slaves that are part of the binding.
     * <p>
     * Note that the master property can be a composition of multiple properties, for instance, using the {@link
     * com.github.leanframeworks.propertiesframework.base.property.CompositeReadableProperty}.
     * <p>
     * For type safety, it is highly advised to use the {@link Binder} to create the binding.
     *
     * @param master      Master (possibly composite) property to be part of the binding.
     * @param transformer Transformer (possibly composite) to be part of the binding.
     * @param slave       Slave (possibly composite) property to be part of the binding.
     */
    public SimpleBinding(ReadableProperty<? extends MO> master,
                         Transformer<? super MO, ? extends SI> transformer,
                         WritableProperty<? super SI> slave) {
        init(master, transformer, slave);
    }

    /**
     * Constructor specifying the master property, the transformers and the slaves that are part of the binding.
     * <p>
     * Note that the master property can be a composition of multiple properties, for instance, using the {@link
     * com.github.leanframeworks.propertiesframework.base.property.CompositeReadableProperty}.
     * <p>
     * For type safety, it is highly advised to use the {@link Binder} to create the binding.
     *
     * @param master      Master (possibly composite) property to be part of the binding.
     * @param transformer Transformer (possible composite) to be part of the binding.
     * @param slaves      Slave properties to be part of the binding.
     */
    public SimpleBinding(ReadableProperty<? extends MO> master,
                         Transformer<? super MO, ? extends SI> transformer,
                         Collection<WritableProperty<? super SI>> slaves) {
        // Initialize binding
        CompositeWritableProperty<SI> compositeSlave = new CompositeWritableProperty<>();
        init(master, transformer, compositeSlave);

        // Add slave properties only after initialization, otherwise they will first be set to null
        for (WritableProperty<? super SI> wrappedSlave : slaves) {
            compositeSlave.addProperty(wrappedSlave);
        }
    }

    /**
     * Initializes the binding.
     *
     * @param master      Master (possibly composite) property.
     * @param transformer Value transformer.
     * @param slave       Slave (possibly composite) property.
     */
    private void init(ReadableProperty<? extends MO> master,
                      Transformer<? super MO, ? extends SI> transformer,
                      WritableProperty<? super SI> slave) {
        this.master = master;
        this.transformer = transformer;
        this.slave = slave;

        master.addChangeListener(masterAdapter);

        // Slave initial values
        updateSlaves(master.getValue());
    }

    /**
     * Sets the value of the slaves according the value of the master.
     *
     * @param masterOutputValue Master value.
     */
    private void updateSlaves(MO masterOutputValue) {
        // Transform value
        SI slaveInputValue = transformer.transform(masterOutputValue);

        // Notify slave(s)
        slave.setValue(slaveInputValue);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        master.removeChangeListener(masterAdapter);
    }

    /**
     * Listener to master property changes and updating the slave property.
     */
    private class MasterAdapter implements ValueChangeListener<MO> {

        /**
         * @see ValueChangeListener#valueChanged(ReadableProperty, Object, Object)
         */
        @Override
        public void valueChanged(ReadableProperty<? extends MO> property, MO oldValue, MO newValue) {
            updateSlaves(newValue);
        }
    }
}
