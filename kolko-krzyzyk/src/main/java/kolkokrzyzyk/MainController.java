package kolkokrzyzyk;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class MainController {

    @FXML private Button saveButton;
    @FXML private Button newGameButton;
    @FXML private Button exitButton;
    @FXML private GridPane gameGrid;

    private Button[][] buttons;
    private GameBoard gameBoard;
    private SmartComputerMoves computerMoves;
    private char currentPlayer = 'X';
    private boolean vsComputer = false;
    private int size = 3;
    private int requiredInLine = 3;

    private static final String SAVE_FILE = "savegame.ser";

    // Stan gry do wczytywania/zapisywania
    private SavedGame savedGame;

    @FXML
    public void initialize() {
        // Sprawdzamy czy istnieje zapisany stan gry
        if (Files.exists(Paths.get(SAVE_FILE))) {
            askLoadOrNewGame();
        } else {
            showSetupDialogsAndStart();
        }
    }

    private void askLoadOrNewGame() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(
                "Nowa gra", FXCollections.observableArrayList("Nowa gra", "Wczytaj zapis")
        );
        dialog.setTitle("Wczytaj grę?");
        dialog.setHeaderText("Znaleziono zapis gry.");
        dialog.setContentText("Co chcesz zrobić?");
        String choice = dialog.showAndWait().orElse("Nowa gra");
        if (choice.equals("Wczytaj zapis")) {
            loadSavedGame();
        } else {
            showSetupDialogsAndStart();
            currentPlayer = 'X';
        }
    }

    private void showSetupDialogsAndStart() {
        // jaki rozmar planszy?
        ChoiceDialog<String> sizeDialog = new ChoiceDialog<>(
                "3x3", FXCollections.observableArrayList("3x3", "10x10")
        );
        sizeDialog.setTitle("Wybór planszy");
        sizeDialog.setHeaderText("Wybierz rozmiar planszy:");
        sizeDialog.setContentText("Rozmiar:");
        String sizeChoice = sizeDialog.showAndWait().orElse("3x3");
        size = sizeChoice.equals("10x10") ? 10 : 3;
        requiredInLine = size == 10 ? 5 : 3;


        ChoiceDialog<String> playerDialog = new ChoiceDialog<>(
                "Człowiek vs Człowiek",
                FXCollections.observableArrayList("Człowiek vs Człowiek", "Człowiek vs Komputer")
        );
        playerDialog.setTitle("Wybór trybu gry");
        playerDialog.setHeaderText("Wybierz tryb gry:");
        playerDialog.setContentText("Tryb:");
        String playerChoice = playerDialog.showAndWait().orElse("Człowiek vs Człowiek");
        vsComputer = playerChoice.equals("Człowiek vs Komputer");

        buttons = new Button[size][size];
        gameBoard = new GameBoard(size, requiredInLine);
        computerMoves = new SmartComputerMoves();

        generateBoard(size);
    }

    private void generateBoard(int size) {
        gameGrid.getChildren().clear();
        gameGrid.getColumnConstraints().clear();
        gameGrid.getRowConstraints().clear();

        double boardPixelSize = 400.0;
        double buttonSize = boardPixelSize / size;
        double fontSize = buttonSize / 3.5;

        gameGrid.setPrefSize(boardPixelSize, boardPixelSize);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Button cell = new Button();
                cell.setPrefSize(buttonSize, buttonSize);
                cell.setStyle("-fx-font-size: " + fontSize + "px;");
                final int r = row;
                final int c = col;
                cell.setOnAction(e -> handleMove(r, c));
                buttons[row][col] = cell;
                gameGrid.add(cell, col, row);
            }
        }
    }

    private void handleMove(int row, int col) {
        if (gameBoard.setMove(row, col, currentPlayer)) {
            buttons[row][col].setText(String.valueOf(currentPlayer));
            buttons[row][col].setDisable(true);

            if (gameBoard.checkWin(currentPlayer)) {
                showAlert("Wygrana!", "Gracz " + currentPlayer + " wygrywa!");
                disableAllButtons();
            } else if (gameBoard.isFull()) {
                showAlert("Remis", "Brak wolnych pól. Remis!");
                disableAllButtons();
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                if (vsComputer && currentPlayer == 'O') {
                    makeComputerMove();
                }
            }
        }
    }

    private void makeComputerMove() {
        char[][] boardCopy = copyBoard(gameBoard.getBoard());
        int[] move = computerMoves.chooseMove(boardCopy, 'O', 'X');
        if (move != null) {
            handleMove(move[0], move[1]);
        }
    }

    private char[][] copyBoard(char[][] board) {
        int n = board.length;
        char[][] copy = new char[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, n);
        }
        return copy;
    }

    private void disableAllButtons() {
        for (Button[] row : buttons) {
            for (Button btn : row) {
                btn.setDisable(true);
            }
        }
    }

    @FXML
    private void handleSaveGame() {
        SavedGame toSave = new SavedGame(gameBoard.getBoard(), currentPlayer, vsComputer, size, requiredInLine);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(toSave);
            showAlert("Zapis gry", "Stan gry został zapisany!");
        } catch (IOException e) {
            showAlert("Błąd zapisu", "Nie udało się zapisać gry: " + e.getMessage());
        }
    }

    @FXML
    private void handleNewGame() {
        if (Files.exists(Paths.get(SAVE_FILE))) {
            askLoadOrNewGame();
        } else {
            showSetupDialogsAndStart();
            currentPlayer = 'X';
        }
    }

    private void loadSavedGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            savedGame = (SavedGame) ois.readObject();
            // Przywracamy stan gry
            size = savedGame.size;
            requiredInLine = savedGame.requiredInLine;
            vsComputer = savedGame.vsComputer;
            currentPlayer = savedGame.currentPlayer;
            buttons = new Button[size][size];
            gameBoard = new GameBoard(size, requiredInLine);
            computerMoves = new SmartComputerMoves();
            generateBoard(size);

            // Odzwierciedlenie stanu na planszy
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    gameBoard.getBoard()[i][j] = savedGame.board[i][j];
                    buttons[i][j].setText(savedGame.board[i][j] == ' ' ? "" : String.valueOf(savedGame.board[i][j]));
                    buttons[i][j].setDisable(
                            savedGame.board[i][j] != ' ' ||
                                    gameBoard.checkWin('X') ||
                                    gameBoard.checkWin('O')
                    );
                }
            }
            showAlert("Wczytano grę", "Przywrócono ostatni zapisany stan gry!");

// Jeśli komputer miałby ruszać zaraz po wznowieniu gry, wykonaj ruch
            if (vsComputer && currentPlayer == 'O') {
                makeComputerMove();
            }
        } catch (Exception e) {
            showAlert("Błąd odczytu", "Nie udało się wczytać gry: " + e.getMessage());
            showSetupDialogsAndStart();
        }
    }

    @FXML
    private void handleExitGame() {
        System.exit(0);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class SavedGame implements Serializable {
        private static final long serialVersionUID = 1L;
        char[][] board;
        char currentPlayer;
        boolean vsComputer;
        int size;
        int requiredInLine;

        SavedGame(char[][] board, char currentPlayer, boolean vsComputer, int size, int requiredInLine) {
            this.size = size;
            this.requiredInLine = requiredInLine;
            this.board = new char[size][size];
            for (int i = 0; i < size; i++) {
                this.board[i] = board[i].clone();
            }
            this.currentPlayer = currentPlayer;
            this.vsComputer = vsComputer;
        }
    }
}
