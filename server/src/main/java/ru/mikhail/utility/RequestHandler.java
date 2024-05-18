package ru.mikhail.utility;



import ru.mikhail.exceptions.*;
import ru.mikhail.managers.CommandManager;
import ru.mikhail.managers.ConnectionManagerPool;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;

import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

public class RequestHandler implements Callable<ConnectionManagerPool> {
    private final CommandManager commandManager;
    private final Request request;
    private ObjectOutputStream objectOutputStream;

    public RequestHandler(CommandManager commandManager, Request request, ObjectOutputStream objectOutputStream) {
        this.commandManager = commandManager;
        this.request = request;
        this.objectOutputStream = objectOutputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    @Override
    public ConnectionManagerPool call() {
        try {
            commandManager.addToHistory(request.getUser(), request.getCommandName());
            return new ConnectionManagerPool(commandManager.execute(request), objectOutputStream);
        } catch (IllegalArgumentsException e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.WRONG_ARGUMENTS,
                    "Неверное использование аргументов команды"), objectOutputStream);
        } catch (CommandRuntimeException e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.ERROR,
                    "Ошибка при исполнении программы"), objectOutputStream);
        } catch (NoCommandException e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.ERROR, "Такой команды нет в списке"), objectOutputStream);
        } catch (ExitException e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.EXIT), objectOutputStream);
        } catch (InvalidFormException e) {
            throw new RuntimeException(e);
        }
    }
}

