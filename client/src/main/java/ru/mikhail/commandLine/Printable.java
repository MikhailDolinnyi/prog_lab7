package ru.mikhail.commandLine;


import ru.mikhail.utility.OutputColors;

/**
 * Интерфейс объединяющий способы вывода
 */
public interface Printable {

    void println(String a);

    void print(String a);

    void printError(String a);

    default void println(String a, OutputColors consoleColors) {
        println(a);
    }


    default void print(String a, OutputColors consoleColors) {
        print(a);
    }

}
