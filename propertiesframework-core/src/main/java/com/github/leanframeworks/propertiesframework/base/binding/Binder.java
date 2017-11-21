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

import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.api.property.WritableProperty;
import com.github.leanframeworks.propertiesframework.api.transform.Transformer;
import com.github.leanframeworks.propertiesframework.base.property.CompositeReadableProperty;
import com.github.leanframeworks.propertiesframework.base.transform.ChainedTransformer;

import java.util.Arrays;
import java.util.Collection;

/**
 * Utility class that can be used to help binding properties and transform their values.
 * <p>
 * This binder utility will create {@link SimpleBinding}s between properties. These bindings can be broken by calling
 * their {@link SimpleBinding#dispose()} method.
 *
 * @see ReadableProperty
 * @see WritableProperty
 * @see SimpleBinding
 */
public final class Binder {

    /**
     * Private constructor for utility class.
     */
    private Binder() {
        // Nothing to be done
    }

    /**
     * Specifies the master property that is part of the binding.
     *
     * @param master Master property.
     * @param <MO>   Type of value to be read from the master property.
     * @return DSL object.
     */
    public static <MO> SingleMasterBinding<MO, MO> from(ReadableProperty<MO> master) {
        return new SingleMasterBinding<>(master, null);
    }

    /**
     * Specifies the master properties that are part of the binding.
     *
     * @param masters Master properties.
     * @param <MO>    Type of value to be read from the master properties.
     * @return DSL object.
     */
    public static <MO> MultipleMasterBinding<MO, Collection<MO>> from(Collection<ReadableProperty<MO>> masters) {
        return new MultipleMasterBinding<>(masters, null);
    }

    /**
     * Specifies the master properties that are part of the binding.
     *
     * @param masters Master properties.
     * @param <MO>    Type of value to be read from the master properties.
     * @return DSL object.
     */
    public static <MO> MultipleMasterBinding<MO, Collection<MO>> from(ReadableProperty<MO>... masters) {
        return new MultipleMasterBinding<>(Arrays.asList(masters), null);
    }

    /**
     * Builder class that is part of the DSL for binding properties.
     *
     * @param <MO> Type of data that can be read from the master property.
     * @param <SI> Type of data that can be written to slave properties.
     */
    public static class SingleMasterBinding<MO, SI> {

        /**
         * Master property.
         */
        private final ReadableProperty<MO> master;

        /**
         * Master property value transformer.
         */
        private final ChainedTransformer<MO, SI> transformer;

        /**
         * Constructor specifying the master property to be bound and the transformer to be applied.
         *
         * @param master      Master property that is part of the binding.
         * @param transformer Transformer to be applied.
         */
        public SingleMasterBinding(ReadableProperty<MO> master, Transformer<MO, SI> transformer) {
            this.master = master;
            this.transformer = new ChainedTransformer<>(transformer);
        }

        /**
         * Specifies a transformer to be used to transform the master property value.
         *
         * @param transformer Transformer to be used by the binding.
         * @param <TSI>       Type of output of the specified transformer.
         * @return Builder object to continue building the binding.
         */
        public <TSI> SingleMasterBinding<MO, TSI> transform(Transformer<? super SI, TSI> transformer) {
            return new SingleMasterBinding<>(master, this.transformer.chain(transformer));
        }

        /**
         * Specifies the slave property that is part of the bind and creates the binding between the master and the
         * slave.
         *
         * @param slave Slave property.
         * @return Binding between the master and the slave.
         */
        public SimpleBinding<MO, SI> to(WritableProperty<SI> slave) {
            return new SimpleBinding<>(master, transformer, slave);
        }

        /**
         * Specifies the slave properties that are part of the bind and creates the binding between the master and the
         * slaves.
         *
         * @param slaves Slave properties.
         * @return Binding between the master and the slaves.
         */
        public SimpleBinding<MO, SI> to(Collection<WritableProperty<? super SI>> slaves) {
            return new SimpleBinding<>(master, transformer, slaves);
        }

        /**
         * Specifies the slave properties that are part of the bind and creates the binding between the master and the
         * slaves.
         *
         * @param slaves Slave properties.
         * @return Binding between the master and the slaves.
         */
        public SimpleBinding<MO, SI> to(WritableProperty<SI>... slaves) {
            return to(Arrays.asList(slaves));
        }
    }

    /**
     * Builder class that is part of the DSL for binding properties.
     *
     * @param <MO> Type of data that can be read from the master properties.
     * @param <SI> Type of data that can be written to slave properties.
     */
    public static class MultipleMasterBinding<MO, SI> {

        /**
         * Master properties.
         */
        private final Collection<ReadableProperty<MO>> masters;

        /**
         * Master properties values transformer.
         */
        private final ChainedTransformer<Collection<MO>, SI> transformer;

        /**
         * Constructor specifying the master properties to be bound and the transformer to be applied.
         *
         * @param masters     Master properties that are part of the binding.
         * @param transformer Transformer to be applied.
         */
        public MultipleMasterBinding(Collection<ReadableProperty<MO>> masters, Transformer<Collection<MO>,
                SI> transformer) {
            this.masters = masters;
            this.transformer = new ChainedTransformer<>(transformer);
        }

        /**
         * Specifies a transformer to be used to transform the collection of master properties values.
         *
         * @param transformer Transformer to be used by the binding.
         * @param <TSI>       Type of output of the specified transformer.
         * @return Builder object to continue building the binding.
         */
        public <TSI> MultipleMasterBinding<MO, TSI> transform(Transformer<? super SI, TSI> transformer) {
            return new MultipleMasterBinding<>(masters, this.transformer.chain(transformer));
        }

        /**
         * Specifies the slave property that is part of the bind and creates the binding between the masters and the
         * slave.
         *
         * @param slave Slave property.
         * @return Binding between the masters and the slave.
         */
        public SimpleBinding<Collection<MO>, SI> to(WritableProperty<? super SI> slave) {
            return new SimpleBinding<>(new CompositeReadableProperty<>(masters), transformer, slave);
        }

        /**
         * Specifies the slave properties that are part of the bind and creates the binding between the masters and the
         * slaves.
         *
         * @param slaves Slave properties.
         * @return Binding between the masters and the slaves.
         */
        public SimpleBinding<Collection<MO>, SI> to(Collection<WritableProperty<? super SI>> slaves) {
            return new SimpleBinding<>(new CompositeReadableProperty<>(masters), transformer, slaves);
        }

        /**
         * Specifies the slave properties that are part of the bind and creates the binding between the masters and the
         * slaves.
         *
         * @param slaves Slave properties.
         * @return Binding between the masters and the slaves.
         */
        public SimpleBinding<Collection<MO>, SI> to(WritableProperty<? super SI>... slaves) {
            return to(Arrays.asList(slaves));
        }
    }
}
