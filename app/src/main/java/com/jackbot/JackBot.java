package com.jackbot;

import com.jackbot.core.Pieces;
import com.jackbot.core.Position;
import com.jackbot.core.Squares;

public class JackBot {
  public static void main(String[] args) {
    Position test = Position.fromFEN("4k3/8/8/8/8/8/8/4K3 w - - 0 1");
    System.out.println(test.toString());
    test = Position.fromFEN("r6r/8/8/8/8/8/8/R6R w - - 0 1");
    System.out.println(test.toString());
    test = Position.fromFEN("8/8/8/8/4P3/8/8/8 w - - 0 1");
    System.out.println(test.toString());
    test = Position.fromFEN("8/8/8/8/8/8/8/8 w - - 0 1");
    System.out.println(test.toString());
    test = Position.fromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    System.out.println(test.toString());
    long[] pieces = test.getPieces();
    System.out.println(
        Long.toBinaryString(pieces[Pieces.WP]) + " " + Long.toHexString(pieces[Pieces.WP]));
    System.out.println(
        Long.toBinaryString(Squares.rankMask(Squares.RANK_2))
            + " "
            + Squares.rankMask(Squares.RANK_2));
  }
}
