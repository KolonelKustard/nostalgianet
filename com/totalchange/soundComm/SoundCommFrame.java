package com.totalchange.soundComm;

import java.awt.*;
import java.awt.event.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class SoundCommFrame extends Frame implements SoundCommThreadListener {
    private SoundCommThread soundComm;

    private boolean playMin = false;
    private boolean playMax = false;

    Panel panel3 = new Panel();
    GridLayout gridLayout2 = new GridLayout();
    Panel panel4 = new Panel();
    Panel panel5 = new Panel();
    GridLayout gridLayout3 = new GridLayout();
    Button setMinButton = new Button();
    Button playMaxButton = new Button();
    Button playMinButton = new Button();
    Button setMaxButton = new Button();
    GridLayout gridLayout4 = new GridLayout();
    Label label3 = new Label();
    TextField textInput = new TextField();
    Panel panel6 = new Panel();
    TextArea outgoingText = new TextArea();
    Panel panel1 = new Panel();
    Label label1 = new Label();
    BorderLayout borderLayout1 = new BorderLayout();
    Panel panel2 = new Panel();
    TextArea incomingText = new TextArea();
    Label label2 = new Label();
    BorderLayout borderLayout2 = new BorderLayout();
    BorderLayout borderLayout3 = new BorderLayout();
    GridLayout gridLayout1 = new GridLayout();

    public SoundCommFrame() {
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public SoundCommFrame(SoundCommThread soundComm) {
        this();

        this.soundComm = soundComm;
        soundComm.addListener(this);
    }

    private void jbInit() throws Exception {
        this.setTitle("Nostalgia Network");
        this.setLayout(borderLayout3);
        panel3.setLayout(gridLayout2);
        gridLayout2.setColumns(2);
        panel4.setLayout(gridLayout3);
        gridLayout3.setRows(2);
        gridLayout3.setColumns(2);
        setMinButton.setLabel("Set Min");
        setMinButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMinButton_actionPerformed(e);
            }
        });
        playMaxButton.setLabel("Play Max");
        playMaxButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playMaxButton_actionPerformed(e);
            }
        });
        playMinButton.setLabel("Play Min");
        playMinButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playMinButton_actionPerformed(e);
            }
        });
        setMaxButton.setLabel("Set Max");
        setMaxButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMaxButton_actionPerformed(e);
            }
        });
        panel5.setLayout(gridLayout4);
        gridLayout4.setRows(2);
        label3.setAlignment(Label.CENTER);
        label3.setText("Send:");
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                this_windowClosing(e);
            }
        });
        textInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textInput_actionPerformed(e);
            }
        });
        panel1.setLayout(borderLayout1);
        label1.setText("Outgoing");
        panel2.setLayout(borderLayout2);
        label2.setText("Incoming");
        panel6.setLayout(gridLayout1);
        gridLayout1.setColumns(2);
        this.add(panel6, BorderLayout.CENTER);
        panel6.add(panel2, null);
        panel2.add(label2, BorderLayout.NORTH);
        panel2.add(incomingText, BorderLayout.CENTER);
        panel6.add(panel1, null);
        panel1.add(label1, BorderLayout.NORTH);
        panel1.add(outgoingText, BorderLayout.CENTER);
        this.add(panel3, BorderLayout.SOUTH);
        panel3.add(panel4, null);
        panel4.add(setMinButton, null);
        panel4.add(setMaxButton, null);
        panel4.add(playMinButton, null);
        panel4.add(playMaxButton, null);
        panel3.add(panel5, null);
        panel5.add(label3, null);
        panel5.add(textInput, null);
    }

    public void appendToOutgoing(String s) {
        outgoingText.append(s);
    }

    public void appendToIncoming(String s) {
        incomingText.append(s);
    }

    void this_windowClosing(WindowEvent e) {
        soundComm.shutdown();
        System.exit(0);
    }

    void playMinButton_actionPerformed(ActionEvent e) {
        playMin = !playMin;

        if (playMin) {
            playMinButton.setLabel("Stop Min");
            soundComm.playMin();
        }
        else {
            playMinButton.setLabel("Play Min");
            soundComm.stopMin();
        }
    }

    void playMaxButton_actionPerformed(ActionEvent e) {
        playMax = !playMax;

        if (playMax) {
            playMaxButton.setLabel("Stop Max");
            soundComm.playMax();
        }
        else {
            playMaxButton.setLabel("Play Max");
            soundComm.stopMax();
        }
    }

    void textInput_actionPerformed(ActionEvent e) {
        soundComm.sendOutgoingText(textInput.getText() + "\n");
        textInput.setText("");
    }

    void setMinButton_actionPerformed(ActionEvent e) {
        double min = soundComm.configureMin();

        incomingText.append("\nMinimum frequency set to: " + min + "Hz\n");
    }

    void setMaxButton_actionPerformed(ActionEvent e) {
        double max = soundComm.configureMax();

        incomingText.append("\nMaximum frequency set to: " + max + "Hz\n");
    }
}