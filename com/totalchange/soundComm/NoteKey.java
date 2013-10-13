package com.totalchange.soundComm;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class NoteKey {
    private String key;
    private int pos;
    private int time;

    public NoteKey(String key, int pos, int time) {
        this.key = key;
        this.pos = pos;
        this.time = time;
    }

    /**
     * Returns the key this NoteKey represents...
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the position in the frequency range this NoteKey is...
     */
    public int getPosition() {
        return pos;
    }

    /**
     * Returns the amount of time the frequency is intended to be...
     */
    public int getTime() {
        return time;
    }
}