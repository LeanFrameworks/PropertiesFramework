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

package com.github.leanframeworks.propertiesframework.base.property.simple;

import com.github.leanframeworks.propertiesframework.api.property.SetPropertyChange;
import com.github.leanframeworks.propertiesframework.api.property.SetPropertyChangeListener;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.github.leanframeworks.propertiesframework.test.TestUtils.haveEqualElements;
import static com.github.leanframeworks.propertiesframework.test.TestUtils.matches;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class SimpleSetPropertyTest {

    @Test
    public void testDefaultConstructor() {
        SimpleSetProperty<Integer> property = new SimpleSetProperty<>();
        SetPropertyChangeListener<Integer> listener = mock(SetPropertyChangeListener.class);
        property.addChangeListener(listener);

        assertTrue(haveEqualElements(property, Collections.emptySet()));
        assertTrue(haveEqualElements(property.asUnmodifiableSet(), Collections.emptySet()));

        verifyZeroInteractions(listener);
    }

    @Test
    public void testConstructorWithItems() {
        Set<Integer> ref = new HashSet<>();
        ref.add(1);
        ref.add(2);
        ref.add(3);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<>(ref);
        SetPropertyChangeListener<Integer> listener = mock(SetPropertyChangeListener.class);
        property.addChangeListener(listener);

        assertTrue(haveEqualElements(ref, property));
        assertTrue(haveEqualElements(ref, property.asUnmodifiableSet()));

        verifyZeroInteractions(listener);
    }

    @Test
    public void testConstructorWithListeners() {
        SetPropertyChangeListener<Integer> listener1 = mock(SetPropertyChangeListener.class);
        SetPropertyChangeListener<Integer> listener2 = mock(SetPropertyChangeListener.class);
        SimpleSetProperty<Integer> property = new SimpleSetProperty<>(listener1, listener2);

        property.add(4);

        verify(listener1).setPropertyChanged(matches(new SetPropertyChange<>(property, null, singleton(4))));
        verify(listener2).setPropertyChanged(matches(new SetPropertyChange<>(property, null, singleton(4))));
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    @Test
    public void testConstructorWithItemsAndListeners() {
        Set<Integer> ref = new HashSet<>();
        ref.add(1);
        ref.add(2);
        ref.add(3);

        SetPropertyChangeListener<Integer> listener1 = mock(SetPropertyChangeListener.class);
        SetPropertyChangeListener<Integer> listener2 = mock(SetPropertyChangeListener.class);
        SimpleSetProperty<Integer> property = new SimpleSetProperty<>(ref, listener1, listener2);

        assertTrue(haveEqualElements(ref, property));
        assertTrue(haveEqualElements(ref, property.asUnmodifiableSet()));

        property.add(4);

        verify(listener1).setPropertyChanged(matches(new SetPropertyChange<>(property, null, singleton(4))));
        verify(listener2).setPropertyChanged(matches(new SetPropertyChange<>(property, null, singleton(4))));
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    @Test
    public void testSize() {
        SimpleSetProperty<Integer> property = new SimpleSetProperty<>();
        assertEquals(0, property.size());

        property.add(1);
        assertEquals(1, property.size());
        property.add(2);
        assertEquals(2, property.size());
        property.add(3);
        assertEquals(3, property.size());

        property.clear();
        assertEquals(0, property.size());
    }

    @Test
    public void testIsEmpty() {
        SimpleSetProperty<Integer> property = new SimpleSetProperty<>();
        assertTrue(property.isEmpty());

        property.add(1);
        assertFalse(property.isEmpty());
        property.add(2);
        assertFalse(property.isEmpty());
        property.add(3);
        assertFalse(property.isEmpty());

        property.clear();
        assertTrue(property.isEmpty());
    }

    @Test
    public void testAdd() {
        SimpleSetProperty<Integer> property = new SimpleSetProperty<>();
        SetPropertyChangeListener<Integer> listener = mock(SetPropertyChangeListener.class);
        property.addChangeListener(listener);

        property.add(1);
        property.add(3);
        property.add(2);
        property.add(3);
        property.add(2);

        Set<Integer> refFirst = singleton(1);
        Set<Integer> refSecond = singleton(3);
        Set<Integer> refThird = singleton(2);
        Set<Integer> refAll = new HashSet<>();
        refAll.add(1);
        refAll.add(2);
        refAll.add(3);

        assertTrue(haveEqualElements(refAll, property));
        verify(listener).setPropertyChanged(matches(new SetPropertyChange<>(property, null, refFirst)));
        verify(listener).setPropertyChanged(matches(new SetPropertyChange<>(property, null, refSecond)));
        verify(listener).setPropertyChanged(matches(new SetPropertyChange<>(property, null, refThird)));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testAddAll() {
        SimpleSetProperty<Integer> property = new SimpleSetProperty<>();
        SetPropertyChangeListener<Integer> listener = mock(SetPropertyChangeListener.class);
        property.addChangeListener(listener);

        Set<Integer> ref = new HashSet<>();
        ref.add(1);
        ref.add(2);
        ref.add(3);

        property.addAll(ref);

        assertEquals(ref.size(), property.size());
        assertTrue(haveEqualElements(ref, property));
        verify(listener).setPropertyChanged(matches(new SetPropertyChange<>(property, null, ref)));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testRemove() {
        Set<Integer> refAll = new HashSet<>();
        refAll.add(1);
        refAll.add(2);
        refAll.add(3);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<>(refAll);
        SetPropertyChangeListener<Integer> listener = mock(SetPropertyChangeListener.class);
        property.addChangeListener(listener);

        property.remove(2);
        property.remove(1);
        property.remove(3);

        assertTrue(property.isEmpty());
        verify(listener).setPropertyChanged(matches(new SetPropertyChange<>(property, singleton(2), null)));
        verify(listener).setPropertyChanged(matches(new SetPropertyChange<>(property, singleton(1), null)));
        verify(listener).setPropertyChanged(matches(new SetPropertyChange<>(property, singleton(3), null)));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testRemoveAll() {
        Set<Integer> refAll = new HashSet<>();
        refAll.add(1);
        refAll.add(2);
        refAll.add(3);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<>(refAll);
        SetPropertyChangeListener<Integer> listener = mock(SetPropertyChangeListener.class);
        property.addChangeListener(listener);

        property.removeAll(refAll);

        assertTrue(property.isEmpty());
        verify(listener).setPropertyChanged(matches(new SetPropertyChange<>(property, refAll, null)));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testRetainAll() {
        Set<Integer> initial = new HashSet<>();
        initial.add(1);
        initial.add(2);
        initial.add(3);
        initial.add(4);
        initial.add(5);
        initial.add(6);
        Set<Integer> retained = new HashSet<>();
        retained.add(1);
        retained.add(2);
        retained.add(3);
        retained.add(11);
        retained.add(12);
        retained.add(13);
        Set<Integer> removed = new HashSet<>();
        removed.add(4);
        removed.add(5);
        removed.add(6);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<>(initial);
        SetPropertyChangeListener<Integer> listener = mock(SetPropertyChangeListener.class);
        property.addChangeListener(listener);

        property.retainAll(retained);

        assertEquals(3, property.size());
        verify(listener).setPropertyChanged(matches(new SetPropertyChange<>(property, removed, null)));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testClear() {
        Set<Integer> refAll = new HashSet<>();
        refAll.add(1);
        refAll.add(2);
        refAll.add(3);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<>(refAll);
        SetPropertyChangeListener<Integer> listener = mock(SetPropertyChangeListener.class);
        property.addChangeListener(listener);

        property.clear();

        assertTrue(property.isEmpty());
        verify(listener).setPropertyChanged(matches(new SetPropertyChange<>(property, refAll, null)));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testContains() {
        SimpleSetProperty<Integer> property = new SimpleSetProperty<>();

        assertFalse(property.contains(1));
        assertFalse(property.contains(2));
        assertFalse(property.contains(3));
        assertFalse(property.contains(4));
        assertFalse(property.contains(5));

        property.add(1);
        property.add(2);
        property.add(3);

        assertTrue(property.contains(1));
        assertTrue(property.contains(2));
        assertTrue(property.contains(3));
        assertFalse(property.contains(4));
        assertFalse(property.contains(5));

        property.clear();

        assertFalse(property.contains(1));
        assertFalse(property.contains(2));
        assertFalse(property.contains(3));
        assertFalse(property.contains(4));
        assertFalse(property.contains(5));
    }

    @Test
    public void testContainsAll() {
        Set<Integer> refSmall = new HashSet<>();
        refSmall.add(1);
        refSmall.add(2);
        refSmall.add(3);

        Set<Integer> refBig = new HashSet<>();
        refBig.add(1);
        refBig.add(2);
        refBig.add(3);
        refBig.add(4);
        refBig.add(5);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<>();
        assertFalse(property.containsAll(refSmall));
        assertFalse(property.containsAll(refBig));

        property.add(1);
        assertFalse(property.containsAll(refSmall));
        assertFalse(property.containsAll(refBig));

        property.add(2);
        assertFalse(property.containsAll(refSmall));
        assertFalse(property.containsAll(refBig));

        property.add(3);
        assertTrue(property.containsAll(refSmall));
        assertFalse(property.containsAll(refBig));

        property.add(4);
        assertTrue(property.containsAll(refSmall));
        assertFalse(property.containsAll(refBig));

        property.add(5);
        assertTrue(property.containsAll(refSmall));
        assertTrue(property.containsAll(refBig));

        property.clear();
        assertFalse(property.containsAll(refSmall));
        assertFalse(property.containsAll(refBig));
    }

    @Test
    public void testAsUnmodifiableSetAndIterator() {
        Set<Integer> ref = new HashSet<>();
        ref.add(1);
        ref.add(2);
        ref.add(3);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<>();
        Set<Integer> unmodifiable = property.asUnmodifiableSet();

        assertTrue(haveEqualElements(property, unmodifiable));
        assertTrue(unmodifiable.isEmpty());

        property.addAll(ref);
        assertTrue(haveEqualElements(property, unmodifiable));
        assertTrue(haveEqualElements(ref, unmodifiable));

        try {
            Iterator<Integer> iterator = property.iterator();
            assertTrue(iterator.hasNext());
            iterator.next();
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.add(4);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        ref.add(5);
        try {
            unmodifiable.addAll(ref);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.retainAll(ref);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.remove(1);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.removeAll(ref);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.clear();
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
    }

    @Test
    public void testToArray() {
        Integer[] ref = new Integer[]{1, 2, 3};

        SimpleSetProperty<Integer> property = new SimpleSetProperty<>();
        property.add(1);
        property.add(2);
        property.add(3);

        assertArrayEquals(ref, property.toArray());
        assertArrayEquals(ref, property.toArray(new Integer[3]));
    }
}
