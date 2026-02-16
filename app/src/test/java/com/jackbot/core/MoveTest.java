package com.jackbot.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MoveTest {

  @Test
  void testBasicMove() {
    String expected = "b1c3";
    Move test = Move.of(1, 18);
    assertEquals(expected, test.toString());
    assertFalse(test.isPromotion());
    assertFalse(test.isCapture());
    assertFalse(test.isDoublePush());
    assertFalse(test.isEnPassant());
    assertFalse(test.isCastle());
  }

  @Test
  void testPawnDoublePush() {
    String expected = "e2e4";
    Move test = Move.doublePawnPush(12, 28);
    assertEquals(expected, test.toString());
    assertFalse(test.isPromotion());
    assertFalse(test.isCapture());
    assertTrue(test.isDoublePush());
    assertFalse(test.isEnPassant());
    assertFalse(test.isCastle());
  }

  @Test
  void testCapture() {
    String expected = "e4d5";
    Move test = Move.capture(28, 35);
    assertEquals(expected, test.toString());
    assertFalse(test.isPromotion());
    assertTrue(test.isCapture());
    assertFalse(test.isDoublePush());
    assertFalse(test.isEnPassant());
    assertFalse(test.isCastle());
  }

  @Test
  void testCastling() {
    String expected = "e1g1";
    Move test = Move.castle(4, 6);
    assertEquals(expected, test.toString());
    assertFalse(test.isPromotion());
    assertFalse(test.isCapture());
    assertFalse(test.isDoublePush());
    assertFalse(test.isEnPassant());
    assertTrue(test.isCastle());
  }

  @Test
  void testPromotion() {
    // Test normal promotion
    String expected = "e7e8q";
    Move test = Move.promotion(52, 60, Move.PROMO_Q, false);
    assertEquals(expected, test.toString());
    assertTrue(test.isPromotion());
    assertFalse(test.isCapture());
    assertFalse(test.isDoublePush());
    assertFalse(test.isEnPassant());
    assertFalse(test.isCastle());
    // Test capture promotion
    expected = "g7h8b";
    test = Move.promotion(54, 63, Move.PROMO_B, true);
    assertEquals(expected, test.toString());
    assertTrue(test.isPromotion());
    assertTrue(test.isCapture());
    assertFalse(test.isDoublePush());
    assertFalse(test.isEnPassant());
    assertFalse(test.isCastle());
  }

  @Test
  void testEnPassant() {
    String expected = "e5d6";
    Move test = Move.enPassant(36, 43);
    assertEquals(expected, test.toString());
    assertFalse(test.isPromotion());
    assertTrue(test.isCapture());
    assertFalse(test.isDoublePush());
    assertTrue(test.isEnPassant());
    assertFalse(test.isCastle());
  }
}
