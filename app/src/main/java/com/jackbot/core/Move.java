package com.jackbot.core;

public final class Move {
  /**
   * rawData uses bits to store all the move data needed Bits 0-5: from square (0-63) Bits 6-11: to
   * square (0-63) Bits 12-15: promotionFlag 0: none, 1: knight, 2: bishop, 3: rook, 4: queen Bits
   * 16-19: extraFlags Bit 16: capture, Bit 17: double pawn push, Bit 18: en passant, Bit 19: castle
   */
  private final int rawData;

  // Helpers for promotion pieces
  public static final int PROMO_NONE = 0;
  public static final int PROMO_N = 1;
  public static final int PROMO_B = 2;
  public static final int PROMO_R = 3;
  public static final int PROMO_Q = 4;

  // Other flags options
  private static final int CAPTURE = 0x1;
  private static final int DOUBLE_PUSH = 0x2;
  private static final int EN_PASSANT = 0x4;
  private static final int CASTLE = 0x8;

  // Extractor helpers
  private static final int SQ_MASK = 0x3F;
  private static final int FLAG_MASK = 0xF;
  private static final char[] PROMOTION_PIECE = {'x', 'n', 'b', 'r', 'q'};

  // Constructors
  private Move(int from, int to) {
    this(from, to, 0, 0);
  }

  private Move(int from, int to, int promotionFlag, int otherFlags)
      throws IllegalArgumentException {
    // Validate from and to
    if ((from < 0 || from > 63) || (to < 0 || to > 63)) {
      throw new IllegalArgumentException("Invalid move, must be between 0-63");
    }
    // Validate promotionFlag
    if (promotionFlag < 0 || promotionFlag > 4) {
      throw new IllegalArgumentException("Invalid promotionFlag, must be between 0-4");
    }
    // Validate otherFlags
    if (otherFlags < 0 || otherFlags > 15) {
      throw new IllegalArgumentException("Invalid otherFlags, must be between 0-15");
    }
    rawData = from | (to << 6) | (promotionFlag << 12) | (otherFlags << 16);
  }

  // Different types of moves
  public static Move of(int from, int to) throws IllegalArgumentException {
    return new Move(from, to);
  }

  public static Move capture(int from, int to) throws IllegalArgumentException {
    return new Move(from, to, 0, Move.CAPTURE);
  }

  public static Move promotion(int from, int to, int promotionType, boolean isCapture) {
    int otherFlags;
    if (isCapture) {
      otherFlags = Move.CAPTURE;
    } else {
      otherFlags = 0;
    }
    return new Move(from, to, promotionType, otherFlags);
  }

  public static Move castle(int from, int to) {
    return new Move(from, to, 0, Move.CASTLE);
  }

  public static Move enPassant(int from, int to) {
    return new Move(from, to, 0, Move.CAPTURE | Move.EN_PASSANT);
  }

  public static Move doublePawnPush(int from, int to) {
    return new Move(from, to, 0, Move.DOUBLE_PUSH);
  }

  // Getter methods
  public int from() {
    return rawData & Move.SQ_MASK;
  }

  public int to() {
    return (rawData >> 6) & Move.SQ_MASK;
  }

  private int promotion() {
    return (rawData >> 12) & Move.FLAG_MASK;
  }

  public boolean isPromotion() {
    return this.promotion() != 0;
  }

  public char promotionType() {
    return Move.PROMOTION_PIECE[this.promotion()];
  }

  public boolean isCapture() {
    return (this.flags() & Move.CAPTURE) == Move.CAPTURE;
  }

  public boolean isDoublePush() {
    return (this.flags() & Move.DOUBLE_PUSH) == Move.DOUBLE_PUSH;
  }

  public boolean isEnPassant() {
    return (this.flags() & Move.EN_PASSANT) == Move.EN_PASSANT;
  }

  public boolean isCastle() {
    return (this.flags() & Move.CASTLE) == Move.CASTLE;
  }

  private int flags() {
    return (this.rawData >> 16) & Move.FLAG_MASK;
  }

  public static String toUci(int square) {
    StringBuilder uci = new StringBuilder();
    uci.append((char) ('a' + (square & 7)));
    uci.append((char) ('1' + (square >> 3)));
    return uci.toString();
  }

  // toString to help with debugging
  @Override
  public String toString() {
    StringBuilder humanReadableMove = new StringBuilder();
    humanReadableMove.append(Move.toUci(this.from()));
    humanReadableMove.append(Move.toUci(this.to()));
    if (this.isPromotion()) {
      humanReadableMove.append(this.promotionType());
    }
    return humanReadableMove.toString();
  }
}
