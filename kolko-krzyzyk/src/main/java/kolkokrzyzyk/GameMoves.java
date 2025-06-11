package kolkokrzyzyk;

import java.util.Scanner;

public class GameMoves {
    private Scanner scanner = new Scanner(System.in);

    public int[] getPlayerMove(String playerName, int size) {
        while (true) {
            System.out.print("Graczu " + playerName + ", podaj ruch (np. 2b): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (isValidInput(input, size)) {
                int row = Character.getNumericValue(input.charAt(0)) - 1;
                int col = input.charAt(1) - 'a';
                return new int[] {row, col};
            } else {
                System.out.println("Błędny format ruchu! Spróbuj jeszcze raz.");
            }
        }
    }
    private boolean isValidInput(String move, int size) {
        if (move.length() != 2) return false;
        char rowChar = move.charAt(0);
        char colChar = move.charAt(1);

        if (rowChar < '1' || rowChar > (char)('0' + size)) return false;

        if (colChar < 'a' || colChar >= (char)('a' + size)) return false;

        return true;
    }
}