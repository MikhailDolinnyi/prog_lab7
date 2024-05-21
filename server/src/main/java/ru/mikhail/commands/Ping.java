package ru.mikhail.commands;


import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;

/**
 * Команда 'ping'
 * пингануть сервак
 */
public class Ping extends Command {
    public Ping() {
        super("ping", ": пингануть сервер");
    }

    /**
     * Исполнить команду
     *
     * @param request запрос клиента
     * @throws ru.mikhail.exceptions.IllegalArgumentsException неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArgumentsException {
        return new Response(ResponseStatus.OK, "pong");
    }
}
