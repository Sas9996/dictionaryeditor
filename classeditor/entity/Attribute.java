

package edu.kit.informatik.classeditor.entity;

import edu.kit.informatik.classeditor.database.Nameable;
import edu.kit.informatik.classeditor.ui.Main;

import java.util.Objects;

/**
 * @author Sara
 * @version 1.0
 */
public class Attribute extends Nameable implements Comparable<Attribute>, Containable {

    /**
     * The index of the construct name in the input format specified in the assignment.
     */
    public static final int CONSTRUCT_NAME_INDEX = 1;
    /**
     * The index of the visibility modifier in the input format specified in the assignment.
     */
    public static final int VISIBILITY_MODIFIER_INDEX = 2;
    /**
     * The index of the final input in the input format specified in the assignment.
     */
    public static final int FINAL_INDEX = 3;
    /**
     * The index of the type in the input format specified in the assignment.
     */
    public static final int TYPE_INDEX = 4;
    /**
     * The index of the attribute name in the input format specified in the assignment.
     */
    public static final int NAME_INDEX = 5;

    /**
     * The pattern for a variable name.
     */
    public static final String VARIABLE_NAME_PATTERN = "([a-z][a-zA-Z0-9]*)";

    /**
     * The pattern for a signature as stated in the assignment.
     */
    public static final String ATTRIBUTE_SIGNATURE_PATTERN = Construct.getConstructNamePattern() + Construct
            .getConstructSeparator() + Main.SEPARATOR + VisibilityModifier.getVisibilityModifierPattern() + Final
                                                                     .getFinalPattern() + Type
                                                                     .getCaseInsensitivePattern() + Main.SEPARATOR
                                                             + VARIABLE_NAME_PATTERN;

    private final VisibilityModifier visibilityModifier;
    private final Final aFinal;
    private final Type type;
    private final NameableConstruct construct;

    /**
     * Instantiates a new Attribute with the given parameters. No checks are performed.
     *
     * @param name the name of this attribute
     * @param visibilityModifier the visibilityModifier of this attribute
     * @param aFinal the final state of this attribute
     * @param type the type of this attribute
     * @param construct the construct this attribute is contained in
     */
    public Attribute(final String name, final VisibilityModifier visibilityModifier, final Final aFinal,
            final Type type, final NameableConstruct construct) {
        super(name);
        this.visibilityModifier = visibilityModifier;
        this.aFinal = aFinal;
        this.type = type;
        this.construct = construct;
    }

    @Override public String getSignature() {
        // @formatter:off
        return (construct != null
                ? construct.getName()
                : "")
                  + Construct.getConstructSeparator() + Main.SEPARATOR
               + (visibilityModifier != VisibilityModifier.DEFAULT
                        ? visibilityModifier.getPattern() + Main.SEPARATOR
                        : Main.EMPTY_STRING)
               + (aFinal != Final.NOT_FINAL
                        ? aFinal.getPattern() + Main.SEPARATOR
                       : Main.EMPTY_STRING)
               + getType().toString() + Main.SEPARATOR
               + getName();
        // @formatter:on
    }

    /**
     * @return the visibilityModifier of this attribute
     */
    public VisibilityModifier getVisibilityModifier() {
        return visibilityModifier;
    }

    /**
     * @return the final state of this attribute
     */
    public Final getaFinal() {
        return aFinal;
    }

    /**
     * @return the type of this attribute
     */
    public Type getType() {
        return type;
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attribute)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Attribute attribute = (Attribute) o;
        return visibilityModifier == attribute.visibilityModifier && aFinal == attribute.aFinal && Objects
                .equals(type, attribute.type);
    }

    @Override public int hashCode() {
        return Objects.hash(super.hashCode(), visibilityModifier, aFinal, type);
    }

    @Override public int compareTo(final Attribute o) {
        return getSignature().compareTo(o.getSignature());
    }

    /**
     * @return the containing construct of this attribute
     */
    public NameableConstruct getConstruct() {
        return construct;
    }

    @Override public NameableConstruct getContainer() {
        return getConstruct();
    }
}
