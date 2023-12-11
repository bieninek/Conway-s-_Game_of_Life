import view.*;

import java.util.concurrent.TimeUnit;

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
