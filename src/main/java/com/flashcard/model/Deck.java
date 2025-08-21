package com.flashcard.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Deck {
    private String id;
    private String name;
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public Deck(String id, String name) {
        this.id = id;
        this.name = name;
        this.cards = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public boolean removeCard(String cardId) {
        return cards.removeIf(card -> card.getId().equals(cardId));
    }

    public Card findCardById(String cardId) {
        return cards.stream()
                .filter(card -> card.getId().equals(cardId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return Objects.equals(id, deck.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Deck{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cards=" + cards.size() +
                '}';
    }
}