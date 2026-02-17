package com.jackbot;

import com.jackbot.core.Move;
import com.jackbot.core.Position;

public class JackBot {
  public static void main(String[] args) {
    Position startingBoard = Position.fromFEN(Position.FEN_STARTING_POSITION);
    Move testMove = Move.of(8, 16);
    startingBoard.makeMove(testMove);
    System.out.println("Made Move");
    System.out.println(startingBoard.toString());
    startingBoard.undoMove(testMove);
    System.out.println(startingBoard.toString());
  }
}
