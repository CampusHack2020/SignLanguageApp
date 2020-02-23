package mquinn.sign_language.dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Content {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Word> ITEMS = new ArrayList<Word>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Word> ITEM_MAP = new HashMap<String, Word>();

    public static ArrayList<String> wordList;

    private static void addWords(){
        wordList = new ArrayList<String>();
        wordList.add("Hello");
        wordList.add("And");
        wordList.add("You");
        wordList.add("That");
        wordList.add("How");
        wordList.add("Who");
        wordList.add("Why");
        wordList.add("What");
        wordList.add("The");
        wordList.add("To");
        wordList.add("This");
        wordList.add("Are");
        wordList.add("From");
        wordList.add("About");
        wordList.add("All");
        wordList.add("Also");
        wordList.add("Because");
        wordList.add("But");
        wordList.add("Can");
        wordList.add("Come");
        wordList.add("Could");
        wordList.add("Even");
        wordList.add("Find");
        wordList.add("First");
        wordList.add("Give");
        wordList.add("Here");
        wordList.add("Know");
        wordList.add("Just");
        wordList.add("Look");
        wordList.add("Make");
        wordList.add("Many");
        wordList.add("Think");
        wordList.add("Those");
        wordList.add("Antidisestablishmentarianism");
        Collections.sort(wordList);
    }

    static {
        addWords();
        // Add some sample items.
        for (int i = 0; i < wordList.size(); i++) {
            addItem(String.valueOf(i+1), createItem(i+1, wordList.get(i)));
        }
    }

    private static void addItem(String id, Word word) {
        ITEMS.add(word);
        ITEM_MAP.put(id, word);
    }

    private static Word createItem(int id, String word) {
        return new Word(String.valueOf(id), word, makeDetails(word));
    }

    private static String makeDetails(String word) {
        StringBuilder builder = new StringBuilder();
        builder.append("How to spell " + word + ": ");

//        for (int i = 0; i < word.split("(?!^)").length; i++) {
//            String character = word.split("(?!^)")[i];
//
//        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Word {
        public final String id;
        public final String word;
        public final String details;

        public Word(String id, String word, String details) {
            this.id = id;
            this.word = word;
            this.details = details;
        }

        @Override
        public String toString() {
            return word;
        }
    }
}
