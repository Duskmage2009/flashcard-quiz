package com.flashcard.service;

import com.flashcard.model.Deck;
import com.flashcard.storage.DataStorage;

import java.util.List;
import java.util.UUID;

public class DeckService {
    private final DataStorage dataStorage;

    public DeckService(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    public Deck createDeck(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва колоди не може бути порожньою");
        }

        String id = UUID.randomUUID().toString();
        Deck deck = new Deck(id, name.trim());
        dataStorage.saveDeck(deck);
        return deck;
    }

    public List<Deck> getAllDecks() {
        return dataStorage.loadAllDecks();
    }

    public Deck getDeckById(String id) {
        return dataStorage.loadDeck(id);
    }

    public boolean deleteDeck(String id) {
        return dataStorage.deleteDeck(id);
    }

    public void updateDeck(Deck deck) {
        dataStorage.saveDeck(deck);
    }
}