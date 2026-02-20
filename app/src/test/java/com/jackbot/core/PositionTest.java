package com.jackbot.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PositionTest {

  // private static String FEN_FOOLS_MATE =
  // "rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3";
  /** A chess position in FEN notation where en passant is playable. */
  private static String FEN_EN_PASSANT = "8/8/8/3Pp3/4K3/8/8/4k3 w - e6 0 1";

  // private static String FEN_GAME_OF_THE_CENTURY =
  // "r3k2r/pppq1ppp/2npbn2/8/2BPP3/2N2N2/PPP2PPP/R1BQ1RK1 b kq - 4 10";

  @Test
  void testStartingPositionPawns() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    long[] pieces = startingBoard.getPieces();
    // Test white pawns
    assertEquals(Squares.rankMask(Squares.RANK_2), pieces[Pieces.WP]);
    assertEquals(8, Long.bitCount(pieces[Pieces.WP]));
    // Test black pawns
    assertEquals(Squares.rankMask(Squares.RANK_7), pieces[Pieces.BP]);
    assertEquals(8, Long.bitCount(pieces[Pieces.BP]));
  }

  @Test
  void testStartingPositionKnights() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    long[] pieces = startingBoard.getPieces();
    // Test white knights
    assertEquals(
        Squares.bit(Squares.sq(Squares.FILE_B, Squares.RANK_1))
            | Squares.bit(Squares.sq(Squares.FILE_G, Squares.RANK_1)),
        pieces[Pieces.WN]);
    assertEquals(2, Long.bitCount(pieces[Pieces.WN]));
    // Test black knights
    assertEquals(
        Squares.bit(Squares.sq(Squares.FILE_B, Squares.RANK_8))
            | Squares.bit(Squares.sq(Squares.FILE_G, Squares.RANK_8)),
        pieces[Pieces.BN]);
    assertEquals(2, Long.bitCount(pieces[Pieces.BN]));
  }

  @Test
  void testStartingPositionBishops() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    long[] pieces = startingBoard.getPieces();
    // Test white bishops
    assertEquals(
        Squares.bit(Squares.sq(Squares.FILE_C, Squares.RANK_1))
            | Squares.bit(Squares.sq(Squares.FILE_F, Squares.RANK_1)),
        pieces[Pieces.WB]);
    assertEquals(2, Long.bitCount(pieces[Pieces.WB]));
    // Test black bishops
    assertEquals(
        Squares.bit(Squares.sq(Squares.FILE_C, Squares.RANK_8))
            | Squares.bit(Squares.sq(Squares.FILE_F, Squares.RANK_8)),
        pieces[Pieces.BB]);
    assertEquals(2, Long.bitCount(pieces[Pieces.BB]));
  }

  @Test
  void testStartingPositionRooks() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    long[] pieces = startingBoard.getPieces();
    // Test white rooks
    assertEquals(
        Squares.bit(Squares.sq(Squares.FILE_A, Squares.RANK_1))
            | Squares.bit(Squares.sq(Squares.FILE_H, Squares.RANK_1)),
        pieces[Pieces.WR]);
    assertEquals(2, Long.bitCount(pieces[Pieces.WR]));
    // Test black rooks
    assertEquals(
        Squares.bit(Squares.sq(Squares.FILE_A, Squares.RANK_8))
            | Squares.bit(Squares.sq(Squares.FILE_H, Squares.RANK_8)),
        pieces[Pieces.BR]);
    assertEquals(2, Long.bitCount(pieces[Pieces.BR]));
  }

  @Test
  void testStartingPositionQueens() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    long[] pieces = startingBoard.getPieces();
    // Test white queen
    assertEquals(Squares.bit(Squares.sq(Squares.FILE_D, Squares.RANK_1)), pieces[Pieces.WQ]);
    assertEquals(1, Long.bitCount(pieces[Pieces.WQ]));
    // Test black queen
    assertEquals(Squares.bit(Squares.sq(Squares.FILE_D, Squares.RANK_8)), pieces[Pieces.BQ]);
    assertEquals(1, Long.bitCount(pieces[Pieces.BQ]));
  }

  @Test
  void testStartingPositionKings() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    long[] pieces = startingBoard.getPieces();
    // Test white king
    assertEquals(Squares.bit(Squares.sq(Squares.FILE_E, Squares.RANK_1)), pieces[Pieces.WK]);
    assertEquals(1, Long.bitCount(pieces[Pieces.WK]));
    // Test black king
    assertEquals(Squares.bit(Squares.sq(Squares.FILE_E, Squares.RANK_8)), pieces[Pieces.BK]);
    assertEquals(1, Long.bitCount(pieces[Pieces.BK]));
  }

  @Test
  void testStartingPositionWhitePieces() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    long whitePieces = startingBoard.getWhitePieces();
    assertEquals(Squares.rankMask(Squares.RANK_1) | Squares.rankMask(Squares.RANK_2), whitePieces);
    assertEquals(16, Long.bitCount(whitePieces));
  }

  @Test
  void testStartingPositionBlackPieces() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    long blackPieces = startingBoard.getBlackPieces();
    assertEquals(Squares.rankMask(Squares.RANK_7) | Squares.rankMask(Squares.RANK_8), blackPieces);
    assertEquals(16, Long.bitCount(blackPieces));
  }

  @Test
  void testStartingPositionAllPieces() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    long allPieces = startingBoard.getAllPieces();
    long whitePieces = startingBoard.getWhitePieces();
    long blackPieces = startingBoard.getBlackPieces();
    assertEquals(
        Squares.rankMask(Squares.RANK_1)
            | Squares.rankMask(Squares.RANK_2)
            | Squares.rankMask(Squares.RANK_7)
            | Squares.rankMask(Squares.RANK_8),
        allPieces);
    assertEquals(whitePieces | blackPieces, allPieces);
    assertEquals(32, Long.bitCount(allPieces));
  }

  @Test
  void testStartingPositionMetadata() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    assertEquals(Pieces.WHITE, startingBoard.getSideToMove());
    assertEquals(1, Position.WHITE_KINGSIDE);
    assertEquals(2, Position.WHITE_QUEENSIDE);
    assertEquals(4, Position.BLACK_KINGSIDE);
    assertEquals(8, Position.BLACK_QUEENSIDE);
    assertEquals(
        Position.WHITE_KINGSIDE
            | Position.WHITE_QUEENSIDE
            | Position.BLACK_KINGSIDE
            | Position.BLACK_QUEENSIDE,
        startingBoard.getCastlingRights());
    assertEquals(-1, startingBoard.getEpSquare());
    assertEquals(0, startingBoard.getHalfmoveClock());
    assertEquals(1, startingBoard.getFullMoveNumber());
  }

  @Test
  void testStartingPositionNoPieceOverlap() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    long[] pieces = startingBoard.getPieces();
    for (int firstBitBoard = 0; firstBitBoard <= 11; firstBitBoard++) {
      for (int secondBitBoard = firstBitBoard + 1; secondBitBoard <= 11; secondBitBoard++) {
        assertEquals(0, pieces[firstBitBoard] & pieces[secondBitBoard]);
      }
    }
  }

  @Test
  void testEnPassantPosition() {
    Position enPassant = Position.fromFEN(FEN_EN_PASSANT);
    assertEquals(44, enPassant.getEpSquare());
  }

  @Test()
  void testInvalidAmountOfArgs() {
    String invalidArgs = "8/8/8/8/8/8/8/8 w - - 0";
    assertThrows(IllegalArgumentException.class, () -> Position.fromFEN(invalidArgs));
  }

  @Test()
  void testInvalidFenPiecePosition() {
    String invalidAmountOfRanks = "8/8/8/8/8/8/8 w - - 0 0";
    assertThrows(IllegalArgumentException.class, () -> Position.fromFEN(invalidAmountOfRanks));
    String invalidAmountOfFiles = "9/8/8/8/8/8/8/8 w - - 0 0";
    assertThrows(IllegalArgumentException.class, () -> Position.fromFEN(invalidAmountOfFiles));
    String invalidPieceNotation = "8/f7/8/8/8/8/8/8 w - - 0 0";
    assertThrows(IllegalArgumentException.class, () -> Position.fromFEN(invalidPieceNotation));
  }

  @Test()
  void testInvalidSideToMovePosition() {
    String invalidSideToMove = "8/8/8/8/8/8/8/8 c - - 0 0";
    assertThrows(IllegalArgumentException.class, () -> Position.fromFEN(invalidSideToMove));
  }

  @Test()
  void testInvalidCastlingPosition() {
    String invalidCastlingPosition = "8/8/8/8/8/8/8/8 w b - 0 0";
    assertThrows(IllegalArgumentException.class, () -> Position.fromFEN(invalidCastlingPosition));
  }

  @Test()
  void testInvalidEnPPosition() {
    String invalidEnPPositionInvalidEntry = "8/8/8/8/8/8/8/8 w - e 0 0";
    assertThrows(
        IllegalArgumentException.class, () -> Position.fromFEN(invalidEnPPositionInvalidEntry));
    String invalidEnPPositionInvalidSquare = "8/8/8/8/8/8/8/8 w - q5 0 0";
    assertThrows(
        IllegalArgumentException.class, () -> Position.fromFEN(invalidEnPPositionInvalidSquare));
  }

  @Test()
  void testInvalidMoveCounterPosition() {
    String invalidHalfmovePosition = "8/8/8/8/8/8/8/8 w - - a 0";
    assertThrows(IllegalArgumentException.class, () -> Position.fromFEN(invalidHalfmovePosition));
    String invalidTotalMovePosition = "8/8/8/8/8/8/8/8 w - - 0 a";
    assertThrows(IllegalArgumentException.class, () -> Position.fromFEN(invalidTotalMovePosition));
  }

  @Test()
  void testBasicPieceMoveAndUndo() {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    Position expectedMove =
        Position.fromFEN("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR b KQkq - 1 1");
    Position expectedUndo = Position.fromFEN(Position.FEN_STARTING_POSITION);
    Move testMove = Move.of(8, 16);
    startingBoard.makeMove(testMove);
    assertTrue(startingBoard.equals(expectedMove));
    startingBoard.undoMove(testMove);
    assertTrue(startingBoard.equals(expectedUndo));
  }
}
