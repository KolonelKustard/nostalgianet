package com.totalchange.soundComm;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class Constants {
    /**
     * The range of notes to allow.  e.g. 8 notes to represent an octave.
     */
    public static final int NOTE_RANGE = 8;

    /**
     * Minimum time for a note to be in millis.
     */
    public static final int MIN_DURATION = 1500;

    /**
     * Incrementations above minimum duration when making additional notes.
     */
    public static final int DURATION_INCREMENTOR = 1000;

    /**
     * All the recognised characters.
     */
    public static final String[] CHARACTERS = {"a", "b", "c", "d", "e", "f",
        "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y", "z", " ", "\n"};

    /**
     * This is the percentage deviance allowed in matching a frequency to a
     * note.
     */
    public static final int MATCH_FREQ_DEVIANCE = 100;
}