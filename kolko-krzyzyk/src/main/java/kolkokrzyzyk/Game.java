package kolkokrzyzyk;

import java.util.Scanner;

public class Game {
    private GameBoard board;
    private GameMoves moves = new GameMoves();
    private SmartComputerMoves smartComputer = new SmartComputerMoves();

    public void start() {
        Scanner scanner = new Scanner(System.in);

        boolean playAgain = true;
        while (playAgain) {
            // Wybór planszy
            int size = 3;
            int requiredInLine = 3;
            System.out.println("Wybierz planszę:");
            System.out.println("1 - 3x3 (wygraj 3 w linii)");
            System.out.println("2 - 10x10 (wygraj 5 w linii)");
            String wyborPlanszy = scanner.nextLine().trim();

            if (wyborPlanszy.equals("2")) {
                size = 10;
                requiredInLine = 5;
            }
            board = new GameBoard(size, requiredInLine);


            int liczbaGraczy = 0;
            while (liczbaGraczy != 1 && liczbaGraczy != 2) {
                System.out.print("Wybierz liczbę graczy (1 – grasz z komputerem, 2 – dwóch graczy): ");
                String line = scanner.nextLine().trim();
                try {
                    liczbaGraczy = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    liczbaGraczy = 0;
                }
            }

            String playerX = "Gracz X";
            String playerO = (liczbaGraczy == 1) ? "Komputer" : "Gracz O";
            String currentPlayer = playerX;
            char currentSymbol = 'X';

            boolean isGameOver = false;

            board.clearBoard();
            board.displayBoard();

            while (!isGameOver) {
                int[] move;
                if (liczbaGraczy == 1 && currentPlayer.equals("Komputer")) {

                    move = smartComputer.chooseMove(
                            board.getBoard(),
                            'O',
                            'X'
                    );
                    if (move == null) {

                        System.out.println("Brak możliwych ruchów!");
                        break;
                    }
                    System.out.println("Komputer wybiera ruch: " + (move[0] + 1) + (char) ('a' + move[1]));
                } else {
                    move = moves.getPlayerMove(currentPlayer, board.getBoard().length);
                }

                int row = move[0];
                int col = move[1];

                if (!board.setMove(row, col, currentSymbol)) {
                    if (!(liczbaGraczy == 1 && currentPlayer.equals("Komputer"))) {
                        System.out.println("To pole jest już zajęte! Spróbuj jeszcze raz.");
                    }
                    continue;
                }

                board.displayBoard();

                if (board.checkWin(currentSymbol)) {
                    if (liczbaGraczy == 1 && currentPlayer.equals("Komputer")) {
                        System.out.println("Komputer wygrywa!");
                    } else {
                        System.out.println("Gratulacje! Wygrał " + currentPlayer + "!");
                    }
                    isGameOver = true;
                } else if (board.isFull()) {
                    System.out.println("Remis!");
                    isGameOver = true;
                } else {
                    if (currentPlayer.equals(playerX)) {
                        currentPlayer = playerO;
                        currentSymbol = 'O';
                    } else {
                        currentPlayer = playerX;
                        currentSymbol = 'X';
                    }
                }
            }
            System.out.print("Czy chcesz zagrać jeszcze raz? (t/n): ");
            String odp = scanner.nextLine().trim().toLowerCase();
            if (!odp.equals("t")) {
                playAgain = false;
                System.out.println("Dzięki za grę!");
            }
        }
    }
}