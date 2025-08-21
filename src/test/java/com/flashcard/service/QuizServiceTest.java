package com.flashcard.service;

import static org.junit.jupiter.api.Assertions.*;

import com.flashcard.model.Card;
import com.flashcard.model.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuizService Tests")
class QuizServiceTest {

    @Mock
    private DeckService mockDeckService;

    private QuizService quizService;
    private Deck testDeck;

    @BeforeEach
    void setUp() {
        quizService = new QuizService(mockDeckService);
        testDeck = new Deck("deck-1", "Test Deck");
    }

    @RepeatedTest(10)
    @DisplayName("Should return random card from deck with cards")
    void testGetRandomCard_DeckWithCards_ReturnsCard() {
        Card card1 = new Card("1", "Q1", "A1");
        Card card2 = new Card("2", "Q2", "A2");
        testDeck.addCard(card1);
        testDeck.addCard(card2);

        when(mockDeckService.getDeckById("deck-1")).thenReturn(testDeck);

        Card result = quizService.getRandomCard("deck-1");

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(testDeck.getCards().contains(result))
        );
    }

    @Test
    @DisplayName("Should return null when deck is empty")
    void testGetRandomCard_EmptyDeck_ReturnsNull() {
        when(mockDeckService.getDeckById("deck-1")).thenReturn(testDeck);

        Card result = quizService.getRandomCard("deck-1");

        assertNull(result);
    }

    @Test
    @DisplayName("Should return null when deck is not found")
    void testGetRandomCard_DeckNotFound_ReturnsNull() {
        when(mockDeckService.getDeckById("deck-1")).thenReturn(null);

        Card result = quizService.getRandomCard("deck-1");

        assertNull(result);
    }

    @Test
    @DisplayName("Should return true for correct answer")
    void testCheckAnswer_CorrectAnswer_ReturnsTrue() {
        Card card = new Card("1", "What is 2+2?", "4");

        boolean result = quizService.checkAnswer(card, "4");

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return true for correct answer with different case")
    void testCheckAnswer_CorrectAnswerDifferentCase_ReturnsTrue() {
        Card card = new Card("1", "What is the capital of Ukraine?", "Kyiv");

        boolean result = quizService.checkAnswer(card, "kyiv");

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return true for correct answer with extra whitespace")
    void testCheckAnswer_CorrectAnswerWithWhitespace_ReturnsTrue() {
        Card card = new Card("1", "What is 2+2?", "4");

        boolean result = quizService.checkAnswer(card, "  4  ");

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false for incorrect answer")
    void testCheckAnswer_IncorrectAnswer_ReturnsFalse() {
        Card card = new Card("1", "What is 2+2?", "4");

        boolean result = quizService.checkAnswer(card, "5");

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when card is null")
    void testCheckAnswer_NullCard_ReturnsFalse() {
        boolean result = quizService.checkAnswer(null, "answer");

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when user answer is null")
    void testCheckAnswer_NullAnswer_ReturnsFalse() {
        Card card = new Card("1", "Question", "Answer");

        boolean result = quizService.checkAnswer(card, null);

        assertFalse(result);
    }
}
