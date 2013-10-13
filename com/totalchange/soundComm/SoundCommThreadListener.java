package com.totalchange.soundComm;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public interface SoundCommThreadListener {
    public void appendToOutgoing(String s);
    public void appendToIncoming(String s);
}