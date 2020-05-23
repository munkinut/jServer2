/*
 * RandomGenerator.java - produces strings of random characters
 *
 * Copyright (C) 2001 by Warren Milburn
 *
 */

package net.munki.util.random;

import java.util.Date;
import java.util.Random;

/** Provides the basis for a random String generator
 */
public abstract class RandomGenerator {
    
    private Random random;
    private char[] select = {'A','B','C','D','E','F','G','H','J','K','L',
    'M','N','P','Q','R','S','T','U','V','W','X',
    'Y','Z',
    'a','b','c','d','e','f','g','h','i','j','k',
    'm','n','o','p','q','r','s','t','u','v','w','x',
    'y','z',
    '2','3','4','5','6','7','8','9'};
    
    /** Create a random String generator
     */    
    public RandomGenerator() {
        random = new Random(new Date().getTime());
    }
    
    /** Generates a random sequence of characters
     * @param chars Number of characters to generate
     * @return String of x characters
     */    
    protected String generateCode(int chars) {
        char[] pass = new char[chars];
        int index;
        for (int i = 0; i < chars; i++) {
            index = random.nextInt(select.length);
            pass[i] = select[index];
        }
        return new String(pass);
    }
    
    /** Implemented by subclasses to specify the pattern of randomly generated Strings.
     * @return Randomly generated String
     */    
    public abstract String generate();
    
}