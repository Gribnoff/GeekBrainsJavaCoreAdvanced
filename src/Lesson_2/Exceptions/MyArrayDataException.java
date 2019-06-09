package Lesson_2.Exceptions;

class MyArrayDataException extends Exception {

    MyArrayDataException(int i, int j) {
        super("Элемент в ячейке [" + i + " ," + j + "] не является числом");
    }
}
