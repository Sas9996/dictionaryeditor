

package edu.kit.informatik.classeditor.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.classeditor.database.Database;

/**
 * Main class for the first task of the third assignment. Contains the entry
 * point and input/output constants.
 *
 * @author Sara
 * @version 1.0
 */
public class Main {
    /**
     * The separator between a command and the parameters.
     */
    public static final String COMMAND_SEPARATOR = " ";

    /**
     * Index of the first parameter.
     */
    public static final int FIRST_PARAMETER_INDEX = 1;

    /**
     * The separator between lines of output.
     */
    public static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * The separator between different parameters for the command line input.
     */
    public static final String SEPARATOR = " ";

    /**
     * An empty string used for some outputs.
     */
    public static final String EMPTY_STRING = "";

    /**
     * The start of an output string for a failed operation.
     */
    public static final String ERROR = "Error, ";

    /**
     * The output string for a succeeded operation.
     */
    public static final String OK = "OK";

    /**
     * Entry point to the program. Checks the given input and produces corresponding
     * output.
     *
     * @param args IGNORED
     */
    public static void main(final String[] args) {
        final Database database = new Database();
        while (database.isActive()) {
            final String input = Terminal.readLine();
            final String output = Command.executeCommand(input, database);
            if (output != null) {
                Terminal.printLine(output);
            }
        }
    }
}
