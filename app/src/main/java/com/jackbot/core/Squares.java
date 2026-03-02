package com.jackbot.core;

public final class Squares {
  /** Represents the length and width of the board */
  public static final int BOARD_SIZE = 8;

  // Files
  // They go left to right on the chess board.
  /** 0 represents file A. */
  public static final int FILE_A = 0;

  /** 1 represents file B. */
  public static final int FILE_B = 1;

  /** 2 represents file C. */
  public static final int FILE_C = 2;

  /** 3 represents file D. */
  public static final int FILE_D = 3;

  /** 4 represents file E. */
  public static final int FILE_E = 4;

  /** 5 represents file F. */
  public static final int FILE_F = 5;

  /** 6 represents file G. */
  public static final int FILE_G = 6;

  /** 7 represents file H. */
  public static final int FILE_H = 7;

  // Ranks
  // Ranks go from top to bottom on the chess board.
  /** 0 represents rank 1. */
  public static final int RANK_1 = 0;

  /** 1 represents rank 2. */
  public static final int RANK_2 = 1;

  /** 2 represents rank 3. */
  public static final int RANK_3 = 2;

  /** 3 represents rank 4. */
  public static final int RANK_4 = 3;

  /** 4 represents rank 5. */
  public static final int RANK_5 = 4;

  /** 5 represents rank 6. */
  public static final int RANK_6 = 5;

  /** 6 represents rank 7. */
  public static final int RANK_7 = 6;

  /** 7 represents rank 8. */
  public static final int RANK_8 = 7;

  /** Empty constructor. Should never be called. */
  private Squares() {}

  /**
   * Calculates the square a piece is on and returns it as an int Each int is a square on the board
   * with 0 being the bottom left and 63 being the top right The squares increments from left to
   * right then moving up to the next rank
   *
   * @param file the piece is on
   * @param rank the piece is on
   * @return the sq the piece is on as an int position
   */
  public static int sq(int file, int rank) {
    return rank * 8 + file;
  }

  /**
   * Returns the position a piece is on as a bit
   *
   * @param square is where the piece is represented as an int (can use sq() to get that value)
   * @return the bit the piece is on
   */
  public static long bit(int square) {
    return 1L << square;
  }

  /**
   * Returns a long with all bits of a specific rank set.
   *
   * @param rank to set all bits for.
   * @return a long representing all the squares of a chess board.
   */
  public static long rankMask(int rank) {
    return 0xFFL << (rank * 8);
  }

  /**
   * Returns a long with all bits of a specific file set.
   *
   * @param file to set all bits for.
   * @return a long representing all the squares of a chess board.
   */
  public static long fileMask(int file) {
    return 0x0101010101010101L << file;
  }
}
