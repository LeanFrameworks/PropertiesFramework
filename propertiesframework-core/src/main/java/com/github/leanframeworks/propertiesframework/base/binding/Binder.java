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
 * This binder utility will create {@link SimpleBond}s between properties. These bonds can be broken by calling their
 * {@link
 * SimpleBond#dispose()} method.
 * <p>
 * If you are using JavaFX, you should better use JavaFX's property binding mechanism. The binding mechanism provided by
 * the PropertiesFramework is very simple and mostly meant for Swing and other frameworks that can benefit from it.
 * JavaFX has a much more furnished API to achieve similar goals and much more.
 *
 * @see ReadableProperty
 * @see WritableProperty
 * @see SimpleBond
 */
public final class Binder {

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
         * @param master      Master property that is part of the bond.
         * @param transformer Transformer to be applied.
         */
        public SingleMasterBinding(ReadableProperty<MO> master, Transformer<MO, SI> transformer) {
            this.master = master;
            this.transformer = new ChainedTransformer<MO, SI>(transformer);
        }

        /**
         * Specifies a transformer to be used to transform the master property value.
         *
         * @param transformer Transformer to be used by the bond.
         * @param <TSI>       Type of output of the specified transformer.
         *
         * @return Builder object to continue building the bond.
         */
        public <TSI> SingleMasterBinding<MO, TSI> transform(Transformer<SI, TSI> transformer) {
            return new SingleMasterBinding<MO, TSI>(master, this.transformer.chain(transformer));
        }

        /**
         * Specifies the slave property that is part of the bind and creates the bond between the master and the slave.
         *
         * @param slave Slave property.
         *
         * @return Bond between the master and the slave.
         */
        public SimpleBond<MO, SI> write(WritableProperty<SI> slave) {
            return new SimpleBond<MO, SI>(master, transformer, slave);
        }

        /**
         * Specifies the slave properties that are part of the bind and creates the bond between the master and the
         * slaves.
         *
         * @param slaves Slave properties.
         *
         * @return Bond between the master and the slaves.
         */
        public SimpleBond<MO, SI> write(Collection<WritableProperty<SI>> slaves) {
            return new SimpleBond<MO, SI>(master, transformer, slaves);
        }

        /**
         * Specifies the slave properties that are part of the bind and creates the bond between the master and the
         * slaves.
         *
         * @param slaves Slave properties.
         *
         * @return Bond between the master and the slaves.
         */
        public SimpleBond<MO, SI> write(WritableProperty<SI>... slaves) {
            return write(Arrays.asList(slaves));
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
         * @param masters     Master properties that are part of the bond.
         * @param transformer Transformer to be applied.
         */
        public MultipleMasterBinding(Collection<ReadableProperty<MO>> masters, Transformer<Collection<MO>,
                SI> transformer) {
            this.masters = masters;
            this.transformer = new ChainedTransformer<Collection<MO>, SI>(transformer);
        }

        /**
         * Specifies a transformer to be used to transform the collection of master properties values.
         *
         * @param transformer Transformer to be used by the bond.
         * @param <TSI>       Type of output of the specified transformer.
         *
         * @return Builder object to continue building the bond.
         */
        public <TSI> MultipleMasterBinding<MO, TSI> transform(Transformer<SI, TSI> transformer) {
            return new MultipleMasterBinding<MO, TSI>(masters, this.transformer.chain(transformer));
        }

        /**
         * Specifies the slave property that is part of the bind and creates the bond between the masters and the slave.
         *
         * @param slave Slave property.
         *
         * @return Bond between the masters and the slave.
         */
        public SimpleBond<Collection<MO>, SI> write(WritableProperty<SI> slave) {
            return new SimpleBond<Collection<MO>, SI>(new CompositeReadableProperty<MO>(masters), transformer, slave);
        }

        /**
         * Specifies the slave properties that are part of the bind and creates the bond between the masters and the
         * slaves.
         *
         * @param slaves Slave properties.
         *
         * @return Bond between the masters and the slaves.
         */
        public SimpleBond<Collection<MO>, SI> write(Collection<WritableProperty<SI>> slaves) {
            return new SimpleBond<Collection<MO>, SI>(new CompositeReadableProperty<MO>(masters), transformer, slaves);
        }

        /**
         * Specifies the slave properties that are part of the bind and creates the bond between the masters and the
         * slaves.
         *
         * @param slaves Slave properties.
         *
         * @return Bond between the masters and the slaves.
         */
        public SimpleBond<Collection<MO>, SI> write(WritableProperty<SI>... slaves) {
            return write(Arrays.asList(slaves));
        }
    }

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
     *
     * @return DSL object.
     */
    public static <MO> SingleMasterBinding<MO, MO> read(ReadableProperty<MO> master) {
        return new SingleMasterBinding<MO, MO>(master, null);
    }

    /**
     * Specifies the master properties that are part of the binding.
     *
     * @param masters Master properties.
     * @param <MO>    Type of value to be read from the master properties.
     *
     * @return DSL object.
     */
    public static <MO> MultipleMasterBinding<MO, Collection<MO>> read(Collection<ReadableProperty<MO>> masters) {
        return new MultipleMasterBinding<MO, Collection<MO>>(masters, null);
    }

    /**
     * Specifies the master properties that are part of the binding.
     *
     * @param masters Master properties.
     * @param <MO>    Type of value to be read from the master properties.
     *
     * @return DSL object.
     */
    public static <MO> MultipleMasterBinding<MO, Collection<MO>> read(ReadableProperty<MO>... masters) {
        return new MultipleMasterBinding<MO, Collection<MO>>(Arrays.asList(masters), null);
    }
}
