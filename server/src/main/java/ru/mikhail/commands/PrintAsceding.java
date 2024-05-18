package ru.mikhail.commands;


import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.managers.CollectionManager;
import ru.mikhail.models.SpaceMarine;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Команда 'print_asceding'
 * выводит элементы коллекции в порядке возрастания
 */

public class PrintAsceding extends Command {
    private final CollectionManager collectionManager;

    public PrintAsceding(CollectionManager collectionManager) {
        super("print_asceding", " : вывести элементы коллекции в порядке возрастания");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws IllegalArgumentsException {

        if (!request.getArgs().isBlank()) throw new IllegalArgumentsException();
        Collection<SpaceMarine> collection = collectionManager.getCollection();
        if (collection == null || collection.isEmpty()) {
            return new Response(ResponseStatus.ERROR, "Коллекция еще не инициализирована");
        }
        return new Response(ResponseStatus.OK, "Коллекция: ", collection.stream().filter(Objects::nonNull).
                sorted(SpaceMarine::compareTo).collect(Collectors.toList()));
    }


}
