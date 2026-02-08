package org.example;

import org.example.consoleInputValidation.ConsoleInputProcessor;

import java.util.List;
import java.util.function.Consumer;

public class Main {
    static ConsoleInputProcessor inputProcessor;
    static UserService userService;

    static void main() {
        inputProcessor = new ConsoleInputProcessor();
        userService = new UserService(new UserDao());
        Integer menuUserInput = 0;

        while (menuUserInput != -1) {
            System.out.println(MenuTexts.MAIN_MENU.getMessage());
            menuUserInput = inputProcessor.getInteger();
            switch (menuUserInput) {
                case (1):
                    userService.addNewUser(createUserFromInput());
                    break;
                case (2):
                    List<User> temp = userService.getListOfUsers();
                    if (!temp.isEmpty()) System.out.println(temp);
                    else System.out.println(MenuTexts.COLLECTION_IS_EMPTY.getMessage());
                    break;
                case (3):
                    runConsumerIfUserIsFoundById(System.out::println);
                    break;
                case (4):
                    runConsumerIfUserIsFoundById(Main::updateUserWithId);
                    break;
                case (5):
                    userService.removeUserRecordById(getIdFromInput());
                    break;
                case (-1):
                    break;
                default:
                    System.out.println(MenuTexts.DEFAULT_SWITCH_MESSAGE.getMessage());
                    break;
            }
        }
        inputProcessor.close();
        HibernateSessionFactoryUtil.shutDown();
    }

    private static User createUserFromInput() {
        System.out.println(MenuTexts.PICK_NAME.getMessage());
        String name = inputProcessor.getNonEmptyString();
        System.out.println(MenuTexts.PICK_EMAIL.getMessage());
        String email = inputProcessor.getNonEmptyString();
        System.out.println(MenuTexts.PICK_AGE.getMessage());
        Integer age = inputProcessor.getNonNegativeInteger();
        return new User(name, email, age);
    }

    private static long getIdFromInput() {
        System.out.println(MenuTexts.ENTER_ENTRY_ID.getMessage());
        return inputProcessor.getPositiveInteger();
    }

    private static void runConsumerIfUserIsFoundById(Consumer<User> consumer) {
        userService.findUserById(getIdFromInput()).
                ifPresentOrElse( consumer,
                        () -> System.out.println(MenuTexts.ENTRY_NOT_FOUND.getMessage()));
    }

    private static void updateUserWithId(User user) {
        while (true) {
            System.out.printf("Текущий user:%s%n%s%n", user, MenuTexts.SET_FIELD_MENU.getMessage());
            switch (inputProcessor.getInteger()) {
                case (1):
                    System.out.println(MenuTexts.PICK_NAME.getMessage());
                    user.setName(inputProcessor.getNonEmptyString());
                    break;
                case (2):
                    System.out.println(MenuTexts.PICK_EMAIL.getMessage());
                    user.setEmail(inputProcessor.getNonEmptyString());
                    break;
                case (3):
                    System.out.println(MenuTexts.PICK_AGE.getMessage());
                    user.setAge(inputProcessor.getNonNegativeInteger());
                    break;
                case (4):
                    userService.updateUserRecord(user);
                    return;
                case (0):
                    return;
                default:
                    System.out.println(MenuTexts.DEFAULT_SWITCH_MESSAGE.getMessage());
                    break;
            }
        }
    }
}
