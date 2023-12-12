# Conway-s-_Game_of_Life
This project allows you to play with 2D cellular automata on a interactive board  
Demo is available on YouTube: https://youtu.be/w2nm6o_xk9A

## Project description
I implemented an algorithm and a program simulating the operation of the 2D cellular automata. The operation was exactly the same as in the game of life - the cell had two
states: <b>alive or dead</b>.  
My application consists of an array of JButtons that can be clicked and thus set the state
starting slots. For example, you can set the states for which the automaton behaves
oscillator, the state does not change, the automaton flies through space or some other. To the right
there is a JButton to start or stop the simulation.  
![](/image/1.jpg )  
The user can follow these steps:
1. Sets the initial state by clicking on the JButton array, and can also unclick its selection if wants to remove the selection,
2. Press the start button,
3. The JButton array becomes unclickable,
4. The simulation starts - a new layout automatically appears every 500 milliseconds,
according to the rules of the game of life,
5. You can stop the simulation by clicking the start/stop button, then you can go back to editing
selection on the JButtons table and return to step 2.  
A few more iterations of the simulation performed in my program:  
![](/image/2.jpg )
![](/image/3.jpg )
![](/image/4.jpg )
![](/image/5.jpg )
![](/image/6.jpg )
## Technical description
The biggest problem turned out to be refreshing the image after each cycle. Originally I wanted once
call the simulate() function after clicking the start button. So I wanted to have a simulate() method in
ActionListener for JButton start. Inside the simulate() function I had a loop that changed color
JBttons on the board. However, this approach turned out to not work. Despite the fact that each
JButton color change is automatically queued, no need to call repaint() or
revalidate() if the contents of the container were changed. Well, there is an EDT thread (event dispatchable
thread), which is the only thread for AWT or Swing for updating the GUI. They are in it too
ActionListener and EventListener commands invoked and supported. You can easily check
does it work on an EDT thread by writing:
```java
SwingUtilities.isEventDispatchThread()
```
It is also queued manually using the InvokeLater() method or automatically during a change
color.  
Therefore, when the simulate() function was called in the ActionListener, they were on the EDT
queued image updates, but you would only see the difference after exiting simulate() or
after another event, such as pressing another button.  
Therefore, I used a less elegant, but working solution. I have an infinite loop
in main(), where I listen for whether someone pressed the start button. If so, then I do simulate() if
no, I'm waiting for another listen.  
```java
public class App {
 public static void main (String[] argv) throws InterruptedException {
   MainUI ui = new MainUI();
   while (true) {
     while (!ui.ifClicked()) {
       TimeUnit.MILLISECONDS.sleep(100);
     }
     ui.simulate();
   }
 }
}
```
In turn, the rest of the program, i.e. MainUI, works in the background, handling events on
EDT. The MainUI class itself is quite simple, I was inspired by the idea of an array of JButtons. In MainUI
I just create a GUI and react when a button is pressed. GUI refreshing and reacting also takes place
key presses.  
CAUtil stores the methods needed to implement the game into reality.  
There is, for example, an enum with cell states:  
```java
enum states {
 DEAD,
 ALIVE
}
```
There can only be two - alive or dead.  
There is a method responsible for the initial preparation of the table - it retrieves information from the GUI
JButtons have been selected.  
There is a deadOrAlive() method, which contains information about the rules of the game of life. When the cell phone
survives when it doesn't. And this is copied to the array that stores the cell states.  
```java
private states deadOrAlive(final int i, final int j) {
 int aliveNeighbours = 0;
 for (int a = i - 1; a <= i + 1; a++) {
 for (int b = j - 1; b <= j + 1; b++) {
 aliveNeighbours +=
cellsState.get(a).get(b).equals(states.ALIVE) ? 1 : 0;
 }
 }
 aliveNeighbours -= cellsState.get(i).get(j).equals(states.ALIVE) ? 1 :0;
 if (cellsState.get(i).get(j).equals(states.DEAD) && aliveNeighbours ==3) {
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
```
Above, the cell's live neighbors are also counted.  
There are also two methods for calculating new JButton values and colors
