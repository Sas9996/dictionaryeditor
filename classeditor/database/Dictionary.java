

package edu.kit.informatik.classeditor.database;

import edu.kit.informatik.classeditor.entity.NameableConstruct;

import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;

/**
 * Encapsulates a dictionary with String keys and the type T. T has to extend NameableConstruct because the name of
 * it is uses as a key.
 *
 * @param <T> Type of the elements stored in this dictionary
 *
 * @author Sara
 * @version 1.0
 */
public class Dictionary<T extends NameableConstruct> {

    private final Map<String, T> availableEntities = new TreeMap<>();

    /**
     * Adds the given element to this dictionary.
     *
     * @param toBeAdded the element ot be added to this dictionary
     *
     * @return the added element or null if an element with that key is already present
     */
    public T add(final T toBeAdded) {
        if (availableEntities.containsKey(toBeAdded.getName())) {
            return null;
        }
        availableEntities.put(toBeAdded.getName(), toBeAdded);
        return toBeAdded;
    }

    /**
     * Checks if the given key is present in this dictionary.
     *
     * @param key the key
     *
     * @return if the given key is present
     */
    public boolean containsKey(final String key) {
        return key != null && availableEntities.containsKey(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this dictionary contains no mapping for the
     * key.
     *
     * @param key the key of an element
     *
     * @return the element or null if no element with the given key exists
     */
    public T get(final String key) {
        return availableEntities.get(key);
    }

    /**
     * Returns a string representation of this dictionary.
     *
     * @param separator the separator to be used in the string representation between two entities
     *
     * @return the string representation of the content of this dictionary
     */
    public String list(final String separator) {
        final StringJoiner tList = new StringJoiner(separator);
        for (final T entity : availableEntities.values()) {
            tList.add(entity.getFullName());
        }
        return tList.toString();
    }
}
