package ru.mikhail.commands;

import ru.mikhail.asks.AskSpaceMarine;
import ru.mikhail.commandLine.Printable;
import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.exceptions.InvalidFormException;
import ru.mikhail.models.SpaceMarine;

public class RemoveLower extends Command {
    private final Printable console;

    public RemoveLower(Printable console) {
        super("remove_lower", "удаляет элементы, если его значение меньше, чем у наименьшего элемента ");
        this.console = console;
    }

    public SpaceMarine execute(String args) throws InvalidFormException, IllegalArgumentsException {
        if (!args.isBlank()) throw new IllegalArgumentsException();
        SpaceMarine spaceMarine = new AskSpaceMarine(console).build();
        if (!spaceMarine.validate()) throw new InvalidFormException();
        return spaceMarine;
    }
}
