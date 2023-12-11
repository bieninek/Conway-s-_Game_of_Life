package view;

import controller.CAUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainUI {
    private JPanel buttonPanel;
    private JPanel optionPanel;
    private final int size;
    private boolean isRunning;
    private ArrayList<ArrayList<JButton>> buttonArrayList;


    public MainUI() {
        size = 30;
        isRunning = false;

        JFrame frame = new JFrame("GameOfLife");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        initButtonPanel();
        initButtonArrayList();
        frame.add(buttonPanel, BorderLayout.WEST);
        initOptionPanel();
        initOptionButton();
        frame.add(optionPanel, BorderLayout.EAST);

        frame.pack();
        frame.setVisible(true);
    }

    private void initButtonPanel() {
        buttonPanel = new JPanel(new GridLayout(size, size, 2, 2));
        buttonPanel.setPreferredSize(new Dimension(700, 700));
    }

    private void initOptionPanel() {
        optionPanel = new JPanel(new GridLayout(1, 1, 20, 20));
        optionPanel.setPreferredSize(new Dimension(100, 700));
    }

    private void initButtonArrayList() {
        buttonArrayList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            buttonArrayList.add(new ArrayList<>());
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JButton tmp = new JButton("");
                tmp.setPreferredSize(new Dimension(3, 3));
                tmp.setBackground(Color.lightGray);
                if (i < 3 || i > size-4 || j <3 || j > size-4) {
                    tmp.setVisible(false);
                }
                tmp.addActionListener(e -> {
                    if (tmp.getBackground() == Color.lightGray) {
                        tmp.setBackground(Color.BLUE);
                    } else if (tmp.getBackground() == Color.BLUE) {
                        tmp.setBackground(Color.lightGray);
                    } else {
                        System.out.println("ERROR in MainUI\n Couldn't set color");
                        System.exit(-1);
                    }
                });
                buttonArrayList.get(i).add(tmp);
                buttonPanel.add(tmp);
            }
        }
    }

    private void initOptionButton() {
        JButton simulateButton = new JButton("Start / stop");
        simulateButton.setMaximumSize(new Dimension(50, 20));
        simulateButton.addActionListener(e -> {
            isRunning = !isRunning;
            ifClicked();
            for (int i = 0; i < size; i++) {
                for (var elem : buttonArrayList.get(i)) {
                    elem.setEnabled(!elem.isEnabled());
                }
            }
        });
        optionPanel.add(simulateButton);

    }

    public boolean ifClicked() {
        return isRunning;
    }

    public void simulate() throws InterruptedException {
        while (isRunning) {
            TimeUnit.MILLISECONDS.sleep(500);

            buttonArrayList = (new CAUtil()).run(buttonArrayList, size);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i >= 3 && i <= size - 4 && j >= 3 && j <= size - 4) {
                        buttonPanel.setComponentZOrder(buttonArrayList.get(i).get(j), i * size + j);
                    } else {
                        JButton tmp = buttonArrayList.get(i).get(j);
                        tmp.setVisible(false);
                        buttonPanel.setComponentZOrder(tmp, i * size + j);
                    }
                }
            }
        }
    }
}
