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

package com.github.leanframeworks.propertiesframework.base.property;

import com.github.leanframeworks.propertiesframework.api.common.Disposable;
import com.github.leanframeworks.propertiesframework.api.property.ReadableSetProperty;
import com.github.leanframeworks.propertiesframework.api.property.SetPropertyChange;
import com.github.leanframeworks.propertiesframework.api.property.SetPropertyChangeListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Abstract implementation of a {@link ReadableSetProperty}.
 *
 * @param <R> Type of values that can be read from this set.
 */
public abstract class AbstractReadableSetProperty<R> implements ReadableSetProperty<R>, Disposable {

    /**
     * Listeners to changes in the list property.
     */
    private final List<SetPropertyChangeListener<? super R>> listeners = new ArrayList<>();

    /**
     * Constructor adding no listener.
     */
    public AbstractReadableSetProperty() {
        // Nothing to be done
    }

    /**
     * Constructor adding the specified listeners.
     *
     * @param listeners Listeners to be added.
     */
    @SafeVarargs
    public AbstractReadableSetProperty(SetPropertyChangeListener<? super R>... listeners) {
        for (SetPropertyChangeListener<? super R> listener : listeners) {
            addChangeListener(listener);
        }
    }

    /**
     * Disposes this readable set property by removing any references to any listener.
     * <p>
     * Sub-classes should call the dispose() method of their parent class.
     * <p>
     * Note that the listeners will not be disposed.
     *
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        listeners.clear();
    }

    /**
     * Gets the registered set item change listeners.
     * <p>
     * Note that the returned collection is not modifiable.
     *
     * @return Set item change listeners.
     */
    public Collection<SetPropertyChangeListener<? super R>> getChangeListeners() {
        return Collections.unmodifiableList(listeners);
    }

    /**
     * @see ReadableSetProperty#addChangeListener(SetPropertyChangeListener)
     */
    @Override
    public void addChangeListener(SetPropertyChangeListener<? super R> listener) {
        listeners.add(listener);
    }

    /**
     * @see ReadableSetProperty#removeChangeListener(SetPropertyChangeListener)
     */
    @Override
    public void removeChangeListener(SetPropertyChangeListener<? super R> listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies the change listeners that items have been added.
     *
     * @param newItems Newly added items.
     */
    protected void doNotifyListenersOfAddedValues(Set<? extends R> newItems) {
        doNotifyListeners(new SetPropertyChange<>(this, null, newItems));
    }

    /**
     * Notifies the change listeners that items have been removed.
     *
     * @param oldItems Removed items.
     */
    protected void doNotifyListenersOfRemovedValues(Set<? extends R> oldItems) {
        doNotifyListeners(new SetPropertyChange<>(this, oldItems, null));
    }

    /**
     * Notifies the change listeners that items have been added or removed.
     * <p>
     * Note that the specified sets of items in the event should be wrapped in unmodifiable sets before being passed to
     * the listeners.
     *
     * @param event Event to be passed to the listeners.
     */
    protected void doNotifyListeners(SetPropertyChange<? extends R> event) {
        List<SetPropertyChangeListener<? super R>> listenersCopy = new ArrayList<>(listeners);
        for (SetPropertyChangeListener<? super R> listener : listenersCopy) {
            listener.setPropertyChanged(event);
        }
    }
}
