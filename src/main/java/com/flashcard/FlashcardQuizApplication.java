package com.flashcard;

import com.flashcard.service.CardService;
import com.flashcard.service.DeckService;
import com.flashcard.service.QuizService;
import com.flashcard.storage.DataStorage;
import com.flashcard.storage.JsonDataStorage;
import com.flashcard.ui.ConsoleUI;

public class FlashcardQuizApplication {
    public static void main(String[] args) {
        DataStorage dataStorage = new JsonDataStorage();
        DeckService deckService = new DeckService(dataStorage);
        CardService cardService = new CardService(deckService);
        QuizService quizService = new QuizService(deckService);

        ConsoleUI ui = new ConsoleUI(deckService, cardService, quizService);
        ui.start();
    }
}