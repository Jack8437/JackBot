package com.jackbot.core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(
    value = "UUF_UNUSED_FIELD",
    justification = "Suppress for now, errors will be fixed once Undo is integrated into Position")
public final class Undo {
  int castlingRights;
  int epSquare;
  int halfmoveClock;
  int fullMoveNumber;
  int capturedPieceIndex;
  int capturedSquare;
}
