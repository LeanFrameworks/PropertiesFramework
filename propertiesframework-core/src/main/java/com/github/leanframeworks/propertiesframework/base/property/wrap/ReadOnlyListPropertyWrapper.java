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
import com.github.leanframeworks.propertiesframework.api.property.ListPropertyChange;
import com.github.leanframeworks.propertiesframework.api.property.ListPropertyChangeListener;
import com.github.leanframeworks.propertiesframework.api.property.ReadableListProperty;
import com.github.leanframeworks.propertiesframework.base.property.AbstractReadableListProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Wrapper for list properties (typically both readable/writable) to make them appear as read-only.
 * <p>
 * This can be useful, for example, to return a read-only list property in a getter method that is actually a
 * readable/writable list property internally. The wrapper then forbids the programmer to cast the returned list
 * property to a writable list property in order to change its contents.
 *
 * @param <R> Type of data that can be read from the wrapped list property.
 */
public class ReadOnlyListPropertyWrapper<R> extends AbstractReadableListProperty<R> {

    /**
     * Listener to changes on the wrapped property.
     */
    private final ListPropertyChangeListener<? super R> changeAdapter = new ListPropertyChangeForwarder();

    /**
     * Wrapped list property.
     */
    private ReadableListProperty<? extends R> wrappedListProperty;

    /**
     * Constructor specifying the list property to be wrapped, typically a list property that is both readable and
     * writable.
     * <p>
     * The wrapped list property will be disposed whenever this list property is disposed.
     *
     * @param wrappedListProperty List property to be wrapped.
     */
    public ReadOnlyListPropertyWrapper(ReadableListProperty<? extends R> wrappedListProperty) {
        super();
        this.wrappedListProperty = wrappedListProperty;
        this.wrappedListProperty.addChangeListener(changeAdapter);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (wrappedListProperty != null) {
            wrappedListProperty.removeChangeListener(changeAdapter);
            if (wrappedListProperty instanceof Disposable) {
                ((Disposable) wrappedListProperty).dispose();
            }
            wrappedListProperty = null;
        }
    }

    /**
     * @see ReadableListProperty#size()
     */
    @Override
    public int size() {
        int size;
        if (wrappedListProperty == null) {
            size = 0;
        } else {
            size = wrappedListProperty.size();
        }
        return size;
    }

    /**
     * @see ReadableListProperty#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return (wrappedListProperty == null) || wrappedListProperty.isEmpty();
    }

    /**
     * @see ReadableListProperty#get(int)
     */
    @Override
    public R get(int index) {
        R value;
        if (wrappedListProperty == null) {
            value = null;
        } else {
            value = wrappedListProperty.get(index);
        }
        return value;
    }

    /**
     * @see ReadableListProperty#contains(Object)
     */
    @Override
    public boolean contains(Object item) {
        return (wrappedListProperty != null) && wrappedListProperty.contains(item);
    }

    /**
     * @see ReadableListProperty#containsAll(Collection)
     */
    @Override
    public boolean containsAll(Collection<?> items) {
        return (wrappedListProperty != null) && wrappedListProperty.containsAll(items);
    }

    /**
     * @see ReadableListProperty#asUnmodifiableList()
     */
    @Override
    public List<R> asUnmodifiableList() {
        List<R> unmodifiable;
        if (wrappedListProperty == null) {
            unmodifiable = Collections.emptyList();
        } else {
            // Safe to cast
            unmodifiable = (List<R>) wrappedListProperty.asUnmodifiableList();
        }
        return unmodifiable;
    }

    /**
     * @see ReadableListProperty#iterator()
     */
    @Override
    public Iterator<R> iterator() {
        return asUnmodifiableList().iterator();
    }

    /**
     * Entity responsible for forwarding the change events from the wrapped list property to the listeners of the
     * read-only wrapper.
     */
    private class ListPropertyChangeForwarder implements ListPropertyChangeListener<R> {

        /**
         * @see ListPropertyChangeListener#listPropertyChanged(ListPropertyChange)
         */
        @Override
        public void listPropertyChanged(ListPropertyChange<? extends R> e) {
            doNotifyListeners(e);
        }
    }
}
