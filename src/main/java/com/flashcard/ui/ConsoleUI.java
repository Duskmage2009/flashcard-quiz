package com.flashcard.ui;
import com.flashcard.model.Card;
import com.flashcard.model.Deck;
import com.flashcard.service.CardService;
import com.flashcard.service.DeckService;
import com.flashcard.service.QuizService;

import java.util.List;
import java.util.Scanner;
public class ConsoleUI {
    private final Scanner scanner;
    private final DeckService deckService;
    private final CardService cardService;
    private final QuizService quizService;

    public ConsoleUI(DeckService deckService, CardService cardService, QuizService quizService) {
        this.scanner = new Scanner(System.in);
        this.deckService = deckService;
        this.cardService = cardService;
        this.quizService = quizService;
    }

    public void start() {
        System.out.println("=== Ласкаво просимо до Flashcard Quiz! ===");

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    startQuiz();
                    break;
                case 2:
                    manageDeckMenu();
                    break;
                case 3:
                    System.out.println("До побачення!");
                    running = false;
                    break;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== ГОЛОВНЕ МЕНЮ ===");
        System.out.println("1. Почати вивчення");
        System.out.println("2. Управління колодами");
        System.out.println("3. Вихід");
        System.out.print("Ваш вибір: ");
    }

    private void startQuiz() {
        List<Deck> decks = deckService.getAllDecks();
        if (decks.isEmpty()) {
            System.out.println("Немає доступних колод для вивчення.");
            return;
        }

        System.out.println("\n=== ВИБІР КОЛОДИ ===");
        showDecks(decks);
        System.out.print("Введіть номер колоди: ");

        int choice = getIntInput();
        if (choice < 1 || choice > decks.size()) {
            System.out.println("Невірний вибір.");
            return;
        }

        Deck selectedDeck = decks.get(choice - 1);
        if (selectedDeck.getCards().isEmpty()) {
            System.out.println("У цій колоді немає карток.");
            return;
        }

        runQuiz(selectedDeck);
    }

    private void runQuiz(Deck deck) {
        System.out.println("\n=== ВІКТОРИНА: " + deck.getName() + " ===");
        System.out.println("Введіть 'quit' для виходу з вікторини\n");

        int correct = 0;
        int total = 0;

        while (true) {
            Card card = quizService.getRandomCard(deck.getId());
            if (card == null) {
                break;
            }

            System.out.println("Питання: " + card.getQuestion());
            System.out.print("Ваша відповідь: ");
            String answer = scanner.nextLine().trim();

            if ("quit".equalsIgnoreCase(answer)) {
                break;
            }

            total++;
            if (quizService.checkAnswer(card, answer)) {
                System.out.println("✓ Правильно!");
                correct++;
            } else {
                System.out.println("✗ Неправильно. Правильна відповідь: " + card.getAnswer());
            }

            System.out.println("Поточний результат: " + correct + "/" + total);
            System.out.println();
        }

        if (total > 0) {
            double percentage = (double) correct / total * 100;
            System.out.printf("Результат вікторини: %d/%d (%.1f%%)\n", correct, total, percentage);
        }
    }

    private void manageDeckMenu() {
        while (true) {
            System.out.println("\n=== УПРАВЛІННЯ КОЛОДАМИ ===");
            System.out.println("1. Створити колоду");
            System.out.println("2. Переглянути колоди");
            System.out.println("3. Управління картками");
            System.out.println("4. Видалити колоду");
            System.out.println("5. Повернутися до головного меню");
            System.out.print("Ваш вибір: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    createDeck();
                    break;
                case 2:
                    viewDecks();
                    break;
                case 3:
                    manageCards();
                    break;
                case 4:
                    deleteDeck();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Невірний вибір.");
            }
        }
    }

    private void createDeck() {
        System.out.print("Введіть назву колоди: ");
        String name = scanner.nextLine().trim();

        try {
            Deck deck = deckService.createDeck(name);
            System.out.println("Колоду '" + deck.getName() + "' успішно створено!");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private void viewDecks() {
        List<Deck> decks = deckService.getAllDecks();
        if (decks.isEmpty()) {
            System.out.println("Немає створених колод.");
            return;
        }

        System.out.println("\n=== СПИСОК КОЛОД ===");
        showDecks(decks);
    }

    private void showDecks(List<Deck> decks) {
        for (int i = 0; i < decks.size(); i++) {
            Deck deck = decks.get(i);
            System.out.printf("%d. %s (%d карток)\n", i + 1, deck.getName(), deck.getCards().size());
        }
    }

    private void deleteDeck() {
        List<Deck> decks = deckService.getAllDecks();
        if (decks.isEmpty()) {
            System.out.println("Немає колод для видалення.");
            return;
        }

        System.out.println("\n=== ВИДАЛЕННЯ КОЛОДИ ===");
        showDecks(decks);
        System.out.print("Введіть номер колоди для видалення: ");

        int choice = getIntInput();
        if (choice < 1 || choice > decks.size()) {
            System.out.println("Невірний вибір.");
            return;
        }

        Deck deck = decks.get(choice - 1);
        if (deckService.deleteDeck(deck.getId())) {
            System.out.println("Колоду '" + deck.getName() + "' успішно видалено!");
        } else {
            System.out.println("Помилка при видаленні колоди.");
        }
    }

    private void manageCards() {
        List<Deck> decks = deckService.getAllDecks();
        if (decks.isEmpty()) {
            System.out.println("Немає доступних колод.");
            return;
        }

        System.out.println("\n=== ВИБІР КОЛОДИ ===");
        showDecks(decks);
        System.out.print("Введіть номер колоди: ");

        int choice = getIntInput();
        if (choice < 1 || choice > decks.size()) {
            System.out.println("Невірний вибір.");
            return;
        }

        Deck selectedDeck = decks.get(choice - 1);
        manageCardsInDeck(selectedDeck);
    }

    private void manageCardsInDeck(Deck deck) {
        while (true) {
            System.out.println("\n=== УПРАВЛІННЯ КАРТКАМИ: " + deck.getName() + " ===");
            System.out.println("1. Додати картку");
            System.out.println("2. Переглянути картки");
            System.out.println("3. Редагувати картку");
            System.out.println("4. Видалити картку");
            System.out.println("5. Повернутися назад");
            System.out.print("Ваш вибір: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    addCard(deck.getId());
                    break;
                case 2:
                    viewCards(deck.getId());
                    break;
                case 3:
                    editCard(deck.getId());
                    break;
                case 4:
                    deleteCard(deck.getId());
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Невірний вибір.");
            }
        }
    }

    private void addCard(String deckId) {
        System.out.print("Введіть питання: ");
        String question = scanner.nextLine().trim();
        System.out.print("Введіть відповідь: ");
        String answer = scanner.nextLine().trim();

        try {
            cardService.createCard(deckId, question, answer);
            System.out.println("Картку успішно додано!");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private void viewCards(String deckId) {
        List<Card> cards = cardService.getCardsInDeck(deckId);
        if (cards.isEmpty()) {
            System.out.println("У цій колоді немає карток.");
            return;
        }

        System.out.println("\n=== СПИСОК КАРТОК ===");
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            System.out.printf("%d. П: %s | В: %s\n", i + 1, card.getQuestion(), card.getAnswer());
        }
    }

    private void editCard(String deckId) {
        List<Card> cards = cardService.getCardsInDeck(deckId);
        if (cards.isEmpty()) {
            System.out.println("У цій колоді немає карток для редагування.");
            return;
        }

        viewCards(deckId);
        System.out.print("Введіть номер картки для редагування: ");

        int choice = getIntInput();
        if (choice < 1 || choice > cards.size()) {
            System.out.println("Невірний вибір.");
            return;
        }

        Card card = cards.get(choice - 1);
        System.out.print("Нове питання (" + card.getQuestion() + "): ");
        String newQuestion = scanner.nextLine().trim();
        if (newQuestion.isEmpty()) {
            newQuestion = card.getQuestion();
        }

        System.out.print("Нова відповідь (" + card.getAnswer() + "): ");
        String newAnswer = scanner.nextLine().trim();
        if (newAnswer.isEmpty()) {
            newAnswer = card.getAnswer();
        }

        try {
            if (cardService.updateCard(deckId, card.getId(), newQuestion, newAnswer)) {
                System.out.println("Картку успішно оновлено!");
            } else {
                System.out.println("Помилка при оновленні картки.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private void deleteCard(String deckId) {
        List<Card> cards = cardService.getCardsInDeck(deckId);
        if (cards.isEmpty()) {
            System.out.println("У цій колоді немає карток для видалення.");
            return;
        }

        viewCards(deckId);
        System.out.print("Введіть номер картки для видалення: ");

        int choice = getIntInput();
        if (choice < 1 || choice > cards.size()) {
            System.out.println("Невірний вибір.");
            return;
        }

        Card card = cards.get(choice - 1);
        if (cardService.deleteCard(deckId, card.getId())) {
            System.out.println("Картку успішно видалено!");
        } else {
            System.out.println("Помилка при видаленні картки.");
        }
    }

    private int getIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Будь ласка, введіть число: ");
            }
        }
    }
}
