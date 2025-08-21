package com.flashcard.service;

import com.flashcard.model.Card;
import com.flashcard.model.Deck;

import java.util.List;
import java.util.UUID;

public class CardService {
    private final DeckService deckService;

    public CardService(DeckService deckService) {
        this.deckService = deckService;
    }

    public Card createCard(String deckId, String question, String answer) {
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("Питання не може бути порожнім");
        }
        if (answer == null || answer.trim().isEmpty()) {
            throw new IllegalArgumentException("Відповідь не може бути порожньою");
        }

        Deck deck = deckService.getDeckById(deckId);
        if (deck == null) {
            throw new IllegalArgumentException("Колода не знайдена");
        }

        String cardId = UUID.randomUUID().toString();
        Card card = new Card(cardId, question.trim(), answer.trim());
        deck.addCard(card);
        deckService.updateDeck(deck);
        return card;
    }

    public List<Card> getCardsInDeck(String deckId) {
        Deck deck = deckService.getDeckById(deckId);
        return deck != null ? deck.getCards() : List.of();
    }

    public boolean updateCard(String deckId, String cardId, String newQuestion, String newAnswer) {
        if (newQuestion == null || newQuestion.trim().isEmpty()) {
            throw new IllegalArgumentException("Питання не може бути порожнім");
        }
        if (newAnswer == null || newAnswer.trim().isEmpty()) {
            throw new IllegalArgumentException("Відповідь не може бути порожньою");
        }

        Deck deck = deckService.getDeckById(deckId);
        if (deck == null) {
            return false;
        }

        Card card = deck.findCardById(cardId);
        if (card == null) {
            return false;
        }

        card.setQuestion(newQuestion.trim());
        card.setAnswer(newAnswer.trim());
        deckService.updateDeck(deck);
        return true;
    }

    public boolean deleteCard(String deckId, String cardId) {
        Deck deck = deckService.getDeckById(deckId);
        if (deck == null) {
            return false;
        }

        boolean removed = deck.removeCard(cardId);
        if (removed) {
            deckService.updateDeck(deck);
        }
        return removed;
    }
}