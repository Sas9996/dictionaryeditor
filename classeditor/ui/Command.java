

package edu.kit.informatik.classeditor.ui;

import edu.kit.informatik.classeditor.database.Database;
import edu.kit.informatik.classeditor.entity.Attribute;
import edu.kit.informatik.classeditor.entity.Class;
import edu.kit.informatik.classeditor.entity.Construct;
import edu.kit.informatik.classeditor.entity.Enum;
import edu.kit.informatik.classeditor.entity.Final;
import edu.kit.informatik.classeditor.entity.Interface;
import edu.kit.informatik.classeditor.entity.Method;
import edu.kit.informatik.classeditor.entity.NameableConstruct;
import edu.kit.informatik.classeditor.entity.Type;
import edu.kit.informatik.classeditor.entity.VisibilityModifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * List of available commands with their command line interaction expressions.
 *
 * @author Sara
 * @version 1.0
 */
public enum Command {
    /**
     * Adds the given construct to the given database if possible.
     */
    ADD_CONSTRUCT(
            "add-construct" + Main.COMMAND_SEPARATOR + Construct.getConstructPattern() + Main.SEPARATOR + Construct
                    .getConstructNamePattern()) {
        @Override public String execute(final Matcher input, final Database database) {
            final String construct = input.group(Main.FIRST_PARAMETER_INDEX);
            final String constructName = input.group(Main.FIRST_PARAMETER_INDEX + 1);
            switch (construct) {
                case Class.PATTERN:
                    return database.addClass(new Class(constructName))
                            ? Main.OK
                            : Main.ERROR + "given class could not be added";
                case Interface.PATTERN:
                    return database.addInterface(new Interface(constructName))
                            ? Main.OK
                            : Main.ERROR + "given interface could not " + "be added";
                case Enum.PATTERN:
                    return database.addEnum(new Enum(constructName))
                            ? Main.OK
                            : Main.ERROR + "given enum could not " + "be added";
                default:
                    return Main.ERROR + "an unknown error occurred";
            }
        }
    },

    /**
     * Adds an inheritance relationship between the first and the second argument for this command if possible.
     */
    ADD_EXTENDS(
            "add-extends" + Main.COMMAND_SEPARATOR + Construct.getConstructNamePattern() + Main.SEPARATOR + Construct
                    .getConstructNamePattern()) {
        @Override public String execute(final Matcher input, final Database database) {
            final NameableConstruct child = database.getConstruct(input.group(Main.FIRST_PARAMETER_INDEX));
            final NameableConstruct parent = database.getConstruct(input.group(Main.FIRST_PARAMETER_INDEX + 1));
            if (child == null) {
                return Main.ERROR + "no child class with name " + input.group(Main.FIRST_PARAMETER_INDEX) + " found";
            }
            if (parent == null) {
                return Main.ERROR + "no parent class with name " + input.group(Main.FIRST_PARAMETER_INDEX + 1) + " "
                       + "found";
            }
            return child.addExtend(parent)
                    ? Main.OK
                    : Main.ERROR + "parent class could not be added";
        }
    },

    /**
     * Adds an implements relationship between the first and the second argument for this command if possible.
     */
    ADD_IMPLEMENTS(
            "add-implements" + Main.COMMAND_SEPARATOR + Construct.getConstructNamePattern() + Main.SEPARATOR + Construct
                    .getConstructNamePattern()) {
        @Override public String execute(final Matcher input, final Database database) {
            final NameableConstruct child = database.getConstruct(input.group(Main.FIRST_PARAMETER_INDEX));
            final NameableConstruct parent = database.getConstruct(input.group(Main.FIRST_PARAMETER_INDEX + 1));
            if (child == null) {
                return Main.ERROR + "no class with that name found";
            }
            if (parent == null) {
                return Main.ERROR + "no interface with that name found";
            }
            return child.addImplement(parent)
                    ? Main.OK
                    : Main.ERROR + "interface could not be added";
        }
    },

    /**
     * Adds the given attribute to the given construct if possible.
     */
    ADD_ATTRIBUTE("add-attribute" + Main.COMMAND_SEPARATOR + Attribute.ATTRIBUTE_SIGNATURE_PATTERN) {
        @Override public String execute(final Matcher input, final Database database) {
            final NameableConstruct construct = database.getConstruct(input.group(Attribute.CONSTRUCT_NAME_INDEX));
            if (construct == null) {
                return Main.ERROR + "no construct with that name found";
            }
            final Type type = database.typeAvailable(input.group(Attribute.TYPE_INDEX));
            final VisibilityModifier modifier = VisibilityModifier
                    .parseFromString(input.group(Attribute.VISIBILITY_MODIFIER_INDEX));
            final Final afinal = Final.parseFromString(input.group(Attribute.FINAL_INDEX));
            if (type == null || modifier == null || afinal == null) {
                return Main.ERROR + "could not parse modifiers or type";
            }
            final Attribute attribute = new Attribute(input.group(Attribute.NAME_INDEX), modifier, afinal, type,
                    construct);
            return construct.addAttribute(attribute)
                    ? Main.OK
                    : Main.ERROR + "could not add attribute";
        }
    },

    /**
     * Adds the given method to the given construct if possible.
     */
    ADD_METHOD("add-method" + Main.COMMAND_SEPARATOR + Method.METHOD_SIGNATURE_PATTERN) {
        @Override public String execute(final Matcher input, final Database database) {
            final String constructName = input.group(Method.CONSTRUCT_INDEX);

            final NameableConstruct construct = database.getConstruct(constructName);
            if (construct == null) {
                return Main.ERROR + "no construct with that name found";
            }
            final Method method = Method.parseFromString(input, database);
            return Database.addMethod(construct, method);
        }
    },

    /**
     * Lists all available constructs, returns an error message if no constructs are available.
     */
    LIST_CONSTRUCTS("list-constructs") {
        @Override public String execute(final Matcher input, final Database database) {
            final String output = database.listConstructs(Main.LINE_SEPARATOR);
            return output.equals(Main.EMPTY_STRING)
                    ? Main.ERROR + "no constructs available"
                    : output;
        }
    },

    /**
     * Lists all available attributes for a construct, returns an error message if no attributes are available.
     */
    LIST_ATTRIBUTES("list-attributes" + Main.COMMAND_SEPARATOR + Construct.getConstructNamePattern()) {
        @Override public String execute(final Matcher input, final Database database) {
            final String output = database.listAttributes(input.group(Main.FIRST_PARAMETER_INDEX));
            return output != null
                    ? output
                    : Main.ERROR + "could not find construct";
        }
    },

    /**
     * Lists all available methods for a construct, returns an error message if no methods are available.
     */
    LIST_METHODS("list-methods" + Main.COMMAND_SEPARATOR + Construct.getConstructNamePattern()) {
        @Override public String execute(final Matcher input, final Database database) {
            final String output = database.listMethods(input.group(Main.FIRST_PARAMETER_INDEX));
            return output != null
                    ? output
                    : Main.ERROR + "could not find construct";
        }
    },

    /**
     * Lists all available methods with the given name for a construct, returns an error message if no methods are
     * available.
     */
    FIND_METHOD_BY_NAME("find-method-by-name" + Main.COMMAND_SEPARATOR + Construct.getConstructNamePattern() + Construct
            .getConstructSeparator() + Method.METHOD_NAME_PATTERN) {
        @Override public String execute(final Matcher input, final Database database) {
            final NameableConstruct construct = database.getConstruct(input.group(Attribute.CONSTRUCT_NAME_INDEX));
            if (construct == null) {
                return Main.ERROR + "no construct with that name found";
            }
            return Database.findMethod(construct, input.group(Main.FIRST_PARAMETER_INDEX + 1));
        }
    },

    /**
     * Lists all available attributes (only shadowing and normal ones) for a construct, returns an error message if no
     * attributes are available.
     */
    LIST_ALL_ATTRIBUTES("list-all-attributes" + Main.COMMAND_SEPARATOR + Construct.getConstructNamePattern()) {
        @Override public String execute(final Matcher input, final Database database) {
            final String output = database.listAllAttributes(input.group(Main.FIRST_PARAMETER_INDEX));
            return output != null
                    ? output
                    : Main.ERROR + "could not find construct";
        }
    },

    /**
     * Lists all available attributes (shadowing and shadowed ones) for a construct, returns an error message if no
     * attributes are available.
     */
    LIST_SHADOWING_ATTRIBUTES(
            "list-shadowing-attributes" + Main.COMMAND_SEPARATOR + Construct.getConstructNamePattern()) {
        @Override public String execute(final Matcher input, final Database database) {
            final String output = database.listShadowingAttributes(input.group(Main.FIRST_PARAMETER_INDEX));
            return output != null
                    ? output
                    : Main.ERROR + "could not find construct";
        }
    },

    /**
     * Lists all available methods (shadowing and normal ones) for a construct, returns an error message if no
     * methods are available.
     */
    LIST_ALL_METHODS("list-all-methods" + Main.COMMAND_SEPARATOR + Construct.getConstructNamePattern()) {
        @Override public String execute(final Matcher input, final Database database) {
            final String output = database.listAllMethods(input.group(Main.FIRST_PARAMETER_INDEX));
            return output != null
                    ? output
                    : Main.ERROR + "could not find construct";
        }
    },

    /**
     * Lists all available methods with the given signature (shadowing and shadowed ones) for a construct, returns an
     * error message if no methods are available.
     */
    FIND_METHOD_OVERRIDE(
            "find-method-override" + Main.COMMAND_SEPARATOR + Construct.getConstructNamePattern() + Construct
                    .getConstructSeparator() + Method.METHOD_NAME_PATTERN + Method.METHOD_PARAMETER_PATTERN
            + Method.RETURN_TYPE_SEPARATOR + Type.getTypePattern()) {
        @Override public String execute(final Matcher input, final Database database) {
            final Method method = Method.parseFromStringOverrideFormat(input, database);
            final String output = database.findMethodOverride(input.group(Main.FIRST_PARAMETER_INDEX), method);
            return output != null
                    ? output
                    : Main.ERROR + "could not find construct";
        }
    },

    /**
     * Quits the program.
     */
    QUIT("quit") {
        @Override public String execute(final Matcher input, final Database database) {
            database.quit();
            return null;
        }
    };

    /**
     * String constant containing an error message for the case that no command
     * could be found in this enum.
     */
    public static final String COMMAND_NOT_FOUND = Main.ERROR + "command not found!";

    /**
     * The pattern of this command.
     */
    private final Pattern pattern;

    /**
     * Instantiates a new command with the given String. The given String must be a
     * compilable {@link Pattern}.
     *
     * @param pattern the pattern of this command
     */
    Command(final String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * Executes the command contained in the input if there is any, returns an error
     * message otherwise. If a command is found in the input, returns the result of
     * this input performed on the given playlist.
     *
     * @param input the line of input
     * @param database the database the command is executed on
     *
     * @return the result of the command execution, may contain error messages or be
     *         null if there is no output
     */
    public static String executeCommand(final String input, final Database database) {
        for (final Command command : Command.values()) {
            final Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                return command.execute(matcher, database);
            }
        }
        return COMMAND_NOT_FOUND;
    }

    /**
     * Executes the given input on the given playlist.
     *
     * @param input the line of input
     * @param database the playlist the command is executed on
     *
     * @return the result of the command execution, may contain error messages or be
     *         null if there is no output
     */
    abstract String execute(Matcher input, Database database);
}
