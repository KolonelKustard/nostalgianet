package com.totalchange.soundComm.recorder;

import java.util.*;

import craigl.fft.ComputeFrequencyWithFFT;
import craigl.winrecorder.*;

/**
 * Title:
 * Description:  Notifies listeners of changes in audio frequency.
 * Copyright:    Copyright (c) 2001
 * Company:
 * @version 1.0
 * @todo Need to make it so that if frequencies aren't the same twice
 *       in a row, they will not register as a new frequency.  Should
 *       stop spikes from disrupting the flow.
 */

public class FrequencyThread extends Thread {
    public static final int SAMPLE_RATE = 22050;
    public static final int BUFFER_SIZE = 7500;
    public static final int BIT_DEPTH = 16;
    public static final int NUM_OF_CHANNELS = 1;
    public static final int DEFAULT_DEVICE_ID = 1;

    public static final double MAX_FREQUENCY = Double.MAX_VALUE;

    /**
     * The allowed amount + and - that frequency can change before it is
     * considered a new frequency.
     */
    public static final double ACCEPTED_FREQUENCY_DEVIANCE = 20.0;

    /**
     * The allowed amount + and - that frequency has to be when compared to last
     * frequency to be considered valid.
     */
    public static final double ACCEPTED_FREQUENCY_SIMILARITY = 2.5;

    /**
     * The amount of time (in millis) a frequency must hold before it is
     * considered worthy of alerting the listeners.
     */
    public static final int MINIMUM_ALERT_TIME = 1000;

    private Vector listeners = new Vector();
    private boolean stillRunning, suspended;

    private WinRecorder recorder;
    private ComputeFrequencyWithFFT compute;
    private int deviceID;

    private short[] getSoundBufferShorts = new short[BUFFER_SIZE];
    private double[] getSoundBufferDoubles = new double[BUFFER_SIZE];

    private double lastFrequency = 0.0;
    private double lastGoodFrequency = 0.0;

    /**
     * Creates the Frequency Thread and produces a dialog if necessary to
     * discover any details about capturing audio that may need to be found out.
     */
    public FrequencyThread(int deviceID) {
        stillRunning = true;
        suspended = false;

        deviceID = deviceID;
        recorder = new WinRecorder(SAMPLE_RATE, NUM_OF_CHANNELS, deviceID, null);
        compute = new ComputeFrequencyWithFFT(SAMPLE_RATE);

        if (recorder.initRecorder()) {
            System.out.println("Successfully initiated audio capture");
        }
        else {
            System.out.println("Failed to initiate audio capture");
        }

        this.start();
    }

    /**
     * Returns an array of doubles that represent the waveform to get the
     * frequency data from.
     */
    private double[] getSoundBuffer() {
        recorder.getSamples(getSoundBufferShorts, getSoundBufferShorts.length);

        for(int num = 0; num < getSoundBufferShorts.length; num++) {
            getSoundBufferDoubles[num] = (double)getSoundBufferShorts[num];
        }

        return getSoundBufferDoubles;
    }

    /**
     * Returns the
     */
    private double getNewFrequency() {
        double thisFrequency = compute.computeFrequency(MAX_FREQUENCY, getSoundBuffer());

        // Check if frequency is gooood...
        if (((thisFrequency - lastFrequency) > (0 - ACCEPTED_FREQUENCY_SIMILARITY)) &&
            ((thisFrequency - lastFrequency) < (0 + ACCEPTED_FREQUENCY_SIMILARITY))) {

            lastGoodFrequency = thisFrequency;
        }

        lastFrequency = thisFrequency;

        return lastGoodFrequency;
    }

    /**
     * The bulk that shouldn't need to be changed
     */
    public void run() {
        double frequency = 0.0;
        int frequencyTime = 0;

        double thisFrequency;
        double firstFrequency = 0.0;
        long firstFrequencyStartTime = System.currentTimeMillis();

        while(stillRunning) {
            while(!suspended) {
                thisFrequency = getNewFrequency();

                // Test to see if this frequency is in the limits...
                if (((thisFrequency - firstFrequency) > (0 - ACCEPTED_FREQUENCY_DEVIANCE)) &&
                    ((thisFrequency - firstFrequency) < (0 + ACCEPTED_FREQUENCY_DEVIANCE))) {

                    // If it is, then we carry on merrily...
                }
                else {
                    // If it isn't then we need to notify listeners of the old
                    // frequency and its duration, and reset firstFrequency...
                    if (!suspended) {
                        frequency = firstFrequency;
                        frequencyTime = (int)(System.currentTimeMillis() - firstFrequencyStartTime);

                        if (frequencyTime > MINIMUM_ALERT_TIME) {
                            for(int num = 0; num < listeners.size(); num++) {
                                ((FrequencyListener)listeners.elementAt(num)).setFrequency(frequency, frequencyTime);
                            }
                        }

                        firstFrequency = thisFrequency;
                        firstFrequencyStartTime = System.currentTimeMillis();
                    }
                }


            }

            if (suspended) {
                synchronized(this) {
                    try {
                        this.wait();
                    }
                    catch(Exception e) {}
                }
            }
        }
    }

    /**
     * Adds a listener to this frequency thread.
     */
    public void addListener(FrequencyListener listener) {
        listeners.addElement(listener);
    }

    /**
     * Removes listener from this frequency thread.
     */
    public void removeListener(FrequencyListener listener) {
        listeners.removeElement(listener);
    }

    /**
     * Stops the thread
     */
    public void stopListening() {
        suspended = true;
    }

    /**
     * Starts the thread
     */
    public void startListening() {
        suspended = false;

        synchronized(this) {
            try {
                this.notify();
            }
            catch(Exception e) {}
        }
    }

    /**
     * To test this thread...
     */
    public static void main(String[] args) throws Exception {
        FrequencyThread trial = new FrequencyThread(1);

        trial.addListener(new FrequencyListener() {
            public void setFrequency(double frequency, int time) {
                System.out.println("Frequency: " + frequency + ", Time: " + time);
            }
        });
    }
}