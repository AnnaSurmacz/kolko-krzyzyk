package kolkokrzyzyk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTestSuite {
    @Test
    void shouldWinOInRow() {
        // given
        GameBoard board = new GameBoard(3,3);
        board.setMove(0, 0, 'O');
        board.setMove(0, 1, 'O');
        board.setMove(0, 2, 'O');
        // when
        boolean result = board.checkWin('O');
        // then
        assertTrue(result);
    }

    @Test
    void shouldWinOInColumn() {
        // given
        GameBoard board = new GameBoard(3,3);
        board.setMove(0, 1, 'O');
        board.setMove(1, 1, 'O');
        board.setMove(2, 1, 'O');
        // when
        boolean result = board.checkWin('O');
        // then
        assertTrue(result);
    }

    @Test
    void shouldWinOInDiagonal() {
        // given
        GameBoard board = new GameBoard(3,3);
        board.setMove(0, 0, 'O');
        board.setMove(1, 1, 'O');
        board.setMove(2, 2, 'O');
        // when
        boolean result = board.checkWin('O');
        // then
        assertTrue(result);
    }


    @Test
    void shouldWinXInRow() {
        // given
        GameBoard board = new GameBoard(3,3);
        board.setMove(0, 0, 'X');
        board.setMove(0, 1, 'X');
        board.setMove(0, 2, 'X');
        // when
        boolean result = board.checkWin('X');
        // then
        assertTrue(result);
    }

    @Test
    void shouldWinXInColumn() {
        // given
        GameBoard board = new GameBoard(3,3);
        board.setMove(0, 1, 'X');
        board.setMove(1, 1, 'X');
        board.setMove(2, 1, 'X');
        // when
        boolean result = board.checkWin('X');
        // then
        assertTrue(result);
    }


    @Test
    void shouldWinXInDiagonal() {
        // given
        GameBoard board = new GameBoard(3,3);
        board.setMove(0, 0, 'X');
        board.setMove(1, 1, 'X');
        board.setMove(2, 2, 'X');
        // when
        boolean result = board.checkWin('X');
        // then
        assertTrue(result);
    }

    @Test
    void shouldRecognizeDrawSituation() {
        // given
        GameBoard board = new GameBoard(3,3);
        board.setMove(0, 0, 'O');
        board.setMove(0, 1, 'X');
        board.setMove(0, 2, 'O');
        board.setMove(1, 0, 'O');
        board.setMove(1, 1, 'X');
        board.setMove(1, 2, 'X');
        board.setMove(2, 0, 'X');
        board.setMove(2, 1, 'O');
        board.setMove(2, 2, 'O');
        // when
        boolean winO = board.checkWin('O');
        boolean winX = board.checkWin('X');
        boolean isFull = board.isFull();
        // then
        assertFalse(winO);
        assertFalse(winX);
        assertTrue(isFull);
    }

    @Test
    void shouldReturnFalseWhenMoveOnTakenField() {
        // given
        GameBoard board = new GameBoard(3,3);
        board.setMove(1, 1, 'X');
        // when
        boolean result = board.setMove(1, 1, 'O');
        // then
        assertFalse(result);
    }
}

