package com.totalchange.soundComm.player;

import quicktime.*;
import quicktime.std.music.*;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;

import com.totalchange.soundComm.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class MIDIPlayer {
    public static final int MIN_NOTE = 60;
    public static final int MAX_NOTE = 68;

    private ToneDescription toneDesc;
    private NoteKeys noteKeys;

    // The actual min and max have to be calculated...
    private int minNote = MIN_NOTE;
    private int maxNote;
    private int noteIncrementor;

    private NoteChannel minNoteChan;
    private NoteChannel maxNoteChan;
    private NoteChannel stdNoteChan;

    public MIDIPlayer() throws Exception {
        noteKeys = new NoteKeys();

        noteIncrementor = (MAX_NOTE - MIN_NOTE) / Constants.NOTE_RANGE;
        maxNote = minNote + ((Constants.NOTE_RANGE - 1) * noteIncrementor);

        System.out.println("Min Note: " + minNote + ", Max Note: " + maxNote + ", Incrementor: " + noteIncrementor);

        QTSession.open();
        toneDesc = new ToneDescription();
        chooseInstrument();
    }

    public void shutdown() {
        QTSession.close();
    }

    public void chooseInstrument() throws Exception {
        toneDesc.pickInstrument(NoteAllocator.getDefault(), "Choose an Instrument...", 0);

        minNoteChan = new NoteChannel(new NoteRequest(toneDesc));
        maxNoteChan = new NoteChannel(new NoteRequest(toneDesc));
        stdNoteChan = new NoteChannel(new NoteRequest(toneDesc));
    }

    /**
     * Plays the sound expressed in String stringSound.  Blocks current thread
     * during playback.
     */
    public void playSound(String stringSound) {
        NoteKey noteKey;
        int noteToPlay;
        int noteLength;

        noteKey = noteKeys.getMatchingNote(stringSound);

        if (noteKey == null) {
            return;
        }

        try {
            noteToPlay = minNote + ((noteKey.getPosition() - 1) * noteIncrementor);
            noteLength = noteKey.getTime();

            System.out.println("Playing: " + stringSound + ", Note num: " + noteToPlay + ", length: " + noteLength + ", Note Pos: " + noteKey.getPosition());
            stdNoteChan.playNote(noteToPlay, 127);
            Thread.currentThread().sleep(noteLength + 50);
            stdNoteChan.playNote(noteToPlay, 0);
        }
        catch(Exception e) {e.printStackTrace();}
    }

    /**
     * Starts the playback of the minimum set up sound.  Audio won't stop until
     * the stopMinSound() method is called.
     */
    public void playMinSound() {
        try {
            minNoteChan.playNote(minNote, 127);
        }
        catch(Exception e) {e.printStackTrace();}
    }

    /**
     * Stops audio started by method playMinSound()
     */
    public void stopMinSound() {
        try {
            minNoteChan.playNote(minNote, 0);
        }
        catch(Exception e) {e.printStackTrace();}
    }

    /**
     * Starts the playback of the maximum set up sound.  Audio won't stop until
     * the stopMaxSound() method is called.
     */
    public void playMaxSound() {
        try {
            minNoteChan.playNote(maxNote, 127);
        }
        catch(Exception e) {e.printStackTrace();}
    }

    /**
     * Stops audio started by the playMaxSound() method.
     */
    public void stopMaxSound() {
        try {
            minNoteChan.playNote(maxNote, 0);
        }
        catch(Exception e) {e.printStackTrace();}
    }

    public static void main(String[] args) throws Exception {
        MIDIPlayer midi = new MIDIPlayer();
        String[] playMe = {"H", "e", "l", "l", "o", " ", "F", "r", "o", "m", " ", "R", "a", "l", "p", "h"};


        String mePlay = "Hi, my name is Alice.Alice is my name too!What a coincidence!The CCCC is hard at work (Cosmic Coincidence Control Center).What is your real name?ALICE is my real name.Why do you think I am your real name ?Deductive reasoning from the facts.A deeper algorithm is needed to respond to that correctly.What is it?It refers to Deeper Algorithm Is Needed To Respond To That Correctly, I think.Therefore you are.Interesting deduction. No I am not.I heard you are.Who said that?I can't remember who said it.Why can't you do it?";
        //String[] playMe = {"H", "e", "l", "l", " "};

        //midi.playMinSound();
        //Thread.currentThread().sleep(3000);
        //midi.stopMinSound();

        //midi.playMaxSound();
        //Thread.currentThread().sleep(3000);
        //midi.stopMaxSound();

        for (int num = 0; num < mePlay.length(); num++) {
            midi.playSound(String.valueOf(mePlay.charAt(num)));
            Thread.currentThread().sleep(300);
        }

        midi.shutdown();

        System.exit(0);
    }
}