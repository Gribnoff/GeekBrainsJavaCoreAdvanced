package Lesson_1.Marafon;

public class Dog extends Animal {
    static String[] names = {"Жучка", "Барбос", "Бобик", "Буч", "Варяг", "Граф", "Жек", "Жиган", "Морис", "Нортон"};
    static int count = 0;

    public Dog(String name) {
        super("Пес", name, 500, 5, 20);
        count++;
    }
}
