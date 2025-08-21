package com.flashcard.service;

import com.flashcard.model.Deck;
import com.flashcard.storage.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeckService Tests")
class DeckServiceTest {

    @Mock
    private DataStorage mockStorage;

    private DeckService deckService;

    @BeforeEach
    void setUp() {
        deckService = new DeckService(mockStorage);
    }

    @Test
    @DisplayName("Should create deck with valid name")
    void testCreateDeck_ValidName_Success() {
        String deckName = "Test Deck";

        Deck result = deckService.createDeck(deckName);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(deckName, result.getName()),
                () -> assertNotNull(result.getId()),
                () -> verify(mockStorage).saveDeck(result)
        );
    }

    @Test
    @DisplayName("Should throw exception when deck name is null")
    void testCreateDeck_NullName_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> deckService.createDeck(null)
        );

        assertEquals("Назва колоди не може бути порожньою", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when deck name is empty")
    void testCreateDeck_EmptyName_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> deckService.createDeck("")
        );

        assertEquals("Назва колоди не може бути порожньою", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when deck name is whitespace only")
    void testCreateDeck_WhitespaceName_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> deckService.createDeck("   ")
        );

        assertEquals("Назва колоди не може бути порожньою", exception.getMessage());
    }

    @Test
    @DisplayName("Should return all decks from storage")
    void testGetAllDecks_ReturnsCorrectList() {
        List<Deck> expectedDecks = List.of(
                new Deck("1", "Deck 1"),
                new Deck("2", "Deck 2")
        );
        when(mockStorage.loadAllDecks()).thenReturn(expectedDecks);

        List<Deck> result = deckService.getAllDecks();

        assertAll(
                () -> assertEquals(expectedDecks, result),
                () -> verify(mockStorage).loadAllDecks()
        );
    }

    @Test
    @DisplayName("Should return deck by ID")
    void testGetDeckById_ExistingDeck_ReturnsDeck() {
        String deckId = "test-id";
        Deck expectedDeck = new Deck(deckId, "Test Deck");
        when(mockStorage.loadDeck(deckId)).thenReturn(expectedDeck);

        Deck result = deckService.getDeckById(deckId);

        assertAll(
                () -> assertEquals(expectedDeck, result),
                () -> verify(mockStorage).loadDeck(deckId)
        );
    }

    @Test
    @DisplayName("Should delete existing deck")
    void testDeleteDeck_ExistingDeck_ReturnsTrue() {
        String deckId = "test-id";
        when(mockStorage.deleteDeck(deckId)).thenReturn(true);

        boolean result = deckService.deleteDeck(deckId);

        assertAll(
                () -> assertTrue(result),
                () -> verify(mockStorage).deleteDeck(deckId)
        );
    }

    @Test
    @DisplayName("Should return false when deleting non-existing deck")
    void testDeleteDeck_NonExistingDeck_ReturnsFalse() {
        String deckId = "non-existing-id";
        when(mockStorage.deleteDeck(deckId)).thenReturn(false);

        boolean result = deckService.deleteDeck(deckId);

        assertAll(
                () -> assertFalse(result),
                () -> verify(mockStorage).deleteDeck(deckId)
        );
    }

    @Test
    @DisplayName("Should update deck")
    void testUpdateDeck_ValidDeck_Success() {
        Deck deck = new Deck("test-id", "Test Deck");
        deckService.updateDeck(deck);
        verify(mockStorage).saveDeck(deck);
    }
}