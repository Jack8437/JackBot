package com.jackbot;

import com.jackbot.core.Fen;
import com.jackbot.core.Move;
import com.jackbot.core.Position;

public class JackBot {
  public static void main(String[] args) {
    Position startingBoard = Fen.parse(Fen.FEN_STARTING_POSITION);
    Position test = Fen.parse(Fen.FEN_STARTING_POSITION);
    Move testMove = Move.of(8, 16);
    startingBoard.makeMove(testMove);
    startingBoard.undoMove(testMove);
    System.out.println(startingBoard.equals(test));
  }
}
