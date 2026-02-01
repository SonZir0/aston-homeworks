package org.example.consoleInputValidation;

import java.io.*;
import java.util.function.Predicate;

/**
 * Класс для получения инпута через консоль.
 * Публичные методы getXXX выполняются в цикле до тех пор, пока не будет получен валидный результат.
 * При не корректном инпуте со стороны пользователя, все методы должны выводить сообщение о типе ожидаемого инпута.
 * По завершению работы нужно закрыть через close().
 */
public class ConsoleInputProcessor implements AutoCloseable {
    private static final String INCORRECT_INPUT_TEMPLATE = "Не корректный ввод. Пожалуйста, введите %s\n";
    private static final String NOT_INT = String.format(INCORRECT_INPUT_TEMPLATE, "-ЦЕЛОЕ ЧИСЛО-");
    private static final String NOT_POSITIVE_INT = String.format(INCORRECT_INPUT_TEMPLATE, "-ЦЕЛОЕ ПОЛОЖИТЕЛЬНОЕ ЧИСЛО-");
    private static final String NOT_NON_NEGATIVE_INT = String.format(INCORRECT_INPUT_TEMPLATE, "-ЦЕЛОЕ НЕОТРИЦАТЕЛЬНОЕ ЧИСЛО-");

    private final BufferedReader bf;

    public ConsoleInputProcessor() {
        this.bf = new BufferedReader(new InputStreamReader(System.in));
    }

    public String getNonEmptyString() {
        while (true) {
            String tempStr = getInput();
            if (StringValidator.isNonEmptySting(tempStr))
                return tempStr.trim();
        }
    }

    public Integer getInteger() {
        Predicate<Integer> anyIntIsValid = (temp) -> true;
        return getRequiredInt(NOT_INT, anyIntIsValid);
    }

    public Integer getNonNegativeInteger() {
        Predicate<Integer> isNonNegative = (temp) -> temp > -1;
        return getRequiredInt(NOT_NON_NEGATIVE_INT, isNonNegative);
    }

    public Integer getPositiveInteger() {
        Predicate<Integer> isPositive = (temp) -> temp > 0;
        return getRequiredInt(NOT_POSITIVE_INT, isPositive);
    }

    public Integer getRequiredInt(String messageOnIncorrectInput, Predicate<Integer> isValid) {
        while (true) {
            Integer temp;
            String tempStr = getNonEmptyString();

            if (StringValidator.isInteger(tempStr) && isValid.test(temp = Integer.parseInt(tempStr)))
                return temp;
            System.out.print(messageOnIncorrectInput);
        }
    }

    private String getInput() {
        try {
            return bf.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            bf.close();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось закрыть BufferedReader в ConsoleInputProcessor");
        }
    }
}