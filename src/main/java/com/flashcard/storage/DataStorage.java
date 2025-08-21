package com.flashcard.storage;

import com.flashcard.model.Deck;

import java.util.List;

public interface DataStorage {
    void saveDeck(Deck deck);
    Deck loadDeck(String id);
    List<Deck> loadAllDecks();
    boolean deleteDeck(String id);
    void saveToFile(String filename);
    void loadFromFile(String filename);
}