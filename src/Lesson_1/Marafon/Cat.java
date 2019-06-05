package Lesson_1.Marafon;

public class Cat extends Animal {
    static String[] names = {"Барсик", "Мурзик", "Васька", "Борхес", "Варяг", "Гагарин", "Зефир", "Беляш", "Пупс", "Рабинович"};
    static int count = 0;

    public Cat(String name) {
        super("Кот", name, 200, 20, 0);
        count++;
    }
}