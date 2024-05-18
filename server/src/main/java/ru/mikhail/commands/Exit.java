package ru.mikhail.commands;


import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;

/**
 * Команда 'exit'
 * завершить программу (без сохранения в файл)
 */
public class Exit extends Command {
    public Exit() {
        super("exit", ": завершить программу (без сохранения в файл)");
    }

    /**
     * Исполнить команду
     *
     * @param request аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArgumentsException {
        if (!request.getArgs().isBlank()) throw new IllegalArgumentsException();
        return new Response(ResponseStatus.EXIT);
    }
}
