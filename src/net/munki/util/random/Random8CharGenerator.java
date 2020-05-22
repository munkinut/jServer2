/*
 * Random8CharGenerator.java - extends RandomGenerator to produce fixed length strings
 *
 * Copyright (C) 2001 by Warren Milburn
 *
 */

package net.munki.util.random;

/** Generates random 8 character Strings
 */
public class Random8CharGenerator extends RandomGenerator {

    /** Creates a new random String generator
     */    
    public Random8CharGenerator() {
        super();
    }
    
    /** Generate the random String
     * @return The random String
     */    
    public String generate() {
        return super.generateCode(8);
    }
}
