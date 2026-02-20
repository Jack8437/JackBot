package com.jackbot.core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(
    value = "URF_UNREAD_FIELD",
    justification = "Suppress for now, errors will be fixed once Undo is integrated into Position")
public final class Undo {
  /** Stores the castling rights of a previous position. */
  int castlingRights;

  /** Stores the en passant square of a previous position. */
  int epSquare;

  /** Stores the halfmove clock of a previous position. */
  int halfmoveClock;

  /** Stores the full move number of a previous position. */
  int fullMoveNumber;

  /** Stores the captured piece index of a previous position. If there was none, it is set to -1. */
  int capturedPieceIndex;

  /**
   * Stores the square of the captured piece of a previous position. If there was none, it is set to
   * -1.
   */
  int capturedSquare;

  /** Empty constructor for Undo class. */
  public Undo() {}
}
