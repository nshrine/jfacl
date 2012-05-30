/*
 * IteratorEnumeration.java
 *
 * Created on 20 January 2006, 16:01
 *
 */

package org.flimbar.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 *
 * @author nrs
 */
public class IteratorEnumeration<T> implements Enumeration {
    
    private final Iterator<T> iterator;
    
    /** Creates a new instance of IteratorEnumeration */
    public IteratorEnumeration(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    /**
     * Returns the next element of this enumeration if this enumeration
     * object has at least one more element to provide.
     * 
     * @return     the next element of this enumeration.
     * @exception  NoSuchElementException  if no more elements exist.
     */
    public T nextElement() { 
        return iterator.next();
    }

    /**
     * Tests if this enumeration contains more elements.
     * 
     * @return  <code>true</code> if and only if this enumeration object
     *           contains at least one more element to provide;
     *          <code>false</code> otherwise.
     */
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }    
}
