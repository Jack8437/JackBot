package com.jackbot.core;

public class Position {
  // Bit flags to represent castling rights
  public static final int WHITE_KINGSIDE = 1;
  public static final int WHITE_QUEENSIDE = 2;
  public static final int BLACK_KINGSIDE = 4;
  public static final int BLACK_QUEENSIDE = 8;

  private static final char[] FEN_LETTER_CONVENTION = {
    'P', 'N', 'B', 'R', 'Q', 'K', 'p', 'n', 'b', 'r', 'q', 'k'
  };

  private long[] pieces;
  private long whitePieces;
  private long blackPieces;
  private long allPieces;
  private int sideToMove;
  private int castlingRights;
  // En Passant Square
  private int epSquare;
  // Number of half moves since last pawn move or last capture, used to track 50 move rule (100 half
  // moves)
  private int halfmoveClock;
  // Move count in chess notation
  private int fullMoveNumber;

  /** Setup the starting position for a standard chess game */
  Position() {
    pieces = startingBoard();
    recomputePieces();
    sideToMove = Pieces.WHITE;
    castlingRights = WHITE_KINGSIDE | WHITE_QUEENSIDE | BLACK_KINGSIDE | BLACK_QUEENSIDE;
    epSquare = -1;
    halfmoveClock = 0;
    fullMoveNumber = 1;
  }

  /**
   * Returns the starting board as a bitboard
   *
   * @return the starting position as a bitboard for a standard game of chess
   */
  private long[] startingBoard() {
    long[] startingBoard = new long[12];
    // Setup the white pieces
    // White pawns
    startingBoard[Pieces.WP] = Squares.rankMask(Squares.RANK_2);
    // White knights
    startingBoard[Pieces.WN] =
        Squares.bit(Squares.sq(Squares.FILE_B, Squares.RANK_1))
            | Squares.bit(Squares.sq(Squares.FILE_G, Squares.RANK_1));
    // White bishops
    startingBoard[Pieces.WB] =
        Squares.bit(Squares.sq(Squares.FILE_C, Squares.RANK_1))
            | Squares.bit(Squares.sq(Squares.FILE_F, Squares.RANK_1));
    // White rooks
    startingBoard[Pieces.WR] =
        Squares.bit(Squares.sq(Squares.FILE_A, Squares.RANK_1))
            | Squares.bit(Squares.sq(Squares.FILE_H, Squares.RANK_1));
    // White queen
    startingBoard[Pieces.WQ] = Squares.bit(Squares.sq(Squares.FILE_D, Squares.RANK_1));
    // White king
    startingBoard[Pieces.WK] = Squares.bit(Squares.sq(Squares.FILE_E, Squares.RANK_1));
    // Black pawns
    startingBoard[Pieces.BP] = Squares.rankMask(Squares.RANK_7);
    // Black knights
    startingBoard[Pieces.BN] =
        Squares.bit(Squares.sq(Squares.FILE_B, Squares.RANK_8))
            | Squares.bit(Squares.sq(Squares.FILE_G, Squares.RANK_8));
    // Black bishops
    startingBoard[Pieces.BB] =
        Squares.bit(Squares.sq(Squares.FILE_C, Squares.RANK_8))
            | Squares.bit(Squares.sq(Squares.FILE_F, Squares.RANK_8));
    // Black rooks
    startingBoard[Pieces.BR] =
        Squares.bit(Squares.sq(Squares.FILE_A, Squares.RANK_8))
            | Squares.bit(Squares.sq(Squares.FILE_H, Squares.RANK_8));
    // Black queen
    startingBoard[Pieces.BQ] = Squares.bit(Squares.sq(Squares.FILE_D, Squares.RANK_8));
    // Black king
    startingBoard[Pieces.BK] = Squares.bit(Squares.sq(Squares.FILE_E, Squares.RANK_8));
    return startingBoard;
  }

  /** Update all piece variables based on the current position. */
  private void recomputePieces() {
    whitePieces =
        pieces[Pieces.WP]
            | pieces[Pieces.WN]
            | pieces[Pieces.WB]
            | pieces[Pieces.WR]
            | pieces[Pieces.WQ]
            | pieces[Pieces.WK];
    blackPieces =
        pieces[Pieces.BP]
            | pieces[Pieces.BN]
            | pieces[Pieces.BB]
            | pieces[Pieces.BR]
            | pieces[Pieces.BQ]
            | pieces[Pieces.BK];
    allPieces = whitePieces | blackPieces;
  }

  long[] getPieces() {
    return pieces.clone();
  }

  long getWhitePieces() {
    return whitePieces;
  }

  long getBlackPieces() {
    return blackPieces;
  }

  long getAllPieces() {
    return allPieces;
  }

  int getSideToMove() {
    return sideToMove;
  }

  int getCastlingRights() {
    return castlingRights;
  }

  int getEpSquare() {
    return epSquare;
  }

  int getHalfmoveClock() {
    return halfmoveClock;
  }

  int getFullMoveNumber() {
    return fullMoveNumber;
  }

  private char getPieceAt(int square) {
    long mask = Squares.bit(square);
    for (int counter = 0; counter < 12; counter++) {
      if ((pieces[counter] & mask) != 0) {
        return FEN_LETTER_CONVENTION[counter];
      }
    }
    return '.';
  }

  @Override
  public String toString() {
    StringBuilder humanReadableBoard = new StringBuilder();
    for (int rank = 0; rank <= 7; rank++) {
      for (int file = 0; file <= 7; file++) {
        humanReadableBoard.append(getPieceAt(Squares.sq(file, rank)));
        humanReadableBoard.append(" ");
      }
      humanReadableBoard.append("\n");
    }
    return humanReadableBoard.toString();
  }
}
