package ru.mikhail.commands;


import ru.mikhail.asks.AskSpaceMarine;
import ru.mikhail.commandLine.Printable;
import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.models.SpaceMarine;

public class Add extends Command {
    private final Printable console;


    public Add(Printable console) {
        super("add", "добавление элемента");
        this.console = console;
    }


    public SpaceMarine execute(String args) throws IllegalArgumentsException {
        if (!args.isBlank()) throw new IllegalArgumentsException();
        return new AskSpaceMarine(console).build();
    }


}
