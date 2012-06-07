package de.brands4friends.daleq.internal.container;

import static nl.jqno.equalsverifier.EqualsVerifier.forClass;

import org.junit.Test;

import nl.jqno.equalsverifier.Warning;

public class FieldContainerTest{

    @Test
    public void testHashcodeAndEquals(){
        forClass(FieldContainer.class).suppress(Warning.NULL_FIELDS).verify();
    }
}