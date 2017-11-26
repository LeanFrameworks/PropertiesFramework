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

package com.github.leanframeworks.propertiesframework.base.common;

import com.github.leanframeworks.propertiesframework.api.common.Disposable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Collection of {@link Disposable}s that can all be disposed at once.
 * <p>
 * Finally, note that the order of disposal is not specified.
 */
public class CompositeDisposable implements Disposable {

    /**
     * {@link Disposable}s to be disposed.
     */
    private final Set<Disposable> disposables = new HashSet<>();

    /**
     * Constructor.
     *
     * @param disposables {@link Disposable}s to be disposed.
     */
    public CompositeDisposable(Disposable... disposables) {
        if (disposables != null) {
            Collections.addAll(this.disposables, disposables);
        }
    }

    /**
     * Constructor.
     *
     * @param disposables {@link Disposable}s to be disposed.
     */
    public CompositeDisposable(Collection<? extends Disposable> disposables) {
        if (disposables != null) {
            this.disposables.addAll(disposables);
        }
    }

    /**
     * Adds the specified {@link Disposable}.
     *
     * @param disposable {@link Disposable} to be added.
     */
    public void addDisposable(Disposable disposable) {
        if (disposable != null) {
            disposables.add(disposable);
        }
    }

    /**
     * Removes the specified {@link Disposable}.
     *
     * @param disposable {@link Disposable} to be removed.
     */
    public void removeDisposable(Disposable disposable) {
        if (disposable != null) {
            disposables.remove(disposable);
        }
    }

    /**
     * Removes all {@link Disposable}s without disposing them.
     */
    public void clear() {
        disposables.clear();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        disposables.clear();
    }
}
