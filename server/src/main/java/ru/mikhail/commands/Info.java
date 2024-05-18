package ru.mikhail.commands;


import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.managers.CollectionManager;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;
import ru.mikhail.utility.OutputColors;

/**
 * Команда 'info'
 * Выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
 */
public class Info extends Command {
    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        super("info", ": вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.collectionManager = collectionManager;
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
        String lastInitTime = (collectionManager.getLastInitTime() == null)
                ? "В сессии коллекция не инициализирована"
                : collectionManager.getLastInitTime();
        String lastSaveTime = (collectionManager.getLastSaveTime() == null)
                ? "В сессии коллекция не инициализирована "
                : collectionManager.getLastSaveTime();
        String stringBuilder = "Сведения о коллекции: \n" +
                OutputColors.toColor("Тип: ", OutputColors.GREEN) + collectionManager.collectionType() + "\n" +
                OutputColors.toColor("Количество элементов: ", OutputColors.GREEN) + collectionManager.collectionSize() + "\n" +
                OutputColors.toColor("Дата последней инициализации: ", OutputColors.GREEN) + lastInitTime + "\n" +
                OutputColors.toColor("Дата последнего изменения: ", OutputColors.GREEN) + lastSaveTime + "\n";
        return new Response(ResponseStatus.OK, stringBuilder);
    }
}
