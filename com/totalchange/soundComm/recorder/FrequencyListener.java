package com.totalchange.soundComm.recorder;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @version 1.0
 *
 */

public interface FrequencyListener {

    /**
     * Whenever a frequency change is detected it will be measured for its
     * duration and this method will be called...
     * @param frequency The detected frequency
     * @param length The duration in millis of the detected frequency
     */
    public void setFrequency(double frequency, int length);
}