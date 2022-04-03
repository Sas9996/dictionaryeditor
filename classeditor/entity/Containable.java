

package edu.kit.informatik.classeditor.entity;

/**
 * A class implementing this interface can be contained in a Nameable construct.
 *
 * @author Sara
 * @version 1.0
 */
public interface Containable {
    /**
     * Returns the container of this item.
     *
     * @return the container this item is contained in
     */
    NameableConstruct getContainer();
}
