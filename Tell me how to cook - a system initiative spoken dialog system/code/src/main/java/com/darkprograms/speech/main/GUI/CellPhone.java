package com.darkprograms.speech.main.GUI;

/**

This program simulates a cellphone.

*/

import javax.swing.*;
import java.awt.*;

public class CellPhone {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(250, 200));
        frame.setTitle("Tell me How to Cook");

        frame.setLayout(new BorderLayout());

        // the main phone buttons
        JPanel centerPanel = new JPanel(new GridLayout(4, 3));
       // for (int i = 1; i <= 9; i++)
        {
            centerPanel.add(new JButton("Dial"));
        }
        centerPanel.add(new JButton("*"));
        centerPanel.add(new JButton("0"));
        centerPanel.add(new JButton("#"));
        frame.add(centerPanel, BorderLayout.CENTER);

        // south status panel
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(new JLabel("Number to dial: "));
        southPanel.add(new JTextField(10));
        frame.add(southPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}     