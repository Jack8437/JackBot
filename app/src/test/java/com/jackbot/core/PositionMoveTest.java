package com.jackbot.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PositionMoveTest {
  private static String FEN_FOOLS_MATE =
      "rnbqk2r/pppp1ppp/5n2/4p2Q/1bB1P3/8/PPPP1PPP/RNB1K1NR w KQkq - 0 1";

  @Test()
  void testBasicPieceMoveAndUndo() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    Position expectedMove =
        Position.fromFEN("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR b KQkq - 0 1");
    Position expectedUndo = Position.fromFEN(Position.FEN_STARTING_POSITION);
    Move testMove = Move.of(8, 16);
    startingBoard.makeMove(testMove);
    assertTrue(startingBoard.equals(expectedMove));
    startingBoard.undoMove(testMove);
    assertTrue(startingBoard.equals(expectedUndo));
  }

  @Test
  void testEnPassantMoveAndUndo() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    assertEquals(-1, startingBoard.getEpSquare());
    Position expectedMove =
        Position.fromFEN("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq a3 0 1");
    Position expectedUndo = Position.fromFEN(Position.FEN_STARTING_POSITION);
    Move testMove = Move.doublePawnPush(8, 24);
    startingBoard.makeMove(testMove);
    assertTrue(startingBoard.equals(expectedMove));
    startingBoard.undoMove(testMove);
    assertTrue(startingBoard.equals(expectedUndo));
    startingBoard.makeMove(testMove);
    testMove = Move.doublePawnPush(48, 32);
    startingBoard.makeMove(testMove);
    expectedMove = Position.fromFEN("rnbqkbnr/1ppppppp/8/p7/P7/8/1PPPPPPP/RNBQKBNR w KQkq a6 0 2");
    assertTrue(startingBoard.equals(expectedMove));
    testMove = Move.of(1, 16);
    startingBoard.makeMove(testMove);
    expectedMove = Position.fromFEN("rnbqkbnr/1ppppppp/8/p7/P7/N7/1PPPPPPP/R1BQKBNR b KQkq - 1 2");
    assertTrue(startingBoard.equals(expectedMove));
  }

  @Test
  void testBasicCaptureAndUndo() {
    Position startingPosition = Position.fromFEN(FEN_FOOLS_MATE);
    Position expectedMove =
        Position.fromFEN("rnbqk2r/pppp1Qpp/5n2/4p3/1bB1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");
    Position expectedUndo = Position.fromFEN(FEN_FOOLS_MATE);
    Move testMove = Move.capture(39, 53);
    startingPosition.makeMove(testMove);
    assertTrue(startingPosition.equals(expectedMove));
    startingPosition.undoMove(testMove);
    System.out.println(startingPosition.toString() + expectedUndo.toString());
    assertTrue(startingPosition.equals(expectedUndo));
  }

  @Test
  void testHalfmoveClock() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    Position expectedMove =
        Position.fromFEN("rnbqkb1r/pppppppp/5n2/8/7N/8/PPPPPPPP/RNBQKB1R b KQkq - 3 2");
    Move testMove = Move.of(6, 21);
    startingBoard.makeMove(testMove);
    testMove = Move.of(62, 45);
    startingBoard.makeMove(testMove);
    testMove = Move.of(21, 31);
    startingBoard.makeMove(testMove);
    assertTrue(expectedMove.equals(startingBoard));
    testMove = Move.of(48, 40);
    startingBoard.makeMove(testMove);
    expectedMove = Position.fromFEN("rnbqkb1r/1ppppppp/p4n2/8/7N/8/PPPPPPPP/RNBQKB1R w KQkq - 0 3");
    assertTrue(expectedMove.equals(startingBoard));
  }
}
