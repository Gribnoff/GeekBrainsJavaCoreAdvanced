package lesson_3.phonebook;

/*
Написать простой класс ТелефонныйСправочник, который хранит в себе список фамилий и телефонных номеров.
В этот телефонный справочник с помощью метода add() можно добавлять записи.
С помощью метода get() искать номер телефона по фамилии.
Следует учесть, что под одной фамилией может быть несколько телефонов, тогда при запросе такой фамилии должны выводиться все телефоны.
 */

public class PhoneMain {
    public static void main(String[] args) {
        Phonebook myBook = new Phonebook();

        myBook.add("Петров", "8-999-124-56-78");
        myBook.add("Иванов", "8-985-421-55-77");
        myBook.add("Сидоров", "8-926-158-54-11");
        myBook.add("Петров", "8-916-173-36-24");
        myBook.add("Иванов", "8-960-197-44-78");

        myBook.printAll();

        System.out.println(myBook.get("Васильев"));
        System.out.println(myBook.get("Петров"));
    }
}
