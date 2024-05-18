package ru.mikhail.utility;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.mikhail.App;
import ru.mikhail.managers.CommandManager;
import ru.mikhail.managers.ConnectionManager;
import ru.mikhail.managers.DatabaseManager;
import ru.mikhail.managers.FutureManager;

public class Server {
    private final int port;
    private final CommandManager commandManager;


    public static final Logger rootLogger = LogManager.getLogger(App.class);

    private final DatabaseManager databaseManager;

    public Server(CommandManager commandManager, DatabaseManager databaseManager) {
        this.port = App.PORT;
        this.commandManager = commandManager;
        this.databaseManager = databaseManager;
    }

    public void run() {


        rootLogger.info("--------------------------------------------------------------------");
        rootLogger.info("-----------------СЕРВЕР УСПЕШНО ЗАПУЩЕН-----------------------------");
        rootLogger.info("--------------------------------------------------------------------");

        while (true) {
            FutureManager.checkAllFutures();
            Thread connectionThread = new Thread(new ConnectionManager(commandManager, databaseManager, port));
            connectionThread.start();

        }
    }


}
