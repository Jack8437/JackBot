package com.jackbot.core;

public final class Move {
  /**
   * rawData uses bits to store all the move data needed Bits 0-5: from square (0-63) Bits 6-11: to
   * square (0-63) Bits 12-15: promotionFlag 0: none, 1: knight, 2: bishop, 3: rook, 4: queen Bits
   * 16-19: extraFlags Bit 16: capture, Bit 17: double pawn push, Bit 18: en passant, Bit 19:
   * castle.
   */
  private final int rawData;

  // Helpers for promotion pieces.
  /** Integer value for when there is no promotion. */
  public static final int PROMO_NONE = 0;

  /** Integer value for when a pawn promotes to a knight. */
  public static final int PROMO_N = 1;

  /** Integer value for when a pawn promotes to a bishop. */
  public static final int PROMO_B = 2;

  /** Integer value for when a pawn promotes to a rook. */
  public static final int PROMO_R = 3;

  /** Integer value for when a pawn promotes to a queen. */
  public static final int PROMO_Q = 4;

  // Other flags options
  /** Bit value to represent a capture in a move. */
  private static final int CAPTURE = 0x1;

  /** Bit value to represent a pawn double push in a move. */
  private static final int DOUBLE_PUSH = 0x2;

  /** Bit value to represent en passant in a move. */
  private static final int EN_PASSANT = 0x4;

  /** Bit value to represent castling in a move. */
  private static final int CASTLE = 0x8;

  // Extractor helpers
  /** Mask to extract the square from rawData. */
  private static final int SQ_MASK = 0x3F;

  /** Mask to extract the extra flags from rawData. */
  private static final int FLAG_MASK = 0xF;

  /** Chars to convert promotion bit to human readable. */
  private static final char[] PROMOTION_PIECE = {'x', 'n', 'b', 'r', 'q'};

  // Constructors
  /**
   * Private constructor for creating a move object. Used when there is no promotion or other
   * special chess events.
   *
   * @param from square the piece is coming from
   * @param to square the piece is going to
   */
  private Move(int from, int to) {
    this(from, to, 0, 0);
  }

  /**
   * Private constructor for creating a move object. Used when there is a promotion and other
   * special chess events.
   *
   * @param from square the piece is coming from
   * @param to square the piece is going to
   * @param promotionFlag stores if there was a promotion in the move and if so what piece it is
   *     promoting to
   * @param otherFlags stores other special chess events
   * @throws IllegalArgumentException
   */
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
  /**
   * Basic chess move. A piece moves from one square to another without any other chess events.
   *
   * @param from square the piece is coming from
   * @param to square the piece is going to
   * @return A new Move object
   * @throws IllegalArgumentException if the square is an illegal value, under 0 or over 63
   */
  public static Move of(int from, int to) throws IllegalArgumentException {
    return new Move(from, to);
  }

  /**
   * Chess move involving a capture. A piece captures another piece in a basic way involving no
   * other special chess events.
   *
   * @param from square the piece is coming from
   * @param to square the piece is going to
   * @return A new Move object
   * @throws IllegalArgumentException if the square is an illegal value, under 0 or over 63
   */
  public static Move capture(int from, int to) throws IllegalArgumentException {
    return new Move(from, to, 0, Move.CAPTURE);
  }

  /**
   * Chess move involving a promotion. Can include a capture as well but not needed.
   *
   * @param from square the piece is coming from
   * @param to square the piece is going to
   * @param promotionType What piece the pawn is promoting to
   * @param isCapture if there was a capture during the move to the promotion square
   * @return a new Move object
   * @throws IllegalArgumentException if the square is an illegal value, under 0 or over 63
   */
  public static Move promotion(int from, int to, int promotionType, boolean isCapture)
      throws IllegalArgumentException {
    int otherFlags;
    if (isCapture) {
      otherFlags = Move.CAPTURE;
    } else {
      otherFlags = 0;
    }
    return new Move(from, to, promotionType, otherFlags);
  }

  /**
   * Chess move involving castling. Will never include a capture or promotion.
   *
   * @param from square the piece is coming from
   * @param to square the piece is going to
   * @return a new Move object
   * @throws IllegalArgumentException if the square is an illegal value, under 0 or over 63
   */
  public static Move castle(int from, int to) throws IllegalArgumentException {
    return new Move(from, to, 0, Move.CASTLE);
  }

  /**
   * Chess move involving en passant. Will always include a capture and the en passant flags, will
   * never involve a promotion.
   *
   * @param from square the piece is coming from
   * @param to square the piece is going to
   * @return a new Move object
   * @throws IllegalArgumentException if the square is an illegal value, under 0 or over 63
   */
  public static Move enPassant(int from, int to) throws IllegalArgumentException {
    return new Move(from, to, 0, Move.CAPTURE | Move.EN_PASSANT);
  }

  /**
   * Chess move involving a double pawn push. Will never involve any other extra flags.
   *
   * @param from square the piece is coming from
   * @param to square the piece is going to
   * @return a new Move object
   * @throws IllegalArgumentException if the square is an illegal value, under 0 or over 63
   */
  public static Move doublePawnPush(int from, int to) throws IllegalArgumentException {
    return new Move(from, to, 0, Move.DOUBLE_PUSH);
  }

  // Getter methods
  /**
   * Gets the from square by applying the square mask on the start of rawData.
   *
   * @return the square the piece is coming from
   */
  public int from() {
    return rawData & Move.SQ_MASK;
  }

  /**
   * Gets the to square by apply the square mask to rawData, but first moving the bits over to avoid
   * the from bits.
   *
   * @return the square the piece is going to
   */
  public int to() {
    return (rawData >> 6) & Move.SQ_MASK;
  }

  /**
   * Gets the promotion type from the move by removing the from and to sections and applying the
   * flag mask.
   *
   * @return 0 if there is no promotion in the move, others the promotion piece type as an int.
   */
  private int promotion() {
    return (rawData >> 12) & Move.FLAG_MASK;
  }

  /**
   * Gets if there is a promotion in the move.
   *
   * @return True if promotion is anything but 0, false otherwise.
   */
  public boolean isPromotion() {
    return this.promotion() != 0;
  }

  /**
   * Gets the promotion type by translating the int from promotion using the static PROMOTION_PIECE.
   *
   * @return a char by translating the int value using PROMOTION_PIECE.
   */
  public char promotionType() {
    return Move.PROMOTION_PIECE[this.promotion()];
  }

  /**
   * Gets if there was a capture in the move by running a bitwise operation on the flags getter
   * method.
   *
   * @return true if there is a capture in the move, false otherwise.
   */
  public boolean isCapture() {
    return (this.flags() & Move.CAPTURE) == Move.CAPTURE;
  }

  /**
   * Gets if there was a double pawn push in the move by running a bitwise operation on the flags
   * getter method.
   *
   * @return true if there was a double pawn push in the move, false otherwise.
   */
  public boolean isDoublePush() {
    return (this.flags() & Move.DOUBLE_PUSH) == Move.DOUBLE_PUSH;
  }

  /**
   * Gets if there was en passant in the move by running a bitwise operation on the flags getter
   * method.
   *
   * @return true if there was en passant in the move, false otherwise.
   */
  public boolean isEnPassant() {
    return (this.flags() & Move.EN_PASSANT) == Move.EN_PASSANT;
  }

  /**
   * Gets if there was castling in the move by running a bitwise operation on the flags getter
   * method.
   *
   * @return true if there was castling in the move, false otherwise.
   */
  public boolean isCastle() {
    return (this.flags() & Move.CASTLE) == Move.CASTLE;
  }

  /**
   * Removes all other data from rawData and apply the flags mask to rawData.
   *
   * @return the flags of the move.
   */
  private int flags() {
    return (this.rawData >> 16) & Move.FLAG_MASK;
  }

  /**
   * Helper method for the toString debugger. Convert a square to UCI format.
   *
   * @param square the piece is on
   * @return the square a piece is on as a String in UCI format.
   */
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
