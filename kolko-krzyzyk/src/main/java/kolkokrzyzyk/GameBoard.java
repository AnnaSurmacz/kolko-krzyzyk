package kolkokrzyzyk;

public class GameBoard {

    private char[][] board;
    private int size;
    private int requiredInLine;

    public GameBoard(int size, int requiredInLine) {
        this.size = size;
        this.requiredInLine = requiredInLine;
        board = new char[size][size];
        clearBoard();
    }

    public void clearBoard() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = ' ';
    }

    public void displayBoard() {
        // wyświetlanie nagłówka tablicy dla 3x3 i 10x10
        System.out.print("   ");
        for (int j = 0; j < size; j++) {
            System.out.print("  " + (char)('a' + j) + " ");
        }
        System.out.println();
        for (int i = 0; i < size; i++) {
            if (i + 1 < 10) System.out.print((i + 1) + "  |");
            else System.out.print((i + 1) + " |");
            for (int j = 0; j < size; j++) {
                System.out.print(" " + board[i][j] + " |");
            }
            System.out.println();
        }
    }

    public boolean setMove(int row, int col, char symbol) {
        if (board[row][col] == ' ') {
            board[row][col] = symbol;
            return true;
        }
        return false;
    }

    public boolean isFull() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board[i][j] == ' ')
                    return false;
        return true;
    }

    public boolean checkWin(char symbol) {
        if (size == 3 && requiredInLine == 3) {
            for (int i = 0; i < 3; i++) {
                if ((board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) ||
                        (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)) {
                    return true;
                }
            }
            if ((board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                    (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)) {
                return true;
            }
            return false;
        } else if (size == 10 && requiredInLine == 5) {
            return checkWin10x10(symbol);
        }
        return false;
    }

    private boolean checkWin10x10(char symbol) {
        // w poziomie
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j <= 10 - 5; j++) {
                if (board[i][j] == symbol && board[i][j+1] == symbol && board[i][j+2] == symbol
                        && board[i][j+3] == symbol && board[i][j+4] == symbol) {
                    return true;
                }
            }
        }
        // w pionie
        for (int i = 0; i <= 10 - 5; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == symbol && board[i+1][j] == symbol && board[i+2][j] == symbol
                        && board[i+3][j] == symbol && board[i+4][j] == symbol) {
                    return true;
                }
            }
        }
        // po przekątnych
        for (int i = 0; i <= 10 - 5; i++) {
            for (int j = 0; j <= 10 - 5; j++) {
                if (board[i][j] == symbol && board[i+1][j+1] == symbol && board[i+2][j+2] == symbol
                        && board[i+3][j+3] == symbol && board[i+4][j+4] == symbol) {
                    return true;
                }
            }
        }
        // po przekątnych w drugą stronę
        for (int i = 0; i <= 10 - 5; i++) {
            for (int j = 4; j < 10; j++) {
                if (board[i][j] == symbol && board[i+1][j-1] == symbol && board[i+2][j-2] == symbol
                        && board[i+3][j-3] == symbol && board[i+4][j-4] == symbol) {
                    return true;
                }
            }
        }
        return false;
    }

    public char getField(int row, int col) {
        return board[row][col];
    }

    public char[][] getBoard() {
        return board;
    }

}