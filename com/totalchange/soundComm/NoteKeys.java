package com.totalchange.soundComm;

/**
 * Title:
 * Description:  Contains a datastore for the NoteKey class.  Many utility
 *               for the interpreter.
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class NoteKeys {
    private NoteKey[] noteKeyArray;

    // Used in for loops...
    private int num;

    /**
     * Sets up all the NoteKeys according to the constants defined in Constants.
     */
    public NoteKeys() {
        String thisKey;
        int thisTime;
        int nextLength = 0;
        int nextPos = 1;

        noteKeyArray = new NoteKey[Constants.CHARACTERS.length];

        // Now run through and assign values to all the keys...
        for (num = 0; num < noteKeyArray.length; num++) {
            thisKey = Constants.CHARACTERS[num];
            thisTime = Constants.MIN_DURATION + (Constants.DURATION_INCREMENTOR * nextLength);

            noteKeyArray[num] = new NoteKey(thisKey, nextPos, thisTime);

            // A line to check the values of the keys being made...
            //System.out.println("Key: " + thisKey + ", Pos: " + nextPos + ", Time: " + thisTime);

            nextPos++;
            if (nextPos > Constants.NOTE_RANGE) {
                nextPos = 1;

                // Need to have a new length of time...
                nextLength++;
            }
        }
    }

    /**
     * When searching for a matching note, the inputted time is rounded down to
     * match the nearest note configuration.  Therefore, if a note is of
     * position 6, and it's time is 1500ms, it will be compared to KeyNotes of
     * position 6 and the nearest KeyNote with a time LESS THAN 1500ms.
     *
     * Because noteKeyArray is ordered shortest time to largest time, the first
     * note to be matched is the successful one...
     */
    public NoteKey getMatchingKey(int pos, int time) {
        try {
            for (num = noteKeyArray.length - 1; num >= 0; num--) {
                if ((noteKeyArray[num].getPosition() == pos) && (noteKeyArray[num].getTime() <= time)) {
                    return noteKeyArray[num];
                }
            }
        }
        catch (NullPointerException e) {
            return null;
        }

        return null;
    }

    /**
     * Returns the NoteKey representation of the specified key.  Returns null if
     * none found.  This method ignores case.
     */
    public NoteKey getMatchingNote(String key) {
        for (num = 0; num < noteKeyArray.length; num++) {
            if (key.equalsIgnoreCase(noteKeyArray[num].getKey())) {
                return noteKeyArray[num];
            }
        }

        return null;
    }
}