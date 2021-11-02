package DZ;

import java.util.Random;
import java.util.Scanner;

public class TicTacToeGame {

    private static final char DOT_X = 'X';
    private static final char DOT_O = 'O';
    private static final char DOT_EMPTY = '.';
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;
    private static int scoreHuman;
    private static int scoreAi;
    private static int roundCounter;
    private static char dotHuman;
    private static char dotAi;

    private static int inX;
    private static int inY;
    private static int winStore;

    public static void main(String[] args) {
        play();
    }

    private static void play() {
        while (true) {
            playRound();
            System.out.printf("SCORE: HUMAN     AI\n" +
                    "         %d       %d\n", scoreHuman, scoreAi);
            System.out.printf("Wanna play again? Y or N >>> ");
            if (!scanner.next().toLowerCase().equals("y")) {
                System.out.println("Good bye!");
                break;
            }
        }
    }

    private static void playRound() {
        System.out.printf("ROUND № %d\n", roundCounter++);
        initField(9, 9, 5);

        System.out.println("Please enter 'x' if you want to play with X, and something else for O >>> ");
        String x = scanner.next();
        if (x.toLowerCase().equals("x")) {
            dotHuman = DOT_X;
            dotAi = DOT_O;
        } else {
            dotHuman = DOT_O;
            dotAi = DOT_X;
        }
        printField();

        while (true) {
            if (dotHuman == DOT_X) {
                humanTurn();
                printField();
                if (checkAll(dotHuman)) break;
                aiTurn();
                printField();
                if (checkAll(dotAi)) break;
            } else {
                aiTurn();
                printField();
                if (checkAll(dotAi)) break;
                humanTurn();
                printField();
                if (checkAll(dotHuman)) break;
            }
        }
    }

    private static boolean checkAll(char dot) {
        if (checkWin(dot)) {
            if (dot == dotHuman) {
                System.out.println("Human WIN!!!");
                scoreHuman++;
            } else {
                System.out.println("AI WIN!!!");
                scoreAi++;
            }
            return true;
        }
        if (checkDraw()) return true;
        return false;
    }

    private static boolean checkDraw() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(y, x)) return false;
            }
        }
        System.out.println("DRAW");
        return true;
    }

    /*
     * Главное ДЗ:
     * Сделать проверку победы в циклах
     * В идеале, сделать такую проверку, которой неважен размер поля и длина выигрышной последовательности
     */
    private static boolean checkWin(char dot) {

        // надеюсь идея понятна, тут не все поле прохожу а беру за отсчет точку которую поставил пользователь/комп
        // вот, и проверяю по 4 направлениям

        int quantityHOR = 1, quantityVER = 1, quantityDIAG1 = 1, quantityDIAG2 = 1;
        int x = inX, y = inY;

        for (int i = 1; i < winStore; i++) {

            if (x+i <= fieldSizeX-1 && field[y][x+i] == dot) quantityHOR++;
            if (x-i >= 0            && field[y][x-i] == dot) quantityHOR++;

            if (y+i <= fieldSizeY-1 && field[y+i][x] == dot) quantityVER++;
            if (y-i >= 0            && field[y-i][x] == dot) quantityVER++;

            if (y+i <= fieldSizeY-1 && x+i <= fieldSizeX-1 && field[y+i][x+i] == dot) quantityDIAG1++;
            if (y-i >= 0            && x-i >= 0            && field[y-i][x-i] == dot) quantityDIAG1++;

            if (y+i <= fieldSizeY-1 && x-i >= 0            && field[y+i][x-i] == dot) quantityDIAG2++;
            if (y-i >= 0            && x+i <= fieldSizeX-1 && field[y-i][x+i] == dot) quantityDIAG2++;

        }

        if (quantityHOR == winStore || quantityVER == winStore || quantityDIAG1 == winStore || quantityDIAG2 == winStore) return true;

        return false;
    }

    private static void aiTurn() {

        // задача 1 обломать пользователя
        if (findPoint(dotHuman)) return;

        // задача 2 найти оптимальную точку
        if (findPoint(dotAi)) return;

        // задача 3 найти любую точку
        int x;
        int y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isCellEmpty(y, x));
        field[y][x] = dotAi;

    }

    private static boolean findPoint(char dot) {

        int quantityHOR;
        int pointHORx = -1, pointHORy = -1;

        int quantityVER;
        int pointVERx = -1, pointVERy = -1;

        int quantityDIAG1;
        int pointDIAG1x = -1, pointDIAG1y = -1;

        int quantityDIAG2;
        int pointDIAG2x = -1, pointDIAG2y = -1;

        for (int y = 0; y < fieldSizeY; y++) {

            for (int x = 0; x < fieldSizeY; x++) {

                if (field[y][x] == dot) {

                    quantityHOR = 1;
                    quantityVER = 1;
                    quantityDIAG1 = 1;
                    quantityDIAG2 = 1;

                    for (int i = 1; i < winStore; i++) {

                        if (x + i <= fieldSizeX - 1 && field[y][x + i] == dot) quantityHOR++;
                        if (x + i <= fieldSizeX - 1 && field[y][x + i] == DOT_EMPTY){
                            pointHORx = x + i;
                            pointHORy = y;
                        }

                        if (y + i <= fieldSizeY - 1 && field[y + i][x] == dot) quantityVER++;
                        if (y + i <= fieldSizeY - 1 && field[y + i][x] == DOT_EMPTY){
                            pointVERx = x;
                            pointVERy = y + i;
                        }

                        if (y + i <= fieldSizeY - 1 && x + i <= fieldSizeX - 1 && field[y + i][x + i] == dot){
                            quantityDIAG1++;
                        }

                        if (y + i <= fieldSizeY - 1 && x + i <= fieldSizeX - 1 && field[y + i][x + i] == DOT_EMPTY){
                            pointDIAG1x = x + i;
                            pointDIAG1y = y + i;
                        }

                        if (y + i <= fieldSizeY - 1 && x - i >= 0 && field[y + i][x - i] == dot) quantityDIAG2++;
                        if (y + i <= fieldSizeY - 1 && x - i >= 0 && field[y + i][x - i] == DOT_EMPTY){
                            pointDIAG2y = y + i;
                            pointDIAG2x = x - i;
                        }

                    }


                        //----------------------------------------------------------------

                    if (quantityHOR == winStore - 1 && pointHORx != -1 && pointHORy != -1) {
                        field[pointHORy][pointHORx] = dotAi;
                        return true;
                    }

                    if (quantityVER == winStore - 1 && pointVERx != -1 && pointVERy != -1) {
                        field[pointVERy][pointVERx] = dotAi;
                        return true;
                    }

                    if (quantityDIAG1 == winStore - 1 && pointDIAG1x != -1 && pointDIAG1y != -1) {
                        field[pointDIAG1y][pointDIAG1x] = dotAi;
                        return true;
                    }

                    if (quantityDIAG2 == winStore - 1 && pointDIAG2x != -1 && pointDIAG2y != -1) {
                        field[pointDIAG2y][pointDIAG2x] = dotAi;
                        return true;
                    }

                }

            }

        }

        return false;

    }

    private static void humanTurn() {
        boolean isFirstTry = true;
        do {
            if (!isFirstTry) {
                System.out.println("Wrong coordinates, try again!");
            }
            isFirstTry = false;
            System.out.print("Please enter coordinates of your turn x & y split by whitespace >>>> ");
            inX = scanner.nextInt() - 1;
            inY = scanner.nextInt() - 1;
        } while (!isCellValid(inY, inX) || !isCellEmpty(inY, inX));
        field[inY][inX] = dotHuman;
    }

    private static boolean isCellValid(int y, int x) {
        return x >= 0 && y >= 0 && x < fieldSizeX && y < fieldSizeY;
    }

    private static boolean isCellEmpty(int y, int x) {
        return field[y][x] == DOT_EMPTY;
    }

    private static void initField(int sizeX, int sizeY, int win) {
        fieldSizeX = sizeX;
        fieldSizeY = sizeY;
        winStore = win;
        field = new char[sizeY][sizeX];
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                field[i][j] = DOT_EMPTY;
            }
        }
    }

    private static void printField() {
        System.out.print("+");
        for (int i = 0; i < fieldSizeX * 2 + 1; i++) {
            System.out.print(i % 2 == 0 ? "|" : i / 2 + 1);
        }
        System.out.println();
        for (int i = 0; i < fieldSizeY; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < fieldSizeX; j++) {
                System.out.print(field[i][j] + "|");
            }
            System.out.println();
        }
//        for (int i = 0; i < fieldSizeX * 2 + 1; i++) {
//            System.out.print("-");
//        }
        System.out.println();
//        for (int y = 0; y < fieldSizeY; y++) {
//            for (int x = 0; x < fieldSizeX; x++) {
//                System.out.print(field[y][x] + " ");
//            }
//            System.out.println();
//        }
    }

}
