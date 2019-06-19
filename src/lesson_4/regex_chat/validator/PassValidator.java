package lesson_4.regex_chat.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PassValidator {
    boolean generalValidation(String text) {
        Pattern pattern = Pattern.compile("\\A(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\p{P}\\p{S}]).{8,}\\z");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
