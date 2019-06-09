package Lesson_2.Exceptions;

public class ExceptionsMain {
    public static void main(String[] args) {
        String[][] array1 = {{"1", "2", "3", "4"},
                {"5", "6", "7", "8"},
                {"9", "10", "11", "12"},
                {"13", "14", "15", "16", "17"}};

        String[][] array2 = {{"1", "2", "3", "4"},
                {"5", "6", "7", "8"},
                {"a", "9", "10", "11"},
                {"12", "13", "14", "15"}};

        try {
            System.out.println(arraySum(array1));
        } catch (MyArraySizeException | MyArrayDataException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(arraySum(array2));
        } catch (MyArrayDataException | MyArraySizeException e) {
            e.printStackTrace();
        }
    }

    private static int arraySum(String[][] arr) throws MyArraySizeException, MyArrayDataException{
        int sum = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int x;
                if (arr.length != 4 || arr[i].length != 4)
                    throw new MyArraySizeException();
                try {
                    x = Integer.parseInt(arr[i][j]);
                } catch (NumberFormatException e) {
                    throw new MyArrayDataException(i, j);
                }
                sum += x;
            }
        }
        return sum;
    }
}
