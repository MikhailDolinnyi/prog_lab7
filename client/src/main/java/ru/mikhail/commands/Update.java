package ru.mikhail.commands;


import ru.mikhail.asks.AskSpaceMarine;
import ru.mikhail.commandLine.Printable;
import ru.mikhail.exceptions.FIleFieldException;
import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.models.SpaceMarine;

public class Update extends Command {
    private final Printable console;

    public Update(Printable console) {
        super("update", "обновление по id");
        this.console = console;
    }


    public SpaceMarine execute(String args) throws IllegalArgumentsException {
        if (args.isBlank()) throw new IllegalArgumentsException();
        SpaceMarine spaceMarine = new AskSpaceMarine(console).build();
        if (!spaceMarine.validate()) throw new FIleFieldException();
        return spaceMarine;

    }
}
