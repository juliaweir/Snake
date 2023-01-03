import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    //declare everything, avoid using any magic numbers in the code
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    //how big do we want the objects in our game?
    static final int UNIT_SIZE = 25; //pixel size
    //how many can fit on a screen
    static final int GAME_NUTS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75; //fairly quick, the higher number the delay, the slower the game is
    //maybe add in easy,medium, hard default speeds here?
    //create two arrays x and y, this will contain all the coordinates of the snake
    final int x[] = new int[GAME_NUTS];
    final int y[] = new int[GAME_NUTS];
    int bodyParts = 6; //body part length
    int applesEaten;
    int appleX; //x position
    int appleY;
    char direction = 'R'; //direction in which movement begins  l left, u for up, d for down
    boolean running = false;
    Timer timer;
    Random random;
    //all things above are needed before you build your constructor


    GamePanel() {
        //GamePanel constructor
        random = new Random();
        //set preferred size
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); //cast in your set values
        this.setBackground(Color.pink); //set the games background color
        this.setFocusable(true);
        //add your key listener
        this.addKeyListener(new MyKeyAdapter());
        //finishing constructor
        startGame();
    }




    //create a start game method
    public void startGame() {
        newApple(); //put an apple on screen
        running = true;
        //instance of timer
        timer = new Timer(DELAY, this);
        timer.start();//use start function
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    //draw method
    public void draw(Graphics g) {
        if (running) { //if the program is running, then all of this

            //to make easier to read when it is being coded, add a grid layout to help visualize pixel size
            //make a for loop
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); //fill your grid, lays everything in the x axis
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); //fills y axis
            }
            //draw the apple
            g.setColor(Color.green);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //so if grid size is changed, the apple will also change in size, through 'unit size'
            //need to draw head of the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) { //head of snake
                    g.setColor(Color.MAGENTA);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else { //dealing with body
                    g.setColor(new Color(180, 0, 144));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont()); //these are metrics
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize()); //display game score
        } else {
            gameOver(g);
        }
    }

    //populate game with an apple
    public void newApple() {
//generate new apple coordinates
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; //apple is somewhere on x axis, cast to an int
        //do the same for y
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE; //we want the apple to be placed evenly on grid
    }

    public void move() { //for the snake to move
        for (int i = bodyParts; i > 0; i--) { //iterate through body parts
            x[i] = x[i - 1]; //shifting all coordinates in array over by one
            y[i] = y[i - 1];
        }

        //create a switch direction, create case for each possible direction
        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            }
    }

    //snake eats apples, that is how points are scored, add a check apple method
    public void checkApple() {  //deals with consuming the apple
        if ((x[0] == appleX) && ((y[0]) == appleY)) { //x position of apples and y position of apple
            bodyParts++; //adds to body
            applesEaten++; //score
            newApple(); //generates new apple
        }


    }

    public void checkCollisions() {
        //check if head collides with body at all
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i] && y[0] == y[i])) {
                running = false;
            }
        }
        //check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        //check touches right
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //check if head touches top
        if (y[0] < 0) {
            running = false;
        }
        //check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        //if running is false then timer stops
        if (!running) {
            timer.stop();
        }
    }


    //game over method
    public void gameOver(Graphics g) {
        //game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont()); //these are metrucs
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics1.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);
        //string value, x puts center, y is screen height / 2
        //final score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont()); //these are metrics
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize()); //display game scor

    }

    //add an inter class
    //movement keys of snake
    public class MyKeyAdapter extends KeyAdapter {
        @Override //one method for key pressed
        public void keyPressed(KeyEvent event) {
            switch (event.getKeyCode()) {
                //one for each arrow
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) { //if game is running
            move();
            checkApple();
            checkCollisions();
        }
        repaint(); //if not running
    }
}
