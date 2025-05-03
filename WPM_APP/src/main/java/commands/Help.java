package commands;

import java.util.ArrayList;
import java.util.List;



/**
 * The Help class provides functionality to format and print a help message
 * It ensures that the help message is neatly formatted with a fixed line width
 */
public class Help{

    /**
     * The maximum length of a line in the help message
     */
    static final int MAX_LINE_LENGTH = 80;

    //------------------------------------------------------------------------------------------------------------------


    /**
     * Prints the formatted help message to the console
     * The message is split into lines that adhere to the {@code MAX_LINE_LENGTH}
     */
    public static void printOutHelpOptionMessage() {
        System.out.println("\r\n" + splitIntoLines(helpOptionString) + "\r\n");
    }

    /**
     * Splits the given text into multiple lines, ensuring that each line does not exceed
     * the {@code MAX_LINE_LENGTH}
     *
     * @param text The text to be split into lines
     * @return A formatted string with line breaks
     */
    private static String splitIntoLines(String text) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        for (String word : text.split(" ")) {
            if (line.length() + word.length() + 1 > MAX_LINE_LENGTH) {
                lines.add(line.toString().trim());
                line.setLength(0);
            }
            line.append(word).append(" ");
        }
        if (!line.isEmpty()) {
            lines.add(line.toString().trim());
        }

        return String.join("\n", lines);
    }


    /**
     * The predefined help message text
     */
    private static final String helpOptionString = "This is the internal Help option message";

}
