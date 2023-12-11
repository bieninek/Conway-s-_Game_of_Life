package controller;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CAUtil {
    private ArrayList<ArrayList<states>> cellsState;
    private ArrayList<ArrayList<states>> newCellsState;

    enum states {
        DEAD,
        ALIVE
    }

    public ArrayList<ArrayList<JButton>> run(final ArrayList<ArrayList<JButton>> buttonArrayList, final int size) {
        copyInitialStates(buttonArrayList, size);
        for (int i = 1; i < size - 1; i++) {
            for (int j = 1; j < size - 1; j++) {
                newCellsState.get(i).set(j, deadOrAlive(i, j));
            }
        }
        return repaint(buttonArrayList, size);
    }

    private void copyInitialStates(final ArrayList<ArrayList<JButton>> buttonArrayList, final int size) {
        cellsState = new ArrayList<>(size);
        newCellsState = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            cellsState.add(new ArrayList<>());
            newCellsState.add(new ArrayList<>(size));
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (buttonArrayList.get(i).get(j).getBackground() == Color.lightGray) { // dead
                    cellsState.get(i).add(states.DEAD);
                    newCellsState.get(i).add(states.DEAD);
                } else if (buttonArrayList.get(i).get(j).getBackground() == Color.BLUE) { // alive
                    cellsState.get(i).add(states.ALIVE);
                    newCellsState.get(i).add(states.ALIVE);
                } else {
                    System.out.println("ERROR in CAUtil\n Couldn't set CA initial state");
                    System.exit(-1);
                }
            }
        }
    }

    private states deadOrAlive(final int i, final int j) {
        int aliveNeighbours = 0;
        for (int a = i - 1; a <= i + 1; a++) {
            for (int b = j - 1; b <= j + 1; b++) {
                aliveNeighbours += cellsState.get(a).get(b).equals(states.ALIVE) ? 1 : 0;
            }
        }
        aliveNeighbours -= cellsState.get(i).get(j).equals(states.ALIVE) ? 1 : 0;

        if (cellsState.get(i).get(j).equals(states.DEAD) && aliveNeighbours == 3) {
            return states.ALIVE;
        } else if (cellsState.get(i).get(j).equals(states.ALIVE) && (aliveNeighbours == 2 || aliveNeighbours == 3)) {
            return states.ALIVE;
        } else if (cellsState.get(i).get(j).equals(states.ALIVE) && aliveNeighbours > 3) {
            return states.DEAD;
        } else if (cellsState.get(i).get(j).equals(states.ALIVE) && aliveNeighbours < 2) {
            return states.DEAD;
        } else if (cellsState.get(i).get(j).equals(states.DEAD) && aliveNeighbours != 3) {
            return states.DEAD;
        } else {
            System.out.println("ERROR in CAUtil\n Couldn't set CA state" );
            System.exit(-1);
            return states.DEAD;
        }
    }

    private ArrayList<ArrayList<JButton>> repaint(ArrayList<ArrayList<JButton>> buttonArrayList, final int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (newCellsState.get(i).get(j).equals(states.ALIVE)) {
                    buttonArrayList.get(i).get(j).setBackground(Color.BLUE);
                } else if (newCellsState.get(i).get(j).equals(states.DEAD)) {
                    buttonArrayList.get(i).get(j).setBackground(Color.lightGray);
                } else {
                    System.out.println("ERROR in CAUtil\n Couldn't color CA");
                    System.exit(-1);
                }
            }
        }
        return buttonArrayList;
    }
}
