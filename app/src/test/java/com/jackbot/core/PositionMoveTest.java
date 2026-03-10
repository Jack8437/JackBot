package com.jackbot.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PositionMoveTest {
  /** Fools mate position */
  private static String FEN_FOOLS_MATE =
      "rnbqk2r/pppp1ppp/5n2/4p2Q/1bB1P3/8/PPPP1PPP/RNB1K1NR w KQkq - 0 1";

  @Test()
  void testBasicPieceMoveAndUndo() {
    Position startingBoard = Fen.parse(Fen.FEN_STARTING_POSITION);
    Position expectedMove = Fen.parse("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR b KQkq - 0 1");
    Position expectedUndo = Fen.parse(Fen.FEN_STARTING_POSITION);
    Move testMove = Move.of(8, 16);
    startingBoard.makeMove(testMove);
    assertTrue(startingBoard.equals(expectedMove));
    startingBoard.undoMove(testMove);
    assertTrue(startingBoard.equals(expectedUndo));
  }

  @Test
  void testEnPassantPossibilityMoveAndUndo() {
    Position startingBoard = Fen.parse(Fen.FEN_STARTING_POSITION);
    assertEquals(-1, startingBoard.getEpSquare());
    Position expectedMove = Fen.parse("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq a3 0 1");
    Position expectedUndo = Fen.parse(Fen.FEN_STARTING_POSITION);
    Move testMove = Move.doublePawnPush(8, 24);
    startingBoard.makeMove(testMove);
    assertTrue(startingBoard.equals(expectedMove));
    startingBoard.undoMove(testMove);
    assertTrue(startingBoard.equals(expectedUndo));
    startingBoard.makeMove(testMove);
    testMove = Move.doublePawnPush(48, 32);
    startingBoard.makeMove(testMove);
    expectedMove = Fen.parse("rnbqkbnr/1ppppppp/8/p7/P7/8/1PPPPPPP/RNBQKBNR w KQkq a6 0 2");
    assertTrue(startingBoard.equals(expectedMove));
    testMove = Move.of(1, 16);
    startingBoard.makeMove(testMove);
    expectedMove = Fen.parse("rnbqkbnr/1ppppppp/8/p7/P7/N7/1PPPPPPP/R1BQKBNR b KQkq - 1 2");
    assertTrue(startingBoard.equals(expectedMove));
  }

  @Test
  void testBasicCaptureAndUndo() {
    Position startingPosition = Fen.parse(FEN_FOOLS_MATE);
    Position expectedMove =
        Fen.parse("rnbqk2r/pppp1Qpp/5n2/4p3/1bB1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");
    Position expectedUndo = Fen.parse(FEN_FOOLS_MATE);
    Move testMove = Move.capture(39, 53);
    startingPosition.makeMove(testMove);
    assertTrue(startingPosition.equals(expectedMove));
    startingPosition.undoMove(testMove);
    System.out.println(startingPosition.toString() + expectedUndo.toString());
    assertTrue(startingPosition.equals(expectedUndo));
  }

  @Test
  void testHalfmoveClock() {
    Position startingBoard = Fen.parse(Fen.FEN_STARTING_POSITION);
    Position expectedMove =
        Fen.parse("rnbqkb1r/pppppppp/5n2/8/7N/8/PPPPPPPP/RNBQKB1R b KQkq - 3 2");
    Move testMove = Move.of(6, 21);
    startingBoard.makeMove(testMove);
    testMove = Move.of(62, 45);
    startingBoard.makeMove(testMove);
    testMove = Move.of(21, 31);
    startingBoard.makeMove(testMove);
    assertTrue(expectedMove.equals(startingBoard));
    testMove = Move.of(48, 40);
    startingBoard.makeMove(testMove);
    expectedMove = Fen.parse("rnbqkb1r/1ppppppp/p4n2/8/7N/8/PPPPPPPP/RNBQKB1R w KQkq - 0 3");
    assertTrue(expectedMove.equals(startingBoard));
  }

  @Test
  void testKingMoveRemovingCasltingRights() {
    Position startingBoard = Fen.parse(Fen.FEN_STARTING_POSITION);
    Position expectedMove = Fen.parse("rnbq1bnr/ppppkppp/8/4p3/4P3/8/PPPPKPPP/RNBQ1BNR w - - 2 3");
    Position expectedUndo =
        Fen.parse("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2");
    Move testMove = Move.doublePawnPush(12, 28);
    startingBoard.makeMove(testMove);
    testMove = Move.doublePawnPush(52, 36);
    startingBoard.makeMove(testMove);
    testMove = Move.of(4, 12);
    startingBoard.makeMove(testMove);
    testMove = Move.of(60, 52);
    startingBoard.makeMove(testMove);
    assertTrue(expectedMove.equals(startingBoard));
    startingBoard.undoMove(testMove);
    testMove = Move.of(4, 12);
    startingBoard.undoMove(testMove);
    assertTrue(expectedUndo.equals(startingBoard));
  }

  @Test
  void testRookMoveAndUndoRemovingCastlingRights() {
    Position startingBoard = Fen.parse(Fen.FEN_STARTING_POSITION);
    Position expectedMove = Fen.parse("1nbqkbnr/rppppppp/8/p7/7P/8/PPPPPPPR/RNBQKBN1 w Qk - 2 3");
    Position expectedUndo =
        Fen.parse("rnbqkbnr/1ppppppp/8/p7/7P/8/PPPPPPP1/RNBQKBNR w KQkq a6 0 2");
    Move testMove = Move.doublePawnPush(15, 31);
    startingBoard.makeMove(testMove);
    testMove = Move.doublePawnPush(48, 32);
    startingBoard.makeMove(testMove);
    testMove = Move.of(7, 15);
    startingBoard.makeMove(testMove);
    testMove = Move.of(56, 48);
    startingBoard.makeMove(testMove);
    assertTrue(expectedMove.equals(startingBoard));
    startingBoard.undoMove(testMove);
    testMove = Move.of(7, 15);
    startingBoard.undoMove(testMove);
    System.out.println(expectedUndo.toString() + startingBoard.toString());
    assertTrue(expectedUndo.equals(startingBoard));
  }

  @Test
  void testRookCaptureRemovingCastlingRights() {
    Position startingPosition =
        Fen.parse("rn1qkbnr/pbpppp1p/6p1/1p6/1P6/6P1/PBPPPP1P/RN1QKBNR w KQkq - 0 4");
    Position expectedMove =
        Fen.parse("rn1qkbnB/p1pppp1p/6p1/1p6/1P6/6P1/P1PPPP1P/RN1QKBNb w Qq - 0 5");
    Position expectedUndo =
        Fen.parse("rn1qkbnr/pbpppp1p/6p1/1p6/1P6/6P1/PBPPPP1P/RN1QKBNR w KQkq - 0 4");
    Move testMove = Move.capture(9, 63);
    startingPosition.makeMove(testMove);
    testMove = Move.capture(49, 7);
    startingPosition.makeMove(testMove);
    assertTrue(expectedMove.equals(startingPosition));
    startingPosition.undoMove(testMove);
    testMove = Move.capture(9, 63);
    startingPosition.undoMove(testMove);
    assertTrue(expectedUndo.equals(startingPosition));
  }

  @Test
  void testCastlingAndUndoingCastling() {
    Position startingBoard = Fen.parse(Fen.FEN_STARTING_POSITION);
    Position expectedMove =
        Fen.parse("rnbq1rk1/pppp1ppp/3b1n2/4p3/3P4/2NQB3/PPP1PPPP/2KR1BNR b - - 7 5");
    Position expectedUndo =
        Fen.parse("rnbqk2r/pppp1ppp/3b1n2/4p3/3P4/2NQB3/PPP1PPPP/R3KBNR b KQkq - 5 4");
    Move testMove = Move.doublePawnPush(11, 27);
    startingBoard.makeMove(testMove);
    testMove = Move.doublePawnPush(52, 36);
    startingBoard.makeMove(testMove);
    testMove = Move.of(2, 20);
    startingBoard.makeMove(testMove);
    testMove = Move.of(61, 43);
    startingBoard.makeMove(testMove);
    testMove = Move.of(1, 18);
    startingBoard.makeMove(testMove);
    testMove = Move.of(62, 45);
    startingBoard.makeMove(testMove);
    testMove = Move.of(3, 19);
    startingBoard.makeMove(testMove);
    testMove = Move.castle(60, 62);
    startingBoard.makeMove(testMove);
    testMove = Move.castle(4, 2);
    startingBoard.makeMove(testMove);
    assertTrue(expectedMove.equals(startingBoard));
    startingBoard.undoMove(testMove);
    testMove = Move.castle(60, 62);
    startingBoard.undoMove(testMove);
    assertTrue(expectedUndo.equals(startingBoard));
  }

  @Test
  void testBasicPromotionAndUndo() {
    Position startingPosition = Fen.parse("4k3/P7/8/8/8/8/p7/4K3 w - - 0 50");
    Position expectedMove = Fen.parse("N3k3/8/8/8/8/8/8/b3K3 w - - 0 51");
    Position expectedUndo = Fen.parse("4k3/P7/8/8/8/8/p7/4K3 w - - 0 50");
    Move testMove = Move.promotion(48, 56, Move.PROMO_N, false);
    startingPosition.makeMove(testMove);
    testMove = Move.promotion(8, 0, Move.PROMO_B, false);
    startingPosition.makeMove(testMove);
    assertTrue(expectedMove.equals(startingPosition));
    startingPosition.undoMove(testMove);
    testMove = Move.promotion(48, 56, Move.PROMO_N, false);
    startingPosition.undoMove(testMove);
    System.out.println(expectedUndo.toString() + startingPosition.toString());
    assertTrue(expectedUndo.equals(startingPosition));
  }

  @Test
  void testCapturePromotionAndUndo() {
    Position startingPosition = Fen.parse("1p2k3/P7/8/8/8/8/p7/1P2K3 w - - 0 50");
    Position expectedMove = Fen.parse("1N2k3/8/8/8/8/8/8/1b2K3 w - - 0 51");
    Position expectedUndo = Fen.parse("1p2k3/P7/8/8/8/8/p7/1P2K3 w - - 0 50");
    Move testMove = Move.promotion(48, 57, Move.PROMO_N, true);
    startingPosition.makeMove(testMove);
    testMove = Move.promotion(8, 1, Move.PROMO_B, true);
    startingPosition.makeMove(testMove);
    System.out.println(expectedMove.toString() + startingPosition.toString());
    assertTrue(expectedMove.equals(startingPosition));
    startingPosition.undoMove(testMove);
    testMove = Move.promotion(48, 57, Move.PROMO_N, true);
    startingPosition.undoMove(testMove);
    assertTrue(expectedUndo.equals(startingPosition));
  }

  @Test
  void testEnPassantPlayedMoveAndUndo() {
    Position startingPosition =
        Fen.parse("r1bqkbnr/ppp1pppp/2n5/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3");
    Position expectedMove =
        Fen.parse("r1bqkbnr/ppp1pppp/2nP4/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 3");
    Position expectedUndo =
        Fen.parse("r1bqkbnr/ppp1pppp/2n5/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3");
    Move testMove = Move.enPassant(36, 43);
    startingPosition.makeMove(testMove);
    assertTrue(expectedMove.equals(startingPosition));
    startingPosition.undoMove(testMove);
    assertTrue(expectedUndo.equals(startingPosition));
  }
}
