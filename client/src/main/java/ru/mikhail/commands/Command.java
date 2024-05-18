package ru.mikhail.commands;

import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.exceptions.InvalidFormException;
import ru.mikhail.models.SpaceMarine;

import java.util.Objects;

public abstract class Command {

    private final String name;

    private final String description;


    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        Command command = (Command) o;
        return (Objects.equals(name, command.name) && Objects.equals(description, command.description));
    }


    public int hashCode() {
        return Objects.hash(name, description);

    }

    public String toString() {
        return name + " " + description;
    }

    public abstract SpaceMarine execute(String args) throws InvalidFormException, IllegalArgumentsException;


}