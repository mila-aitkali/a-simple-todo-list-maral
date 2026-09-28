package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Scanner;
import org.example.TodoList.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for TodoList (JUnit 5).
 */
public class ExampleTest {
    private TodoList list;

    @BeforeEach
    void setUp() {
        list = new TodoList();
    }

    // ----- add -----

    @Test
    void addValidTask() {
        assertTrue(list.add("Buy milk"));
        assertEquals(1, list.size());
        assertEquals("Buy milk", list.all().get(0).getDescription());
    }

    @Test
    void addTrimsWhitespace() {
        list.add("   Buy milk   ");
        assertEquals("Buy milk", list.all().get(0).getDescription());
    }

    @Test
    void addRejectsNullEmptyAndBlank() {
        assertFalse(list.add(null));
        assertFalse(list.add(""));
        assertFalse(list.add("    "));
        assertEquals(0, list.size());
    }

    @Test
    void addRejectsDuplicatesIgnoringCase() {
        list.add("Buy milk");
        assertFalse(list.add("BUY MILK"));
        assertEquals(1, list.size());
    }

    @Test
    void newTaskStartsIncomplete() {
        list.add("Buy milk");
        assertFalse(list.all().get(0).isCompleted());
    }

    // ----- complete(String) / complete(int) -----

    @Test
    void completeByName() {
        list.add("Buy eggs");
        assertTrue(list.complete("buy eggs"));
        assertTrue(list.all().get(0).isCompleted());
    }

    @Test
    void completeByNumber() {
        list.add("Buy milk");
        list.add("Buy eggs");
        assertTrue(list.complete(2));
        assertTrue(list.all().get(1).isCompleted());
        assertFalse(list.all().get(0).isCompleted());
    }

    @Test
    void completeRejectsInvalidInput() {
        list.add("Buy milk");
        assertFalse(list.complete((String) null));
        assertFalse(list.complete(""));
        assertFalse(list.complete("Walk the dog"));
        assertFalse(list.complete(0));
        assertFalse(list.complete(5));
        assertFalse(list.complete(-1));
    }

    @Test
    void completeTwiceReturnsFalse() {
        list.add("Buy milk");
        assertTrue(list.complete("Buy milk"));
        assertFalse(list.complete("Buy milk"));
    }

    // ----- all / complete() / incomplete() -----

    @Test
    void viewsSplitTasksCorrectly() {
        list.add("Buy milk");
        list.add("Buy eggs");
        list.add("Sow beet seeds");
        list.complete("Buy eggs");

        assertEquals(3, list.all().size());

        List<Task> done = list.complete();
        assertEquals(1, done.size());
        assertEquals("Buy eggs", done.get(0).getDescription());

        List<Task> notDone = list.incomplete();
        assertEquals(2, notDone.size());
        assertEquals("Buy milk", notDone.get(0).getDescription());
        assertEquals("Sow beet seeds", notDone.get(1).getDescription());
    }

    @Test
    void viewsOnEmptyListReturnEmpty() {
        assertTrue(list.all().isEmpty());
        assertTrue(list.complete().isEmpty());
        assertTrue(list.incomplete().isEmpty());
    }

    @Test
    void returnedListsCannotModifyTheTodoList() {
        list.add("Buy milk");
        assertThrows(UnsupportedOperationException.class,
                () -> list.all().add(new Task("Hack")));
        assertEquals(1, list.size());
    }

    // ----- clear -----

    @Test
    void clearRemovesEverything() {
        list.add("Buy milk");
        list.add("Buy eggs");
        list.complete("Buy eggs");
        list.clear();
        assertEquals(0, list.size());
        assertTrue(list.complete().isEmpty());
        assertTrue(list.incomplete().isEmpty());
    }

    @Test
    void clearOnEmptyListIsSafe() {
        assertDoesNotThrow(() -> list.clear());
    }

    @Test
    void canAddAfterClear() {
        list.add("Buy milk");
        list.clear();
        assertTrue(list.add("Buy milk"));
        assertEquals(1, list.size());
    }

    // ----- App (interactive menu) -----

    @Test
    void appHandlesNormalSession() {
        String input = String.join("\n",
                "1", "Buy milk",
                "1", "Buy eggs",
                "2", "2",
                "3", "4", "5",
                "6", "y",
                "3",
                "0") + "\n";
        assertDoesNotThrow(() -> App.run(new Scanner(input)));
    }

    @Test
    void appSurvivesInvalidInput() {
        String input = String.join("\n",
                "", "abc", "99", "-1", "3.5", "!!!",     // bad menu choices
                "2",                                     // complete with an empty list
                "1", "",                                 // add a blank task
                "1", "Buy milk",
                "1", "BUY MILK",                         // duplicate
                "2", "999999999999999999999",            // number too large for an int
                "2", "0",                                // task number 0
                "2", "Walk the dog",                     // unknown task
                "6", "maybe", "n",                       // bad then "no" confirmation
                "0") + "\n";
        assertDoesNotThrow(() -> App.run(new Scanner(input)));
    }

    @Test
    void appStopsCleanlyWhenInputEnds() {
        // Input ends in the middle of adding a task (like pressing Ctrl+D).
        assertDoesNotThrow(() -> App.run(new Scanner("1")));
        assertDoesNotThrow(() -> App.run(new Scanner("")));
    }
}