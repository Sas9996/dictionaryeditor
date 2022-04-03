

package edu.kit.informatik.classeditor.entity;

import edu.kit.informatik.classeditor.ui.Main;

/**
 * @author Sara
 * @version 1.0
 */
public class Interface extends NameableConstruct {
    /**
     * The string of a underlying type for the interface.
     */
    public static final String PATTERN = "interface";

    /**
     * Instantiates a new Interface with the given name.
     *
     * @param name the name of this Interface
     */
    public Interface(final String name) {
        super(name);
    }

    @Override public boolean addImplement(final NameableConstruct toBeImplemented) {
        return false;
    }

    @Override public boolean addExtend(final NameableConstruct toBeExtended) {
        return false;
    }

    @Override public String getFullName() {
        return PATTERN + Main.SEPARATOR + getName();
    }

    @Override public String getUnderlyingType() {
        return PATTERN;
    }

    @Override public String addMethod(final Method method) {
        if (method.getFinalState() == Final.FINAL) {
            return "Error, cannot add a final method to an interface";
        }
        return super.addMethod(method);
    }
}
