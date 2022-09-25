package org.glagan.Core;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Input {
    public static String ask(String description, String prefix) {
        if (description != null) {
            System.out.println(description);
        }
        // Scanner should *not* be closed since it's stdin and will close it for the
        // -- remaining of the program
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        String input = null;
        try {
            if (prefix != null) {
                System.out.print(prefix);
            } else {
                System.out.print("command: ");
            }
            input = scanner.nextLine();
        } catch (IllegalStateException | NoSuchElementException e) {
            // Ignore expected errors
        }
        return input;
    }
}
