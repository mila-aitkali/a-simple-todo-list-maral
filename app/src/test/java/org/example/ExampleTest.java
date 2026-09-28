package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExampleTest {
    @Test
    void printReturnsAnExampleMessage() {
        Example classUnderTest = new Example();
        assertEquals(classUnderTest.print(), "This is an example of a class and a method. You can delete this file and replace it with the code for your lab.");
    }
}
