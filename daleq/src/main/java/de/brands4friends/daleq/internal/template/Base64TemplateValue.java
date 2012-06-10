package de.brands4friends.daleq.internal.template;

import org.apache.commons.codec.binary.Base64;

import com.google.common.primitives.Longs;

final class Base64TemplateValue implements TemplateValue {

    @Override
    public String render(final long value) {
        return Base64.encodeBase64String(Longs.toByteArray(value));
    }
}
