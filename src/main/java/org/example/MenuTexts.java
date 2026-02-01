package org.example;

public enum MenuTexts {
    MAIN_MENU("""
            Выберите действие:
            \t1) Добавить новую запись
            \t2) Вывести все записи
            \t3) Вывести запись с ID
            \t4) Обновить запись с ID
            \t5) Удалить запись с ID
            \t-1) Выйти из программы"""),

    SET_FIELD_MENU("""
            Выберите какое поле изменить:
            \t1) имя
            \t2) email
            \t3) возвраст
            \t4) Подтвердить
            \t0) Отмена"""),

    PICK_NAME("Введите имя (строка): "),
    PICK_EMAIL("Введите email (строка): "),
    PICK_AGE("Введите возраст (неотрицательное число): "),
    ENTER_ENTRY_ID("Введите ID записи: "),
    COLLECTION_IS_EMPTY("БД пуста!"),
    ENTRY_NOT_FOUND("Записи с таким ID нет..."),
    DEFAULT_SWITCH_MESSAGE("Такого варианта нет, попробуйте еще раз.");

    private final String message;

    MenuTexts(String message) {
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}
