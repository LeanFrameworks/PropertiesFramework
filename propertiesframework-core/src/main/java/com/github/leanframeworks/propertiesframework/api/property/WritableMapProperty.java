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

package com.github.leanframeworks.propertiesframework.api.property;

import java.util.Map;

/**
 * Interface to be implemented by writable map properties.
 * <p>
 * Note that most of the methods are based on the {@link java.util.Map} interface.
 *
 * @param <K> Type of keys maintained by this map property.
 * @param <W> Type of values that can be written to this map property.
 */
public interface WritableMapProperty<K, W> {

    /**
     * Puts the specified value at the specified key.
     *
     * @param key   Key to put the value at.
     * @param value Value to be put at the specified key.
     * @return Previous element for the specified key, or null.
     * @see Map#put(Object, Object)
     */
    W put(K key, W value);

    /**
     * Removes the entry at the specified key.
     *
     * @param key Key to be removed.
     * @return Value that was removed, or null.
     * @see Map#remove(Object)
     */
    W remove(Object key);

    /**
     * Puts all the specified entries.
     *
     * @param entries Entries to be put in the map.
     * @see Map#putAll(Map)
     */
    void putAll(Map<? extends K, ? extends W> entries);

    /**
     * Removes all entries from the map.
     *
     * @see Map#clear()
     */
    void clear();
}
