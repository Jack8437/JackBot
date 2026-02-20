package com.jackbot.core;

public final class Pieces {
  // Used to control who's move it is
  /** 0 is used to represent that it is white's turn to move. */
  public static final int WHITE = 0;

  /** 1 is used to represent that it is black's turn to move. */
  public static final int BLACK = 1;

  // Used to keep track of the type of pieces in a human readable way
  /** 0 represents that the piece is a white pawn. */
  public static final int WP = 0;

  /** 1 represents that the piece is a white knight. */
  public static final int WN = 1;

  /** 2 represents that the piece is a white bishop. */
  public static final int WB = 2;

  /** 3 represents that the piece is a white rook. */
  public static final int WR = 3;

  /** 4 represents that the piece is a white queen. */
  public static final int WQ = 4;

  /** 5 represents that the piece is the white king. */
  public static final int WK = 5;

  /** 6 represents that the piece is a black pawn. */
  public static final int BP = 6;

  /** 7 represents that the piece is a black knight. */
  public static final int BN = 7;

  /** 8 represents that the piece is a black bishop. */
  public static final int BB = 8;

  /** 9 represents that the piece is a black rook. */
  public static final int BR = 9;

  /** 10 represents that the piece is a black queen. */
  public static final int BQ = 10;

  /** 11 represents that the piece is the black king. */
  public static final int BK = 11;

  /** Empty constructor for the Pieces class. Should never be used. */
  private Pieces() {}
}
