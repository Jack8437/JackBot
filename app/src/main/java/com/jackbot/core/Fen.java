package com.jackbot.core;

public class Fen {
  /**
   * A static string used to represent the starting position of a standard game of chess in FEN
   * notation. FEN notation is how a chess position is entered into the Position class.
   */
  public static final String FEN_STARTING_POSITION =
      "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

  /** Private empty constructor */
  private Fen() {}

  /**
   * This method is how a Position object is able to be created. The chess position is entered in
   * FEN notation.
   *
   * @param fen the position in FEN notation.
   * @return a new Position object representing the FEN notation.
   * @throws IllegalArgumentException if the FEN notation is incorrectly formatted.
   */
  public static Position parse(String fen) throws IllegalArgumentException {
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
  /**
   * Takes the pieces part of the FEN notation and converts it into an array of longs that the
   * Position object can use.
   *
   * @param fen notation of just the current piece position.
   * @return a long array containing all the pieces in the current position.
   * @throws IllegalArgumentException if the FEN is formatted incorrectly, too many ranks or files.
   */
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

  /**
   * A helper method that will convert the letter of the piece to an int usable to pieces.
   *
   * @param fenPiece piece type in FEN notation.
   * @return the piece type as an int noted by the Position class.
   * @throws IllegalArgumentException if the fenPiece is not a letter that is used in FEN notation.
   */
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
        case 'K' -> fenCastlingRights |= Position.WHITE_KINGSIDE;
        case 'Q' -> fenCastlingRights |= Position.WHITE_QUEENSIDE;
        case 'k' -> fenCastlingRights |= Position.BLACK_KINGSIDE;
        case 'q' -> fenCastlingRights |= Position.BLACK_QUEENSIDE;
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
}
