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

package com.github.leanframeworks.propertiesframework.base.property.simple;

import com.github.leanframeworks.propertiesframework.api.property.MapPropertyChangeListener;
import com.github.leanframeworks.propertiesframework.base.property.AbstractReadableWritableMapProperty;
import com.github.leanframeworks.propertiesframework.base.utils.ValueUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Readable/writable map property backed by a {@link Map}.
 *
 * @param <K> Type of keys maintained by this map property and the proxied map.
 * @param <V> Type mapped values.
 */
public class SimpleMapProperty<K, V> extends AbstractReadableWritableMapProperty<K, V> implements Map<K, V> {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMapProperty.class);

    /**
     * Proxied map.
     */
    private final Map<K, V> internal = new HashMap<>();

    /**
     * Read-only version of the proxied map.
     */
    private final Map<K, V> unmodifiable = Collections.unmodifiableMap(internal);

    /**
     * Constructor.
     */
    public SimpleMapProperty() {
        super();
    }

    /**
     * Constructor adding the specified listeners.
     *
     * @param listeners Listeners to be added.
     */
    @SafeVarargs
    public SimpleMapProperty(MapPropertyChangeListener<K, V>... listeners) {
        super(listeners);
    }

    /**
     * Constructor specifying the initial entries.
     *
     * @param entries Initial entries.
     */
    public SimpleMapProperty(Map<K, V> entries) {
        super();
        internal.putAll(entries);
    }

    /**
     * Constructor specifying the initial entries and adding the specified listeners.
     * <p>
     * Note that the specified listeners will not be notified for the addition of the specified initial entries.
     *
     * @param entries   Initial entries.
     * @param listeners Listeners to be added.
     */
    @SafeVarargs
    public SimpleMapProperty(Map<K, V> entries, MapPropertyChangeListener<K, V>... listeners) {
        super(); // Without listeners

        internal.putAll(entries);

        for (MapPropertyChangeListener<K, V> listener : listeners) {
            addChangeListener(listener);
        }
    }

    /**
     * @see Map#size()
     */
    @Override
    public int size() {
        return internal.size();
    }

    /**
     * @see Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    /**
     * @see Map#containsKey(Object)
     */
    @Override
    public boolean containsKey(Object key) {
        return internal.containsKey(key);
    }

    /**
     * @see Map#containsValue(Object)
     */
    @Override
    public boolean containsValue(Object value) {
        return internal.containsValue(value);
    }

    /**
     * @see Map#get(Object)
     */
    @Override
    public V get(Object key) {
        return internal.get(key);
    }

    /**
     * @see Map#put(Object, Object)
     */
    @Override
    public V put(K key, V value) {
        boolean alreadyExists = internal.containsKey(key);
        V oldValue = internal.put(key, value);

        if (alreadyExists) {
            // Changed existing entry
            if (!ValueUtils.areEqual(oldValue, value)) {
                Map<K, V> oldValues = new HashMap<>();
                oldValues.put(key, oldValue);
                Map<K, V> newValues = new HashMap<>();
                newValues.put(key, value);
                doNotifyListenersOfChangedValues(oldValues, newValues);
            }
        } else {
            // Added new entry
            Map<K, V> added = new HashMap<>();
            added.put(key, value);
            doNotifyListenersOfAddedValues(added);
        }

        return oldValue;
    }

    /**
     * @see Map#putAll(Map)
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> entries) {
        Map<K, V> newAddedValues = new HashMap<>();
        Map<K, V> changedOldValues = new HashMap<>();
        Map<K, V> changedNewValues = new HashMap<>();

        for (Entry<? extends K, ? extends V> entry : entries.entrySet()) {
            boolean alreadyExists = internal.containsKey(entry.getKey());
            V oldValue = internal.put(entry.getKey(), entry.getValue());

            if (alreadyExists) {
                // Changed existing entry
                if (!ValueUtils.areEqual(oldValue, entry.getValue())) {
                    changedOldValues.put(entry.getKey(), oldValue);
                    changedNewValues.put(entry.getKey(), entry.getValue());
                }
            } else {
                // Added new entry
                newAddedValues.put(entry.getKey(), entry.getValue());
            }
        }

        // Notify the listeners
        if (!newAddedValues.isEmpty()) {
            doNotifyListenersOfAddedValues(newAddedValues);
        }
        if (!changedNewValues.isEmpty()) {
            doNotifyListenersOfChangedValues(changedOldValues, changedNewValues);
        }
    }

    /**
     * @see Map#remove(Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object key) {
        boolean exists = internal.containsKey(key);
        V previousValue = null;

        if (exists) {
            previousValue = internal.remove(key);

            try {
                Map<K, V> removed = new HashMap<>();
                removed.put((K) key, previousValue);
                doNotifyListenersOfRemovedValues(removed);
            } catch (ClassCastException e) {
                // Just in case
                LOGGER.error("Inconsistent type of key: " + key);
            }
        }

        return previousValue;
    }

    /**
     * @see Map#clear()
     */
    @Override
    public void clear() {
        if (!internal.isEmpty()) {
            Map<K, V> removed = new HashMap<>(internal);
            internal.clear();
            doNotifyListenersOfRemovedValues(removed);
        }
    }

    /**
     * Gets a set containing all keys in the read-only version of the proxied map.
     *
     * @return Key set that cannot be used to modify the proxied map.
     * @see Map#keySet()
     */
    @Override
    public Set<K> keySet() {
        return unmodifiable.keySet();
    }

    /**
     * Gets a collection containing all values in the read-only version of the proxied map.
     *
     * @return Value collection that cannot be used to modified the proxied map.
     * @see Map#values()
     */
    @Override
    public Collection<V> values() {
        return unmodifiable.values();
    }

    /**
     * Gets a set containing all entries in the read-only version of the proxied map.
     *
     * @return Entry set that cannot be use to modified the proxied map.
     * @see Map#entrySet()
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return unmodifiable.entrySet();
    }

    /**
     * @see AbstractReadableWritableMapProperty#asUnmodifiableMap()
     */
    @Override
    public Map<K, V> asUnmodifiableMap() {
        return unmodifiable;
    }
}
