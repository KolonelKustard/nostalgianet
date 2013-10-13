package com.totalchange.soundComm;

import com.totalchange.soundComm.player.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class SoundCommApp {
    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            int deviceID = Integer.parseInt(args[0]);

            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            int xPos;
            int yPos;

            Interpreter interpreter = new Interpreter(deviceID);
            MIDIPlayer midiPlayer = new MIDIPlayer();

            SoundCommThread soundComm = new SoundCommThread(interpreter, midiPlayer);
            SoundCommFrame frame = new SoundCommFrame(soundComm);

            frame.pack();

            xPos = (screenSize.width / 2) - (frame.getSize().width / 2);
            yPos = (screenSize.height / 2) - (frame.getSize().height / 2);
            frame.setLocation(xPos, yPos);

            frame.show();

            soundComm.start();
        }
        else {
            System.out.println("Usage: -d (Sound device ID)");
        }
    }
}