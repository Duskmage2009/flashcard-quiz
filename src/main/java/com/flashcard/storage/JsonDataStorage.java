package com.flashcard.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flashcard.model.Deck;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonDataStorage implements DataStorage {
    private static final String DATA_DIR = "data";
    private static final String DECKS_FILE = "decks.json";

    private Map<String, Deck> decks;
    private ObjectMapper objectMapper;

    public JsonDataStorage() {
        this.decks = new HashMap<>();
        this.objectMapper = createObjectMapper();
        createDataDirectory();
        loadData();
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Enable pretty JSON formatting
          mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Ignore unknown properties when deserializing
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    private void createDataDirectory() {
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
        } catch (IOException e) {
            System.err.println("Помилка створення директорії: " + e.getMessage());
        }
    }

    @Override
    public void saveDeck(Deck deck) {
        decks.put(deck.getId(), deck);
        saveData();
    }

    @Override
    public Deck loadDeck(String id) {
        return decks.get(id);
    }

    @Override
    public List<Deck> loadAllDecks() {
        return new ArrayList<>(decks.values());
    }

    @Override
    public boolean deleteDeck(String id) {
        boolean removed = decks.remove(id) != null;
        if (removed) {
            saveData();
        }
        return removed;
    }

    @Override
    public void saveToFile(String filename) {
        try {
            File file = new File(DATA_DIR, filename);
            objectMapper.writeValue(file, decks.values());
        } catch (IOException e) {
            System.err.println("Помилка збереження в файл: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void loadFromFile(String filename) {
        try {
            File file = new File(DATA_DIR, filename);
            if (file.exists()) {
                // Use TypeReference to correctly deserialize the list of objects
                List<Deck> loadedDecks = objectMapper.readValue(file, new TypeReference<List<Deck>>() {});

                if (loadedDecks != null) {
                    decks.clear();
                    for (Deck deck : loadedDecks) {
                        decks.put(deck.getId(), deck);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Помилка завантаження з файлу: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveData() {
        saveToFile(DECKS_FILE);
    }

    private void loadData() {
        loadFromFile(DECKS_FILE);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void exportDecksToJson(String filename, List<Deck> decksToExport) {
        try {
            File file = new File(DATA_DIR, filename);
            objectMapper.writeValue(file, decksToExport);
            System.out.println("Колоди успішно експортовано в файл: " + filename);
        } catch (IOException e) {
            System.err.println("Помилка експорту колод: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Deck> importDecksFromJson(String filename) {
        try {
            File file = new File(DATA_DIR, filename);
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<List<Deck>>() {});
            }
        } catch (IOException e) {
            System.err.println("Помилка імпорту колод: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}