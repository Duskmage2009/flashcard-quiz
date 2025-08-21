package com.flashcard.service;

import com.flashcard.model.Card;
import com.flashcard.model.Deck;

import java.util.List;
import java.util.Random;

public class QuizService {
    private final DeckService deckService;
    private final Random random;

    public QuizService(DeckService deckService) {
        this.deckService = deckService;
        this.random = new Random();
    }

    public Card getRandomCard(String deckId) {
        Deck deck = deckService.getDeckById(deckId);
        if (deck == null || deck.getCards().isEmpty()) {
            return null;
        }

        List<Card> cards = deck.getCards();
        return cards.get(random.nextInt(cards.size()));
    }

    public boolean checkAnswer(Card card, String userAnswer) {
        if (card == null || userAnswer == null) {
            return false;
        }

        return card.getAnswer().trim().equalsIgnoreCase(userAnswer.trim());
    }
}