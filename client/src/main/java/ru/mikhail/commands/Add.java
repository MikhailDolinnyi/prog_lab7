package ru.mikhail.commands;


import ru.mikhail.asks.AskSpaceMarine;
import ru.mikhail.commandLine.Printable;
import ru.mikhail.exceptions.FIleFieldException;
import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.exceptions.InvalidFormException;
import ru.mikhail.models.SpaceMarine;

public class Add extends Command {
    private final Printable console;


    public Add(Printable console) {
        super("add", "добавление элемента");
        this.console = console;
    }


    public SpaceMarine execute(String args) throws InvalidFormException, IllegalArgumentsException {
        try {
            if (!args.isBlank()) throw new IllegalArgumentsException();

            SpaceMarine spaceMarine = new AskSpaceMarine(console).build();
            if (!spaceMarine.validate()) throw new InvalidFormException();
            return spaceMarine;
        } catch (FIleFieldException e) {
            console.printError("Поля объекта не валидны!");
            return null;

        }
    }


}
