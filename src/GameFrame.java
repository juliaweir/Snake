import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame(){
      //shortcut: move this line of code in the statement on line below  'GamePanel panel = new GamePanel();'
        this.add(new GamePanel());
        this.setTitle("Snake"); //set a title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //sets the "x" button on the popup window
        this.setResizable(false); //no user resizing allowed
        this.pack();  //takes the JFrame and fits it around parameters(sized to fit the preferred size/layouts of subcomponents
        this.setVisible(true);
        this.setLocationRelativeTo(null); //sets the window to open in the middle of the screen

    }
}
