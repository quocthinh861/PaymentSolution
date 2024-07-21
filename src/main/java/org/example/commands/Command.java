package org.example.commands;

import org.example.UserContext;

public interface Command<T> {
    T execute(String... args);

    default boolean validate(String... args) {
        return true;
    }
}
