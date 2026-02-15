package com.jackbot.core;

public final class Squares {
  // Files
  public static final int FILE_A = 0;
  public static final int FILE_B = 1;
  public static final int FILE_C = 2;
  public static final int FILE_D = 3;
  public static final int FILE_E = 4;
  public static final int FILE_F = 5;
  public static final int FILE_G = 6;
  public static final int FILE_H = 7;

  // Ranks
  public static final int RANK_1 = 0;
  public static final int RANK_2 = 1;
  public static final int RANK_3 = 2;
  public static final int RANK_4 = 3;
  public static final int RANK_5 = 4;
  public static final int RANK_6 = 5;
  public static final int RANK_7 = 6;
  public static final int RANK_8 = 7;

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

  public static long rankMask(int rank) {
    return 0xFFL << (rank * 8);
  }

  public static long fileMask(int file) {
    return 0x0101010101010101L << file;
  }
}
