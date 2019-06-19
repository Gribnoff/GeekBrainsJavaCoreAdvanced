package lesson_4.regex_chat.validator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PassValidator validator = new PassValidator();
        boolean isValid = false;
        while (!isValid) {
            System.out.println("Введите пароль:");
            String testPassword = scanner.nextLine();
            System.out.println("Валидность вашего пароля: " + validator.generalValidation(testPassword) + "\n");
            if (validator.generalValidation(testPassword))
                isValid = true;
        }
    }
}
