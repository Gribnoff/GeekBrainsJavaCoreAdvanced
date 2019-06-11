package lesson_3.words;

import java.util.HashMap;
import java.util.Map;

/*
Создать массив с набором слов (10-20 слов, среди которых должны встречаться повторяющиеся).
Найти и вывести список уникальных слов, из которых состоит массив (дубликаты не считаем).
Посчитать, сколько раз встречается каждое слово.
 */

public class WordsMain {
    public static void main(String[] args) {
        String[] words = initWordArray(20);
        printWordArray(words);
        printWordsCount(words);
    }

    //Создаётся массив заданной длины и заполняется случайнымим словами
    private static String[] initWordArray(int size) {
        String[] arr = new String[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = "word#" + (int)(Math.random() * arr.length / 2 + 1);
        }
        return arr;
    }

    //печать массива в строку
    private static void printWordArray(String[] arr) {
        for (String s : arr) {
            System.out.print(s + " ");
        }
        System.out.println();
    }

    //печать количества повторов каждого слова
    private static void printWordsCount(String[] arr) {
        Map<String, Integer> map = new HashMap<>();

        for (String s : arr) {
            Integer count = map.get(s);
            map.put(s, count == null ? 1 : count + 1);
        }

        System.out.println(map);
    }
}
