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

package de.brands4friends.daleq;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.dbunit.dataset.datatype.DataType;
import org.junit.Test;

import com.google.common.base.Optional;

import de.brands4friends.daleq.internal.template.StringTemplateValue;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class FieldDefTest {

    public static final String NAME = "foo";
    public static final String TEMPLATE = "foo";
    public static final String NEW_NAME = "new name";

    @Test
    public void testHashCodeAndEquals() {
        EqualsVerifier
                .forClass(FieldDef.class)
                .withPrefabValues(Optional.class, Optional.of("a"), Optional.of("b"))
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void aFieldWithName_should_haveName() {
        assertThat(FieldDef.fd(DataType.BIGINT).name(NAME).getName().isPresent(), is(true));
    }

    @Test
    public void aFieldWithoutName_should_haveName() {
        assertThat(FieldDef.fd(DataType.BIGINT).getName().isPresent(), is(false));
    }

    @Test
    public void aFieldWithTemplate_should_haveATemplate() {
        assertThat(someFd().template(TEMPLATE).getTemplate().isPresent(), is(true));
    }

    @Test
    public void aFieldWithoutTemplate_should_haveNoTemplate() {
        assertThat(someFd().getTemplate().isPresent(), is(false));
    }

    @Test
    public void aFieldTemplate_should_beCorrect() {
        final TemplateValue expected = new StringTemplateValue(TEMPLATE);
        assertThat(someFd().template(TEMPLATE).getTemplate().get(), is(expected));
    }

    @Test
    public void chainNameAndTemplate_should_haveBoth() {
        final FieldDef fd = someFd().name(NEW_NAME).template(TEMPLATE);
        assertThat(fd.getName().get(), is(NEW_NAME));
        final TemplateValue expected = new StringTemplateValue(TEMPLATE);
        assertThat(fd.getTemplate().get(), is(expected));
    }

    @Test
    public void chainTemplateAndName_should_haveBoth() {
        final FieldDef fd = someFd().template(TEMPLATE).name(NEW_NAME);
        assertThat(fd.getName().get(), is(NEW_NAME));
        final TemplateValue expected = new StringTemplateValue(TEMPLATE);
        assertThat(fd.getTemplate().get(), is(expected));
    }

    @Test(expected = NullPointerException.class)
    public void nameWithNull_should_fail() {
        someFd().name(null);
    }

    @Test(expected = NullPointerException.class)
    public void templateWithNull_should_fail() {
        someFd().template(null);
    }

    private FieldDef someFd() {
        return FieldDef.fd(DataType.INTEGER);
    }
}
