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
  public static final String FEN_STARTING_POSITION =
      "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

  private long[] pieces;
  private long whitePieces;
  private long blackPieces;
  private long allPieces;
  private int sideToMove;
  private int castlingRights;
  // En Passant Square
  private int epSquare;
  // Number of half moves since last pawn move or last capture, used to track 50
  // move rule (100 half
  // moves)
  private int halfmoveClock;
  // Move count in chess notation
  private int fullMoveNumber;

  private Undo[] undoStack = new Undo[255];
  private int ply = 0;

  private Position(
      long[] pieces,
      int sideToMove,
      int castlingRights,
      int epSquare,
      int halfmoveClock,
      int fullMoveNumber) {
    this.pieces = pieces;
    recomputePieces();
    this.sideToMove = sideToMove;
    this.castlingRights = castlingRights;
    this.epSquare = epSquare;
    this.halfmoveClock = halfmoveClock;
    this.fullMoveNumber = fullMoveNumber;
    // Create undo stack
    for (int counter = 0; counter < undoStack.length; counter++) {
      undoStack[counter] = new Undo();
    }
  }

  public static Position fromFEN(String fen) throws IllegalArgumentException {
    // Break up the FEN input
    String[] seperatedFen = fen.split(" ");
    if (seperatedFen.length != 6) {
      throw new IllegalArgumentException(
          "FEN input was incorrect (ex. \"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1\")");
    }
    // Convert the FEN piece input into a bitboard
    long[] fenPieces = convertFenToPieces(seperatedFen[0]);
    // Figure out the side to move
    int fenSideToMove;
    if (seperatedFen[1].equals("w")) {
      fenSideToMove = Pieces.WHITE;
    } else if (seperatedFen[1].equals("b")) {
      fenSideToMove = Pieces.BLACK;
    } else {
      throw new IllegalArgumentException("Invalid side to move entry (most be w or b)");
    }
    // Figure out current castling rights
    int fenCastlingRights = convertFenToCastlingRights(seperatedFen[2]);
    // Figure out if en passant is possible
    int fenEnPassant = convertFenToEnPassant(seperatedFen[3]);
    // Convert clock and number to ints
    int fenHalfmoveClock;
    try {
      fenHalfmoveClock = Integer.parseInt(seperatedFen[4]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid FEN halfmove clock");
    }
    int fenFullMoveNumber;
    try {
      fenFullMoveNumber = Integer.parseInt(seperatedFen[5]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid FEN full move number");
    }
    // Finally create new position and return it
    return new Position(
        fenPieces,
        fenSideToMove,
        fenCastlingRights,
        fenEnPassant,
        fenHalfmoveClock,
        fenFullMoveNumber);
  }

  // Helper methods to convert FEN input into a Position object

  private static long[] convertFenToPieces(String fen) throws IllegalArgumentException {
    long[] fenPieces = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    String[] fenRanks = fen.split("/");
    if (fenRanks.length != 8) {
      throw new IllegalArgumentException(
          "Invalid FEN piece notation, remember each rank is separated by a \"/\"");
    }
    int file;
    int rank = 7;
    for (int fenRank = 0; fenRank <= 7; fenRank++) {
      file = 0;
      for (int stringWalker = 0; stringWalker < fenRanks[fenRank].length(); stringWalker++) {
        try {
          int fenEmptySpace =
              Integer.parseInt(fenRanks[fenRank].substring(stringWalker, stringWalker + 1));
          file += fenEmptySpace;
        } catch (NumberFormatException e) {
          // Check if it is a piece instead of empty space
          int fenPiece = convertFenPieceToNumberValue(fenRanks[fenRank].charAt(stringWalker));
          fenPieces[fenPiece] |= Squares.bit(Squares.sq(file, rank));
          file++;
        }
        if (file > 8) {
          throw new IllegalArgumentException(
              "Rank " + rank + " has more than 8 pieces and/or empty spaces");
        }
      }
      rank--;
    }
    return fenPieces;
  }

  private static int convertFenPieceToNumberValue(char fenPiece) throws IllegalArgumentException {
    switch (fenPiece) {
      case 'P':
        return Pieces.WP;
      case 'N':
        return Pieces.WN;
      case 'B':
        return Pieces.WB;
      case 'R':
        return Pieces.WR;
      case 'Q':
        return Pieces.WQ;
      case 'K':
        return Pieces.WK;
      case 'p':
        return Pieces.BP;
      case 'n':
        return Pieces.BN;
      case 'b':
        return Pieces.BB;
      case 'r':
        return Pieces.BR;
      case 'q':
        return Pieces.BQ;
      case 'k':
        return Pieces.BK;
      default:
        throw new IllegalArgumentException("Invalid FEN piece position " + fenPiece);
    }
  }

  private static int convertFenToCastlingRights(String fen) throws IllegalArgumentException {
    String noCastling = "-";
    int fenCastlingRights = 0;
    if (fen.equals(noCastling)) {
      return fenCastlingRights;
    }
    for (int charCounter = 0; charCounter < fen.length(); charCounter++) {
      switch (fen.charAt(charCounter)) {
        case 'K' -> fenCastlingRights |= WHITE_KINGSIDE;
        case 'Q' -> fenCastlingRights |= WHITE_QUEENSIDE;
        case 'k' -> fenCastlingRights |= BLACK_KINGSIDE;
        case 'q' -> fenCastlingRights |= BLACK_QUEENSIDE;
        default -> throw new IllegalArgumentException("Invalid FEN castling rights");
      }
    }
    return fenCastlingRights;
  }

  private static int convertFenToEnPassant(String fen) throws IllegalArgumentException {
    String noEnPassant = "-";
    int fenEnPassant = -1;
    // Check if en passant is no possible
    if (fen.equals(noEnPassant)) {
      return fenEnPassant;
    }
    // Ensure if en passant is possible, the entry is the correct length
    if (fen.length() != 2) {
      throw new IllegalArgumentException("Invalid FEN en passant entry");
    }
    // Convert the FEN file to the internal numbers
    int fenFile;
    switch (fen.charAt(0)) {
      case 'a' -> fenFile = Squares.FILE_A;
      case 'b' -> fenFile = Squares.FILE_B;
      case 'c' -> fenFile = Squares.FILE_C;
      case 'd' -> fenFile = Squares.FILE_D;
      case 'e' -> fenFile = Squares.FILE_E;
      case 'f' -> fenFile = Squares.FILE_F;
      case 'g' -> fenFile = Squares.FILE_G;
      case 'h' -> fenFile = Squares.FILE_H;
      default -> throw new IllegalArgumentException("Invalid file for FEN en passant entry");
    }
    // Convert the FEN rank to the internal numbers
    int fenRank;
    try {
      fenRank = Integer.parseInt(fen.substring(1)) - 1;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid rank for FEN en passant entry");
    }
    fenEnPassant = Squares.sq(fenFile, fenRank);
    return fenEnPassant;
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

  public void makeMove(Move move) {
    // Update undo stack
    undoStack[ply].castlingRights = this.castlingRights;
    undoStack[ply].epSquare = this.epSquare;
    undoStack[ply].halfmoveClock = this.halfmoveClock;
    undoStack[ply].fullMoveNumber = this.fullMoveNumber;
    undoStack[ply].capturedPieceIndex = -1;
    undoStack[ply].capturedSquare = -1;
    ply++;
    // Move piece
    this.movePiece(move.from(), move.to());
    // Flip sideToMove
    this.flipSideToMove();
    // TODO: check for en passant
    this.epSquare = -1;
    // TODO: update halfmoveClock correctly
    this.halfmoveClock++;
    if (this.sideToMove == Pieces.WHITE) {
      this.fullMoveNumber++;
    }
    // Recompute Occupancies
    recomputePieces();
  }

  public void undoMove(Move move) {
    // Restore metadata from undoStack
    ply--;
    this.castlingRights = undoStack[ply].castlingRights;
    this.epSquare = undoStack[ply].epSquare;
    this.halfmoveClock = undoStack[ply].halfmoveClock;
    this.fullMoveNumber = undoStack[ply].fullMoveNumber;
    // Flip sideToMove
    this.flipSideToMove();
    // Move piece back
    this.movePiece(move.to(), move.from());
    // Recompute occupancies
    recomputePieces();
  }

  private void flipSideToMove() {
    this.sideToMove = Math.abs(this.sideToMove - 1);
  }

  private void movePiece(int from, int to) {
    int pieceType = this.getPieceAt(from);
    this.removePieceAt(pieceType, from);
    this.addPieceAt(pieceType, to);
  }

  private void removePieceAt(int pieceType, int sq) {
    pieces[pieceType] &= ~(Squares.bit(sq));
  }

  private void addPieceAt(int pieceType, int sq) {
    pieces[pieceType] |= Squares.bit(sq);
  }

  // Getter methods

  /**
   * Clones pieces to ensure the original is not mutable
   *
   * @return clone of pieces
   */
  public long[] getPieces() {
    return pieces.clone();
  }

  /**
   * Return the long whitePieces
   *
   * @return whitePieces
   */
  long getWhitePieces() {
    return whitePieces;
  }

  /**
   * Return the long blackPieces
   *
   * @return blackPieces
   */
  long getBlackPieces() {
    return blackPieces;
  }

  /**
   * Return the long allPieces
   *
   * @return allPieces
   */
  long getAllPieces() {
    return allPieces;
  }

  /**
   * Return the int sideToMove
   *
   * @return sideToMove
   */
  int getSideToMove() {
    return sideToMove;
  }

  /**
   * Return the int castlingRights
   *
   * @return castlingRights
   */
  int getCastlingRights() {
    return castlingRights;
  }

  /**
   * Return the square for en passant, -1 means en passant is not possible
   *
   * @return epSquare
   */
  int getEpSquare() {
    return epSquare;
  }

  /**
   * Return the int halfmoveClock
   *
   * @return halfmoveClock
   */
  int getHalfmoveClock() {
    return halfmoveClock;
  }

  /**
   * Return the int fullMoveNumber
   *
   * @return fullMoveNumber
   */
  int getFullMoveNumber() {
    return fullMoveNumber;
  }

  private int getPieceAt(int square) {
    long mask = Squares.bit(square);
    for (int counter = 0; counter < 12; counter++) {
      if ((pieces[counter] & mask) != 0) {
        return counter;
      }
    }
    return -1;
  }

  @Override
  public int hashCode() {
    assert false : "hashCode not designed";
    return 42; // any arbitrary constant will do
  }

  @Override
  public boolean equals(Object otherObject) {
    // Ensure otherObject is a Position then cast to a Position
    if (!(otherObject instanceof Position)) {
      return false;
    }
    Position other = (Position) otherObject;
    // Loop through and check the long array of pieces
    for (int counter = 0; counter < pieces.length; counter++) {
      if (this.pieces[counter] != other.pieces[counter]) {
        return false;
      }
    }
    // Check the longs next
    if ((this.whitePieces != other.whitePieces)
        || (this.blackPieces != other.blackPieces)
        || (this.allPieces != other.allPieces)) {
      return false;
    }
    // Finally check the ints
    if ((this.sideToMove != other.sideToMove)
        || (this.castlingRights != other.castlingRights)
        || (this.epSquare != other.epSquare)
        || (this.halfmoveClock != other.halfmoveClock)
        || (this.fullMoveNumber != other.fullMoveNumber)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder humanReadableBoard = new StringBuilder();
    for (int rank = 7; rank >= 0; rank--) {
      for (int file = 0; file <= 7; file++) {
        int piece = getPieceAt(Squares.sq(file, rank));
        if (piece != -1) {
          humanReadableBoard.append(FEN_LETTER_CONVENTION[getPieceAt(Squares.sq(file, rank))]);
        } else {
          humanReadableBoard.append('.');
        }
        humanReadableBoard.append(" ");
      }
      humanReadableBoard.append("\n");
    }
    humanReadableBoard.append("Metadata:\n");
    humanReadableBoard.append("Side to move: " + sideToMove + "\n");
    humanReadableBoard.append("Castling rights: " + castlingRights + "\n");
    humanReadableBoard.append("En passant square: " + epSquare + "\n");
    humanReadableBoard.append("Halfmove clock: " + halfmoveClock + "\n");
    humanReadableBoard.append("Full move number: " + fullMoveNumber + "\n");
    return humanReadableBoard.toString();
  }
}
