package lesson_3.phonebook;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Phonebook {
    private Map<String, Set<String>> book;

    private static Set<String> noEntry = Collections.unmodifiableSet(Stream.of("Такой записи нет!").collect(Collectors.toSet()));

    Phonebook() {
        book = new HashMap<>();
    }

    void add(String surname, String phone) {
        Set<String> phoneList = book.getOrDefault(surname, new HashSet<>());
        phoneList.add(phone);
        book.put(surname, phoneList);
    }

    Set get(String surname){
        return book.getOrDefault(surname, noEntry);
    }

    void printAll() {
        for (Map.Entry<String, Set<String>> entry : book.entrySet()) {
            String bookEntry = entry.getKey();
            for (String phone : entry.getValue()) {
                bookEntry += " " + phone;
            }
            System.out.println(bookEntry);
        }
        System.out.println();
    }
}
