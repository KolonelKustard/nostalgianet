package com.totalchange.soundComm;

import java.util.*;

import b.*;

import com.totalchange.soundComm.player.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class SoundCommThread extends Thread implements InterpreterListener {
    public static final int PAUSE_BETWEEN_NOTES = 1000;

    private static final int STOPPED = 1;
    private static final int INPUT = 1;
    private static final int OUTPUT = 2;

    private int state = INPUT;
    private boolean paused = false;

    private Interpreter interpreter;
    private MIDIPlayer midiPlayer;

    private Loader loader;
    private Responder robot = new GUIResponder();
    private String hname = "localhost";
    private String response;

    private String textIn = "Hello", textOut = "";

    private boolean stillGoing = true;

    // Used for cancelling output.  Set to true at any time to cancel output...
    private boolean cancel;

    private Vector listeners = new Vector();

    public SoundCommThread(Interpreter interpreter, MIDIPlayer midiPlayer) {
        this.interpreter = interpreter;
        this.midiPlayer = midiPlayer;

        // Load the B stuff...
        Globals.fromFile();
        Classifier.fromFile();
        loader = new Loader();
        loader.setPriority(Thread.NORM_PRIORITY);
        loader.start();

        interpreter.addListener(this);
    }

    public void run() {
        while(stillGoing) {
            // Find out what we should be doing...
            // If waiting for input, then we wait...
            if (state == INPUT) {
                interpreter.startInterpreting();

                try {
                    synchronized(this) {
                        this.wait();
                    }
                }
                catch(Exception e) {}
            }

            // If making some luvvly output, then we make it so...
            if (state == OUTPUT) {
                cancel = false;

                //interpreter.stopInterpreting();

                for (int num = 0; num < textOut.length(); num++) {
                    if (!cancel) {
                        // Send new text to listeners...
                        for (int num2 = 0; num2 < listeners.size(); num2++) {
                            ((SoundCommThreadListener)listeners.elementAt(num2)).appendToOutgoing(String.valueOf(textOut.charAt(num)));
                        }

                        midiPlayer.playSound(String.valueOf(textOut.charAt(num)));

                        try {
                            this.sleep(PAUSE_BETWEEN_NOTES);
                        }
                        catch(InterruptedException e) {
                        }
                    }
                    else break;
                }

                //interpreter.startInterpreting();

                // Swap to listening as soon as sent text...
                state = INPUT;

                // Set texts to nothingness...
                //textIn = "";
                textOut = "";
            }
        }
    }

    /**
     * This is called by the interpretor every time it gets something new.
     * This method therefore decides what the thread should do and stuff...
     */
    public void setText(String s) {
        // Check to see if paused or not...
        if (!paused) {
            // Send new text to listeners...
            for (int num = 0; num < listeners.size(); num++) {
                ((SoundCommThreadListener)listeners.elementAt(num)).appendToIncoming(s);
            }

            // If it's an end of a statement, make up some stuff to send,
            // otherwise append it to the incomings...
            if (s.equals("\n")) {
                System.out.println("Received text: " + textIn);

                // Get response from Bot...
                response = Classifier.multiline_response(textIn, hname, robot);
                System.out.println("Sending a response to: \"" + textIn + "\" of \"" + response + "\"");

                sendOutgoingText(response);
                textIn = "";
            }
            else {
                textIn += s;
            }
        }
    }

    public void addListener(SoundCommThreadListener listener) {
        listeners.addElement(listener);
    }

    public void removeListener(SoundCommThreadListener listener) {
        listeners.removeElement(listener);
    }

    public void shutdown() {
        stillGoing = false;
        cancel = true;
        midiPlayer.shutdown();
    }

    public void playMax() {
        cancel = true;
        paused = true;

        midiPlayer.playMaxSound();
    }

    public void stopMax() {
        midiPlayer.stopMaxSound();

        paused = false;
    }

    public void playMin() {
        cancel = true;
        paused = true;

        midiPlayer.playMinSound();
    }

    public void stopMin() {
        midiPlayer.stopMinSound();

        paused = false;
    }

    public double configureMin() {
        cancel = true;
        paused = true;

        double value = interpreter.setMinFrequencyFromInput();

        paused = false;
        return value;
    }

    public double configureMax() {
        cancel = true;
        paused = true;

        double value = interpreter.setMaxFrequencyFromInput();

        paused = false;
        return value;
    }

    public void sendOutgoingText(String text) {
        textOut = text;
        state = OUTPUT;

        try {
            synchronized(this) {
                this.notify();
            }
        }
        catch(Exception e) {}
    }

    public static void main(String[] args) throws Exception {
        new SoundCommThread(new Interpreter(1), new MIDIPlayer());
    }
}