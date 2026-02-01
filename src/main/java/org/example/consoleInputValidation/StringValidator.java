package org.example.consoleInputValidation;

import java.util.regex.Pattern;

    //  Допускаем нечитаемые символы как до, так и после искомых подстрок.
public class StringValidator {
    /*  integerRegEx должен пропускать строковое "число", с опциональными +/- впереди.
        Незначащие нули НЕ допускаются.
        */
    static Pattern integerRegEx = Pattern.compile("^\\s*([+-]?[1-9]\\d*|[+-]?0)\\s*$");

    public static boolean isNonEmptySting(String userInput) {
        return userInput != null && !userInput.isBlank();
    }

    public static boolean isInteger(String userInput) {
        return userInput != null && integerRegEx.matcher(userInput).matches();
    }
}
