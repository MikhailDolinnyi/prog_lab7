package ru.mikhail.commands;


import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.managers.CommandManager;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;

/**
 * Команда 'help'
 * Вывести справку по доступным командам
 */
public class Help extends Command {
    private final CommandManager commandManager;


    public Help(CommandManager commandManager) {
        super("help", ": вывести справку по доступным командам");
        this.commandManager = commandManager;

    }

    /**
     * Исполнить команду
     *
     * @param request аргументы команды
     * @throws IllegalArgumentsException неверные аргументы команды
     */

    @Override
    public Response execute(Request request) throws IllegalArgumentsException {
        if (!request.getArgs().isBlank()) throw new IllegalArgumentsException();
        return new Response(ResponseStatus.OK,
                String.join("\n",
                        commandManager.getCommands()
                                .stream().map(Command::toString).toList()));
    }
}