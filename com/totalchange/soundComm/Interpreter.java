package com.totalchange.soundComm;

import java.util.*;

import com.totalchange.soundComm.recorder.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class Interpreter {
    private FrequencyThread freqThread;
    private NoteKeys noteKeys;

    private double minFrequency = 0.0;
    private double maxFrequency = 0.0;

    private double[] frequencyTable;
    private double matchingDeviance;

    private NoteKey matchedKey;

    private Vector listeners = new Vector();

    private boolean setMinFreq = false, setMaxFreq = false;

    public Interpreter(int deviceID) {
        freqThread = new FrequencyThread(deviceID);
        freqThread.stopListening();

        // Add a listening callback onto the frequency thread...
        freqThread.addListener(new FrequencyListener() {
            public void setFrequency(double frequency, int time) {

                // Call the private method to do with interpreting from
                // frequencies
                newFrequency(frequency, time);
            }
        });

        // Make our key to note templates...
        noteKeys = new NoteKeys();

        // Setup the array of comparison frequencies (used in newFrequency
        // method).
        setupFrequencyTable();
    }

    /**
     * The part of the callback that handles a new frequency.  Uses this to
     * interpret to whatever.
     */
    private void newFrequency(double frequency, int time) {
        System.out.println("Freq: " + frequency + ", Time: " + time);

        // Check to see if this call is an update one...
        if (setMaxFreq) {
            maxFrequency = frequency;

            try {
                synchronized(this) {
                    this.notify();
                }
            }
            catch(Exception e) {e.printStackTrace();}

            setMaxFreq = false;
            return;
        }

        if (setMinFreq) {
            minFrequency = frequency;

            try {
                synchronized(this) {
                    this.notify();
                }
            }
            catch(Exception e) {e.printStackTrace();}

            setMinFreq = false;
            return;
        }

        // Compare frequency's using matchingDeviance and frequencyTable.
        for (int num = 0; num < frequencyTable.length; num++) {
            if ((frequency > (frequencyTable[num] - matchingDeviance)) && (frequency < (frequencyTable[num] + matchingDeviance))) {

                // Uncomment following line for debugging...
                //System.out.println("Matched to pos: " + (num + 1) + ", freq: " + frequencyTable[num]);

                // Now need to find out if this matches a key that's set up...
                matchedKey = noteKeys.getMatchingKey(num + 1, time);

                // Returns null if no match, so if not null, we know it's a
                // match...
                if (matchedKey != null) {

                    // Seeing as it's matched, we'll let all the listeners know.
                    for (int num2 = 0; num2 < listeners.size(); num2++) {
                        ((InterpreterListener)listeners.elementAt(num2)).setText(matchedKey.getKey());
                    }

                    System.out.println(matchedKey.getKey());
                }
            }
        }
    }

    /**
     * Creates an array of frequency values that relate to note positions.
     * This is calculated by splitting the range of minFrequency to maxFrequency
     * into Constants.NOTE_RANGE parts.
     */
    private void setupFrequencyTable() {
        double difference, incrementor, value;

        frequencyTable = new double[Constants.NOTE_RANGE];

        difference = maxFrequency - minFrequency;
        incrementor = difference / (Constants.NOTE_RANGE - 1);

        value = 0.0;

        for (int num = 0; num < frequencyTable.length; num++) {
            frequencyTable[num] = value + minFrequency;
            value += incrementor;

            // Uncomment next line for debugging...
            //System.out.println("Pos: " + num + ", Freq: " + frequencyTable[num]);
        }

        matchingDeviance = (incrementor / 2) * ((double)Constants.MATCH_FREQ_DEVIANCE / 100);

        // Uncomment next line for debugging...
        //System.out.println("Incrementor: " + incrementor + ", Matching deviance: " + matchingDeviance);
    }

    public void setMinFrequency(double frequency) {
        minFrequency = frequency;
        setupFrequencyTable();
    }

    public double setMinFrequencyFromInput() {
        setMinFreq = true;
        startInterpreting();

        try {
            synchronized(this) {
                this.wait();
            }
        }
        catch(Exception e) {e.printStackTrace();}

        setupFrequencyTable();
        return minFrequency;
    }

    public void setMaxFrequency(double frequency) {
        maxFrequency = frequency;
        setupFrequencyTable();
    }

    public double setMaxFrequencyFromInput() {
        setMaxFreq = true;
        startInterpreting();

        try {
            synchronized(this) {
                this.wait();
            }
        }
        catch(Exception e) {e.printStackTrace();}

        setupFrequencyTable();
        return maxFrequency;
    }

    /**
     * Starts the interpreter...
     */
    public void startInterpreting() {
        freqThread.startListening();
    }

    /**
     * Stops the interpreter...
     */
    public void stopInterpreting() {
        freqThread.stopListening();
    }

    public void addListener(InterpreterListener listener) {
        listeners.addElement(listener);
    }

    public void removeListener(InterpreterListener listener) {
        listeners.removeElement(listener);
    }

    public static void main(String[] args) {
        int la = 8;
        int de = 26;

        Interpreter interpreter = new Interpreter(1);

        interpreter.setMinFrequency(263.0);
        interpreter.setMaxFrequency(392.0);

        interpreter.startInterpreting();
    }
}