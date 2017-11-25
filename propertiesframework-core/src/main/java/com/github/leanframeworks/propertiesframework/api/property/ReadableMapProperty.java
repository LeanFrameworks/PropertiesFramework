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

package com.github.leanframeworks.propertiesframework.api.property;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Interface to be implemented by readable map properties.
 * <p>
 * Note that most of the methods are based on the {@link java.util.Map} interface.
 *
 * @param <K> Type of keys maintained by this map property.
 * @param <R> Type of values that can be read from this map property.
 */
public interface ReadableMapProperty<K, R> {

    /**
     * Adds the specified map value change listener.
     *
     * @param listener Listener to be added.
     */
    void addChangeListener(MapPropertyChangeListener<? super K, ? super R> listener);

    /**
     * Removes the specified map value change listener.
     *
     * @param listener Listener to be removed.
     */
    void removeChangeListener(MapPropertyChangeListener<? super K, ? super R> listener);

    /**
     * Gets the size of the map.
     *
     * @return Map size.
     * @see Map#size()
     */
    int size();

    /**
     * States whether the map is empty or not.
     *
     * @return True if the map is empty, false otherwise.
     * @see Map#isEmpty()
     */
    boolean isEmpty();

    /**
     * States whether the map contains the specified key or not.
     *
     * @param key Key whose presence is to be checked.
     * @return True if the map contains the key, false otherwise.
     * @see Map#containsKey(Object)
     */
    boolean containsKey(Object key);

    /**
     * States whether the map contains the specified value or not.
     *
     * @param value Value whose presence is to be checked.
     * @return True if the map contains the key, false otherwise.
     * @see Map#containsValue(Object)
     */
    boolean containsValue(Object value);

    /**
     * Gets the value for the specified key.
     *
     * @param key Key to get the value for.
     * @return Value for the specified key.
     * @see Map#get(Object)
     */
    R get(Object key);

    /**
     * Gets the keys of the map.
     *
     * @return Map keys.
     * @see Map#keySet()
     */
    Set<K> keySet();

    /**
     * Gets the values of the map.
     *
     * @return Map values.
     * @see Map#values()
     */
    Collection<R> values();

    /**
     * Gets the entries of the map.
     *
     * @return Map entries.
     * @see Map#entrySet()
     */
    Set<Map.Entry<K, R>> entrySet();

    /**
     * Returns a {@link Map} containing the same elements as this map property, and that cannot be modified directly.
     * <p>
     * Unless specified otherwise in the implementing classes, the returned map always represents the contains of this
     * map property, meaning that if items are added/updated/removed from this map property, the contents of the
     * unmodifiable map will also change.
     *
     * @return Unmodifiable map containing the same elements as this map property.
     */
    Map<K, R> asUnmodifiableMap();
}
