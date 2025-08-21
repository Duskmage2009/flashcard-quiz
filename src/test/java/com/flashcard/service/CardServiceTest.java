package com.flashcard.service;

import static org.junit.jupiter.api.Assertions.*;

import com.flashcard.model.Card;
import com.flashcard.model.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CardService Tests")
class CardServiceTest {

    @Mock
    private DeckService mockDeckService;

    private CardService cardService;
    private Deck testDeck;

    @BeforeEach
    void setUp() {
        cardService = new CardService(mockDeckService);
        testDeck = new Deck("deck-1", "Test Deck");
    }

    @Test
    @DisplayName("Should create card with valid data")
    void testCreateCard_ValidData_Success() {
        String question = "What is 2+2?";
        String answer = "4";
        when(mockDeckService.getDeckById("deck-1")).thenReturn(testDeck);

        Card result = cardService.createCard("deck-1", question, answer);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(question, result.getQuestion()),
                () -> assertEquals(answer, result.getAnswer()),
                () -> assertNotNull(result.getId()),
                () -> verify(mockDeckService).updateDeck(testDeck),
                () -> assertEquals(1, testDeck.getCards().size())
        );
    }

    @Test
    @DisplayName("Should throw exception when question is empty")
    void testCreateCard_EmptyQuestion_ThrowsException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cardService.createCard("deck-1", "", "Answer")
        );

        assertEquals("Питання не може бути порожнім", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when answer is empty")
    void testCreateCard_EmptyAnswer_ThrowsException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cardService.createCard("deck-1", "Question", "")
        );

        assertEquals("Відповідь не може бути порожньою", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when deck is not found")
    void testCreateCard_DeckNotFound_ThrowsException() {
        when(mockDeckService.getDeckById("deck-1")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cardService.createCard("deck-1", "Question", "Answer")
        );

        assertEquals("Колода не знайдена", exception.getMessage());
    }

    @Test
    @DisplayName("Should return cards in existing deck")
    void testGetCardsInDeck_ExistingDeck_ReturnsCards() {
        // Given
        Card card1 = new Card("1", "Q1", "A1");
        Card card2 = new Card("2", "Q2", "A2");
        testDeck.addCard(card1);
        testDeck.addCard(card2);

        when(mockDeckService.getDeckById("deck-1")).thenReturn(testDeck);

        List<Card> result = cardService.getCardsInDeck("deck-1");

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.contains(card1)),
                () -> assertTrue(result.contains(card2))
        );
    }

    @Test
    @DisplayName("Should return empty list when deck not found")
    void testGetCardsInDeck_DeckNotFound_ReturnsEmptyList() {
        when(mockDeckService.getDeckById("deck-1")).thenReturn(null);

        List<Card> result = cardService.getCardsInDeck("deck-1");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should update card with valid data")
    void testUpdateCard_ValidData_Success() {
        Card card = new Card("card-1", "Old Question", "Old Answer");
        testDeck.addCard(card);
        when(mockDeckService.getDeckById("deck-1")).thenReturn(testDeck);

        boolean result = cardService.updateCard("deck-1", "card-1", "New Question", "New Answer");

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals("New Question", card.getQuestion()),
                () -> assertEquals("New Answer", card.getAnswer()),
                () -> verify(mockDeckService).updateDeck(testDeck)
        );
    }

    @Test
    @DisplayName("Should return false when updating card in non-existing deck")
    void testUpdateCard_DeckNotFound_ReturnsFalse() {
        when(mockDeckService.getDeckById("deck-1")).thenReturn(null);

        boolean result = cardService.updateCard("deck-1", "card-1", "Question", "Answer");

        assertFalse(result);
    }

    @Test
    @DisplayName("Should delete existing card")
    void testDeleteCard_ExistingCard_Success() {
        Card card = new Card("card-1", "Question", "Answer");
        testDeck.addCard(card);
        when(mockDeckService.getDeckById("deck-1")).thenReturn(testDeck);

        boolean result = cardService.deleteCard("deck-1", "card-1");

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(0, testDeck.getCards().size()),
                () -> verify(mockDeckService).updateDeck(testDeck)
        );
    }

    @Test
    @DisplayName("Should return false when deleting card from non-existing deck")
    void testDeleteCard_DeckNotFound_ReturnsFalse() {
        when(mockDeckService.getDeckById("deck-1")).thenReturn(null);

        boolean result = cardService.deleteCard("deck-1", "card-1");

        assertFalse(result);
    }
}