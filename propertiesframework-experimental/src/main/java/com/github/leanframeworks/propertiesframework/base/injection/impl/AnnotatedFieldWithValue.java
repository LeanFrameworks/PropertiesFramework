/*-
 * #%L
 * PropertiesFramework :: Experiments
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

package com.github.leanframeworks.propertiesframework.base.injection.impl;

import javax.annotation.Generated;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AnnotatedFieldWithValue extends AnnotatedField {

    private final Object value;

    public AnnotatedFieldWithValue(AnnotatedField annotated, Object value) {
        this(annotated.getOwner(),
                annotated.getField(),
                annotated.getAnnotation(),
                annotated.getId(),
                value);
    }

    public AnnotatedFieldWithValue(Owner owner,
                                   Field field,
                                   Annotation annotation,
                                   String id,
                                   Object value) {
        super(owner, field, annotation, id);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Generated("intellij")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AnnotatedFieldWithValue that = (AnnotatedFieldWithValue) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Generated("intellij")
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{" +
                "owner=" + getOwner() +
                ", field=" + getFieldName() +
                ", annotation=" + getAnnotationName() +
                ", id='" + getId() + '\'' +
                ", value='" + getValue() + '\'' +
                '}';
    }
}
