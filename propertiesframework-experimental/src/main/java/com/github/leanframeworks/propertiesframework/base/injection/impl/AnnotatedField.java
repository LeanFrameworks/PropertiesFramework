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
import java.lang.reflect.Modifier;

public class AnnotatedField implements Identifiable {

    private final Owner owner;

    private final Field field;

    private final Annotation annotation;

    private final String id;

    public AnnotatedField(AnnotatedField annotated) {
        this(annotated.getOwner(),
                annotated.getField(),
                annotated.getAnnotation(),
                annotated.getId());
    }

    public AnnotatedField(Owner owner, Field field, Annotation annotation, String id) {
        this.owner = owner;
        this.field = field;
        this.annotation = annotation;
        this.id = id;
    }

    public Owner getOwner() {
        return owner;
    }

    public Class<?> getOwnerClass() {
        return owner.getOwnerClass();
    }

    public String getOwnerClassName() {
        return getOwnerClass().getCanonicalName();
    }

    public Object getOwnerInstance() {
        return owner.getOwnerInstance();
    }

    public Field getField() {
        return field;
    }

    public String getFieldName() {
        return getField().getName();
    }

    public Class<?> getFieldClass() {
        return getField().getType();
    }

    public String getFieldClassName() {
        return getField().getType().getCanonicalName();
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public String getAnnotationName() {
        return getAnnotation().annotationType().getSimpleName();
    }

    @Override
    public String getId() {
        return id;
    }

    public String getLogString() {
        String log;

        if (Modifier.isStatic(getField().getModifiers())) {
            log = "Static Field '" + getFieldName() +
                    "', annotated with '" + getAnnotationName() +
                    "' and ID '" + getId() +
                    "', of class '" + getOwnerClassName() +
                    "'";
        } else {
            log = "Field '" + getFieldName() +
                    "', annotated with '" + getAnnotationName() +
                    "' and ID '" + getId() +
                    "', of instance '" + getOwnerInstance() +
                    "' of class '" + getOwnerClassName() +
                    "'";
        }

        return log;
    }

    @Generated("intellij")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotatedField that = (AnnotatedField) o;

        if (!owner.equals(that.owner)) return false;
        if (!field.equals(that.field)) return false;
        if (!annotation.equals(that.annotation)) return false;
        return id.equals(that.id);
    }

    @Generated("intellij")
    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + field.hashCode();
        result = 31 * result + annotation.hashCode();
        result = 31 * result + id.hashCode();
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
                '}';
    }
}
