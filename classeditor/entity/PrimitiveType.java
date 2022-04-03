

package edu.kit.informatik.classeditor.entity;

/**
 * Encapsulates the used primitive types and {@link String} as described here (https://docs.oracle
 * .com/javase/tutorial/java/nutsandbolts/datatypes.html).
 *
 * @author Sara
 * @version 1.0
 */
public enum PrimitiveType implements Type {
    /**
     * The byte data type is an 8-bit signed two's complement integer.
     */
    BYTE {
        @Override public String getType() {
            return "byte";
        }
    },

    /**
     * The boolean data type has only two possible values: true and false.
     */
    BOOLEAN {
        @Override public String getType() {
            return "boolean";
        }
    },

    /**
     * The short data type is a 16-bit signed two's complement integer.
     */
    SHORT {
        @Override public String getType() {
            return "short";
        }
    },
    /**
     * By default, the int data type is a 32-bit signed two's complement integer.
     */
    INT {
        @Override public String getType() {
            return "int";
        }
    },
    /**
     * The long data type is a 64-bit two's complement integer.
     */
    LONG {
        @Override public String getType() {
            return "long";
        }
    },
    /**
     * The float data type is a single-precision 32-bit IEEE 754 floating point.
     */
    FLOAT {
        @Override public String getType() {
            return "float";
        }
    },
    /**
     * The double data type is a double-precision 64-bit IEEE 754 floating point.
     */
    DOUBLE {
        @Override public String getType() {
            return "double";
        }
    },
    /**
     * The char data type is a single 16-bit Unicode character.
     */
    CHAR {
        @Override public String getType() {
            return "char";
        }
    },
    /**
     * The String class represents character strings.
     */
    STRING {
        @Override public String getType() {
            return "String";
        }
    };

    /**
     * Parses the given String to its contained PrimitiveType if possible.
     *
     * @param type the String containing the primitive type string
     *
     * @return the PrimitiveType with the same type or null
     */
    public static PrimitiveType parseFromString(final String type) {
        for (final PrimitiveType availableType : PrimitiveType.values()) {
            if (availableType.getType().equals(type)) {
                return availableType;
            }
        }
        return null;
    }

    @Override public String toString() {
        return getType();
    }
}
