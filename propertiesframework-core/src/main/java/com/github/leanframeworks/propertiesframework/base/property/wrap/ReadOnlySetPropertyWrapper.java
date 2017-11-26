/*-
 * #%L
 * PropertiesFramework :: Core
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

package com.github.leanframeworks.propertiesframework.base.property.wrap;

import com.github.leanframeworks.propertiesframework.api.common.Disposable;
import com.github.leanframeworks.propertiesframework.api.property.ReadableSetProperty;
import com.github.leanframeworks.propertiesframework.api.property.SetPropertyChange;
import com.github.leanframeworks.propertiesframework.api.property.SetPropertyChangeListener;
import com.github.leanframeworks.propertiesframework.base.property.AbstractReadableSetProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * Wrapper for set properties (typically both readable/writable) to make them appear as read-only.
 * <p>
 * This can be useful, for example, to return a read-only set property in a getter method that is actually a
 * readable/writable set property internally. The wrapper then forbids the programmer to cast the returned set property
 * to a writable set property in order to change its contents.
 *
 * @param <R> Type of data that can be read from the wrapped set property.
 */
public class ReadOnlySetPropertyWrapper<R> extends AbstractReadableSetProperty<R> {

    /**
     * Listener to changes on the wrapped property.
     */
    private final SetPropertyChangeListener<? super R> changeAdapter = new SetPropertyChangeForwarder();

    /**
     * Wrapped set property.
     */
    private ReadableSetProperty<? extends R> wrappedSetProperty;

    /**
     * Constructor specifying the set property to be wrapped, typically a set property that is both readable and
     * writable.
     * <p>
     * The wrapped set property will be disposed whenever this set property is disposed.
     *
     * @param wrappedSetProperty Set property to be wrapped.
     */
    public ReadOnlySetPropertyWrapper(ReadableSetProperty<? extends R> wrappedSetProperty) {
        super();
        this.wrappedSetProperty = wrappedSetProperty;
        this.wrappedSetProperty.addChangeListener(changeAdapter);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (wrappedSetProperty != null) {
            wrappedSetProperty.removeChangeListener(changeAdapter);
            if (wrappedSetProperty instanceof Disposable) {
                ((Disposable) wrappedSetProperty).dispose();
            }
            wrappedSetProperty = null;
        }
    }

    /**
     * @see ReadableSetProperty#size()
     */
    @Override
    public int size() {
        int size;
        if (wrappedSetProperty == null) {
            size = 0;
        } else {
            size = wrappedSetProperty.size();
        }
        return size;
    }

    /**
     * @see ReadableSetProperty#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return (wrappedSetProperty == null) || wrappedSetProperty.isEmpty();
    }

    /**
     * @see ReadableSetProperty#contains(Object)
     */
    @Override
    public boolean contains(Object item) {
        return (wrappedSetProperty != null) && wrappedSetProperty.contains(item);
    }

    /**
     * @see ReadableSetProperty#containsAll(Collection)
     */
    @Override
    public boolean containsAll(Collection<?> items) {
        return (wrappedSetProperty != null) && wrappedSetProperty.containsAll(items);
    }

    /**
     * @see ReadableSetProperty#asUnmodifiableSet()
     */
    @Override
    public Set<R> asUnmodifiableSet() {
        Set<R> unmodifiable;
        if (wrappedSetProperty == null) {
            unmodifiable = Collections.emptySet();
        } else {
            // Safe to cast
            unmodifiable = (Set<R>) wrappedSetProperty.asUnmodifiableSet();
        }
        return unmodifiable;
    }

    /**
     * @see ReadableSetProperty#iterator()
     */
    @Override
    public Iterator<R> iterator() {
        return asUnmodifiableSet().iterator();
    }

    /**
     * Entity responsible for forwarding the change events from the wrapped set property to the listeners of the
     * read-only wrapper.
     */
    private class SetPropertyChangeForwarder implements SetPropertyChangeListener<R> {

        /**
         * @see SetPropertyChangeListener#setPropertyChanged(SetPropertyChange)
         */
        @Override
        public void setPropertyChanged(SetPropertyChange<? extends R> e) {
            doNotifyListeners(e);
        }
    }
}
