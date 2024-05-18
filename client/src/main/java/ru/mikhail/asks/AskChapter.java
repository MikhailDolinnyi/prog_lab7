package ru.mikhail.asks;

import ru.mikhail.commandLine.ConsoleInput;
import ru.mikhail.commandLine.ConsoleOutput;
import ru.mikhail.commandLine.PrintConsole;
import ru.mikhail.commandLine.Printable;
import ru.mikhail.exceptions.FIleFieldException;
import ru.mikhail.models.Chapter;
import ru.mikhail.utility.ExecuteManager;
import ru.mikhail.utility.OutputColors;


/**
 * Форма для глав
 */
public class AskChapter extends AskForm<Chapter> {
    private final Printable console;
    private final ConsoleInput scanner;

    public AskChapter(Printable console) {
        this.console = (ConsoleOutput.isFileMode())
                ? new PrintConsole()
                : console;

        this.scanner = (ConsoleOutput.isFileMode())
                ? new ExecuteManager()
                : new ConsoleInput();
    }

    /**
     * Сконструировать новый элемент класса {@link Chapter}
     *
     * @return объект класса {@link Chapter}
     */

    @Override
    public Chapter build() {
        return new Chapter(askName(), askMarinesCount());
    }

    private String askName() {
        String name;
        while (true) {
            console.println(OutputColors.toColor("Введите название главы", OutputColors.CYAN));
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                console.printError("Имя не может быть пустым");
                if (ConsoleOutput.isFileMode()) throw new FIleFieldException();
            } else {
                return name;
            }
        }
    }

    private Integer askMarinesCount() {
        while (true) {
            console.println(OutputColors.toColor("Введите количество морских пехотинцев", OutputColors.CYAN));
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                console.printError("Число пехотинцев должно быть числом типа int");
            }
        }
    }


}
