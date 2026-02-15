package com.jackbot.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PositionTest {

  @Test
  void testStartingPositionPawns() {
    Position startingBoard = new Position();
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
    Position startingBoard = new Position();
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
    Position startingBoard = new Position();
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
    Position startingBoard = new Position();
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
    Position startingBoard = new Position();
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
    Position startingBoard = new Position();
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
    Position startingBoard = new Position();
    long whitePieces = startingBoard.getWhitePieces();
    assertEquals(Squares.rankMask(Squares.RANK_1) | Squares.rankMask(Squares.RANK_2), whitePieces);
    assertEquals(16, Long.bitCount(whitePieces));
  }

  @Test
  void testStartingPositionBlackPieces() {
    Position startingBoard = new Position();
    long blackPieces = startingBoard.getBlackPieces();
    assertEquals(Squares.rankMask(Squares.RANK_7) | Squares.rankMask(Squares.RANK_8), blackPieces);
    assertEquals(16, Long.bitCount(blackPieces));
  }

  @Test
  void testStartingPositionAllPieces() {
    Position startingBoard = new Position();
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
    Position startingBoard = new Position();
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
    Position startingBoard = new Position();
    long[] pieces = startingBoard.getPieces();
    for (int firstBitBoard = 0; firstBitBoard <= 11; firstBitBoard++) {
      for (int secondBitBoard = firstBitBoard + 1; secondBitBoard <= 11; secondBitBoard++) {
        assertEquals(0, pieces[firstBitBoard] & pieces[secondBitBoard]);
      }
    }
  }
}
