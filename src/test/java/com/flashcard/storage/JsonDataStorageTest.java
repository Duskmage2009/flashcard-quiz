package com.flashcard.storage;


import com.flashcard.model.Card;
import com.flashcard.model.Deck;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JsonDataStorage Tests")
class JsonDataStorageTest {

    private JsonDataStorage storage;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        System.setProperty("user.dir", tempDir.toString());
        storage = new JsonDataStorage();
    }

    @AfterEach
    void tearDown() {
        System.clearProperty("user.dir");
    }

    @Test
    @DisplayName("Should save and load deck successfully")
    void testSaveAndLoadDeck_Success() {
        Deck originalDeck = new Deck("deck-1", "Test Deck");
        Card card = new Card("card-1", "Question", "Answer");
        originalDeck.addCard(card);

        storage.saveDeck(originalDeck);
        Deck loadedDeck = storage.loadDeck("deck-1");

        assertAll(
                () -> assertNotNull(loadedDeck),
                () -> assertEquals(originalDeck.getId(), loadedDeck.getId()),
                () -> assertEquals(originalDeck.getName(), loadedDeck.getName()),
                () -> assertEquals(1, loadedDeck.getCards().size()),
                () -> assertEquals(card.getQuestion(), loadedDeck.getCards().get(0).getQuestion()),
                () -> assertEquals(card.getAnswer(), loadedDeck.getCards().get(0).getAnswer())
        );
    }

    @Test
    @DisplayName("Should load all decks correctly")
    void testLoadAllDecks_MultipleDecks_Success() {
        Deck deck1 = new Deck("deck-1", "Deck 1");
        Deck deck2 = new Deck("deck-2", "Deck 2");

        storage.saveDeck(deck1);
        storage.saveDeck(deck2);
        List<Deck> allDecks = storage.loadAllDecks();

        assertAll(
                () -> assertEquals(2, allDecks.size()),
                () -> assertTrue(allDecks.stream().anyMatch(d -> d.getId().equals("deck-1"))),
                () -> assertTrue(allDecks.stream().anyMatch(d -> d.getId().equals("deck-2")))
        );
    }

    @Test
    @DisplayName("Should delete existing deck successfully")
    void testDeleteDeck_ExistingDeck_Success() {
        Deck deck = new Deck("deck-1", "Test Deck");
        storage.saveDeck(deck);

        boolean result = storage.deleteDeck("deck-1");

        assertAll(
                () -> assertTrue(result),
                () -> assertNull(storage.loadDeck("deck-1"))
        );
    }

    @Test
    @DisplayName("Should return false when deleting non-existing deck")
    void testDeleteDeck_NonExistingDeck_ReturnsFalse() {
        boolean result = storage.deleteDeck("non-existing-id");

        assertFalse(result);
    }
}