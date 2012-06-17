/*
 * Copyright 2012 brands4friends, Private Sale GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.brands4friends.daleq.internal.builder;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.brands4friends.daleq.FieldContainer;
import de.brands4friends.daleq.NoSuchDaleqFieldException;
import de.brands4friends.daleq.RowContainer;

public final class RowContainerImpl implements RowContainer {

    private final List<FieldContainer> fields;
    private final Map<String, FieldContainer> index;

    public RowContainerImpl(final List<FieldContainer> fields) {
        this.fields = ImmutableList.copyOf(Preconditions.checkNotNull(fields));
        this.index = Maps.uniqueIndex(this.fields, new Function<FieldContainer, String>() {
            @Override
            public String apply(@Nullable final FieldContainer field) {
                if (field == null) {
                    return null;
                }
                return field.getName();
            }
        });
    }

    @Override
    public List<FieldContainer> getFields() {
        return fields;
    }

    public FieldContainer getFieldBy(final String fieldName) {
        if (!this.index.containsKey(fieldName)) {
            throw new NoSuchDaleqFieldException("fieldName");
        }
        return this.index.get(fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fields);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof RowContainerImpl) {
            final RowContainerImpl that = (RowContainerImpl) obj;

            return Objects.equal(fields, that.fields);
        }

        return false;
    }

    @Override
    public String toString() {
        return "[" + Joiner.on(",").join(fields) + "]";
    }
}
