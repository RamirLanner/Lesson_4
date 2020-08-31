package Homework_1;

import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Homework1 {

    private static final int SIZE = 5;
    private static final int SIZE_TO_WIN = 4;
    private static final char DOT_EMPTY = '•';
    private static final char DOT_X = 'X';
    private static final char DOT_O = 'O';
    private static int[][] scoreMapDef;
    private static int[][] scoreMapAttack;
    private static int[][] scoreMap;
    private static char[][] gameMap;
    private static Scanner scanner = new Scanner(System.in);
    //public static Random random = new Random();

    public static void main(String[] args) {
        initGameMap();
        while (true) {
            humanTurn();
            printGameMap();
            if (checkWin(DOT_X, SIZE_TO_WIN)) {
                System.out.println("Победил человек");
                break;
            }
            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }
            aiTurn();
            printGameMap();
            if (checkWin(DOT_O, SIZE_TO_WIN)) {
                System.out.println("Победил Искуственный Интеллект");
                break;
            }
            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }
        }
        System.out.println("Игра закончена");

    }
    private static void outArr(int[][] myArr){
        for (int[]arr:myArr) {
            System.out.println(Arrays.toString(arr));
        }
    }

    private static void aiTurn() {
        for (int i = 0; i < gameMap.length; i++) {
            for (int j = 0; j < gameMap[i].length; j++) {
                if (gameMap[i][j] == DOT_EMPTY) {
                    gameMap[i][j] = DOT_X;
                    defStrategy();
                    gameMap[i][j] = DOT_O;
                    attackStrategy();
                    gameMap[i][j] = DOT_EMPTY;
                } else scoreMap[i][j] = 0;
            }
        }

        int turAiX = 0;
        int turnAiY = 0;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                scoreMap[i][j] = scoreMapDef[i][j] + scoreMapAttack[i][j];
                if (gameMap[i][j] != DOT_EMPTY)
                    scoreMap[i][j] =  -10;
                if (scoreMap[i][j] > max) {
                    max = scoreMap[i][j];
                    turAiX = i;
                    turnAiY = j;
                }
            }
        }
//        for (int[]arr:scoreMapDef) {
//            System.out.println(Arrays.toString(arr));
//        }
//        for (int[]arr:scoreMapAttack) {
//            System.out.println(Arrays.toString(arr));
//        }
//        for (int[]arr:scoreMap) {
//            System.out.println(Arrays.toString(arr));
//        }
        gameMap[turAiX][turnAiY] = DOT_O;

    }

    private static void attackStrategy() {
        for (int i = 1; i <= SIZE_TO_WIN; i++) {
            checkWin(DOT_O, i);
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++)
                scoreMapAttack[i][j] = scoreMap[i][j];
        }
    }

    private static void defStrategy() {
        for (int i = 1; i <= SIZE_TO_WIN; i++) {
            checkWin(DOT_X, i);
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++)
                scoreMapDef[i][j] = scoreMap[i][j];
        }
    }

    private static int elevateScore(char symb, int winStack) {
        int defScore1 = 0;
        int defScore2 = 0;
        int defScore3 = 0;
        int defScore4 = 0;
        switch (symb) {
            case DOT_O: {
                defScore1 = 10;
                defScore2 = 100;
                defScore3 = 130;
                defScore4 = 10000;
                break;
            }
            case DOT_X: {
                defScore1 = 10;
                defScore2 = 60;
                defScore3 = 100;
                defScore4 = 500;
                break;
            }
            default: {
            }
        }
        switch (winStack) {
            case 1 -> {
                return defScore1;
            }
            case 2 -> {
                return defScore2;
            }
            case 3 -> {
                return defScore3;
            }
            case 4 -> {
                return defScore4;
            }
            default ->{
                return 0;
            }
        }
    }

    private static boolean isMapFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameMap[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

    private static boolean checkWin(char symb, int winStack) {
        //должен работать на полях с настраеваемой размерностью не только 5 на 5
        //алгортм для строк и столбцов
        int winStrikeCol;
        int winStrikeStr;
        for (int i = 0; i < SIZE; i++) {
            winStrikeStr = 0;
            winStrikeCol = 0;
            for (int j = 0; j < SIZE; j++) {
                //if(j==(SIZE_TO_WIN-1)) break;
                if (gameMap[i][j] == symb) {
                    winStrikeStr++;
                    if (winStrikeStr == winStack) {
                        scoreMap[i][j] = elevateScore(symb, winStack);
                        return true;
                    }
                } else {
                    winStrikeStr = 0;
                }
                if (gameMap[j][i] == symb) {
                    winStrikeCol++;
                    if (winStrikeCol == winStack) {
                        scoreMap[i][j] = elevateScore(symb, winStack);
                        return true;
                    }
                } else {
                    winStrikeCol = 0;
                }
            }
        }
        //алгоритм для диагоналей, мне кажется еще можно оптимизировать... потом посмотрю если останется время
        int winStrikeExtendBackslashHi = 0;
        int winStrikeExtendSlashHi = 0;
        int winStrikeExtendBackslashLow = 0;
        int winStrikeExtendSlashLow = 0;
        for (int k = 0; k <= (SIZE - winStack); k++) {//для того что бы определить кол-во доп диагоналей
            for (int i = 0; i < SIZE - k; i++) {
                //для диагоналей расположенных "выше" главной и побочной
                //для главной и побочной считается дважды, при к=0. потом разберусь..
                if (gameMap[i][i + k] == symb) {
                    winStrikeExtendBackslashHi++;
                    if (winStrikeExtendBackslashHi == winStack) {
                        scoreMap[i][i + k] = elevateScore(symb, winStack);
                        return true;
                    }
                } else {
                    winStrikeExtendBackslashHi = 0;
                }
                if (gameMap[i][SIZE - (i + 1 + k)] == symb) {
                    winStrikeExtendSlashHi++;
                    if (winStrikeExtendSlashHi == winStack) {
                        scoreMap[i][SIZE - (i + 1 + k)] = elevateScore(symb, winStack);
                        return true;
                    }
                } else {
                    winStrikeExtendSlashHi = 0;
                }
                //для диагоналей расположенных "ниже" главной и побочной
                if (gameMap[i + k][i] == symb) {
                    winStrikeExtendBackslashLow++;
                    if (winStrikeExtendBackslashLow == winStack) {
                        scoreMap[i + k][i] = elevateScore(symb, winStack);
                        return true;
                    }
                } else {
                    winStrikeExtendBackslashLow = 0;
                }
                if (gameMap[i + k][SIZE - (i + 1)] == symb) {
                    winStrikeExtendSlashLow++;
                    if (winStrikeExtendSlashLow == winStack) {
                        scoreMap[i + k][SIZE - (i + 1)] = elevateScore(symb, winStack);
                        return true;
                    }
                } else {
                    winStrikeExtendSlashLow = 0;
                }
            }
        }

        return false;
    }

    private static void humanTurn() {
        int x, y;
        do {
            System.out.println("Введите координаты в формате X Y");
            try {
                x = scanner.nextInt() - 1;
                y = scanner.nextInt() - 1;
            } catch (InputMismatchException e) {
                System.out.println("Неверный формат");
                scanner.nextLine();
                x = -1;
                y = -1;
            }
        } while (!isCellValid(x, y)); // while(isCellValid(x, y) == false)
        gameMap[y][x] = DOT_X;
    }

    public static boolean isCellValid(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) return false;
        return gameMap[y][x] == DOT_EMPTY;
    }

    private static void printGameMap() {

        for (int i = 0; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(gameMap[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initGameMap() {
        gameMap = new char[SIZE][SIZE];
        scoreMap = new int[SIZE][SIZE];
        scoreMapAttack = new int[SIZE][SIZE];
        scoreMapDef = new int[SIZE][SIZE];
        for (char[] chars : gameMap) {
            Arrays.fill(chars, DOT_EMPTY);
        }
        printGameMap();
    }

}
