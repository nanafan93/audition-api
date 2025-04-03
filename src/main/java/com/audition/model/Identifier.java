package com.audition.model;

import lombok.Getter;

@Getter
public final class Identifier {

    private final int rawValue;

    public Identifier(final String id) {
        this.rawValue = requireValidId(id);
    }

    private int requireValidId(final String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(String.format("Id cannot be null or empty, received [%s]", id));
        }
        try {
            final int parsedId = Integer.parseInt(id);
            if (parsedId <= 0) {
                throw new IllegalArgumentException(String.format("Id must be a positive Integer, received [%s]", id));
            }
            return parsedId;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Id must be a valid Integer, received [%s]", id), e);

        }
    }
}
