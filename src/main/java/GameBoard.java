import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.awt.*;
import java.util.List;

public class GameBoard extends JPanel implements KeyListener {

    ArrayList<Square> squares;
    int score = 0;
    GameBoard() {
        int boardWidth = 350;
        int boardHeight = 350;
        // Generate squares
        squares = getSquares();

        // Pick two starting squares
        addValueSquare();
        addValueSquare();

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);
        repaint();
    }

    private void addValueSquare() {
        List<Square> filledSquares = squares.stream().filter(square -> square.value != 0).toList();
        if (filledSquares.size() == 16) {
            System.out.println("GAME OVER");
            return;
        }
        Random rand = new Random();
        int randomSquare = rand.nextInt(squares.size());
        Square square = squares.get(randomSquare);
        if (square.value == 0) {
            int randomValue = rand.nextInt(10);
            if (randomValue > 7) {
                square.value = 4;
            } else {
                square.value = 2;
            }
        } else {
            addValueSquare();
        }
    }

    private ArrayList<Square> getSquares() {
        ArrayList<Square> squares = new ArrayList<>();
        int x = 80;
        int y = 80;
        for(int i = 0; i < 16; i++) {
            Square newSquare = new Square(x, y);
            x+=50;
            if (x == 280) {
                x = 80;
                y += 50;
            }
            squares.add(newSquare);
        }
        return squares;
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }
    public void draw(Graphics graphics) {
        //background
        graphics.setColor(Color.darkGray);
        graphics.fillRect(75, 75, 200, 200);

        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        graphics.drawString("Score: " + score, 75, 60);

        graphics.setColor(Color.BLACK);
        graphics.drawRect(74,74, 201, 201);

        for (int i = 0; i<squares.size(); i++) {
            Square currentSquare = squares.get(i);
            switch (currentSquare.value) {
                case 0:
                    graphics.setColor(Color.LIGHT_GRAY);
                    break;
                case 2:
                    graphics.setColor(Color.WHITE);
                    break;
                case 4:
                    graphics.setColor(Color.GREEN);
                    break;
                case 8:
                    graphics.setColor(Color.PINK);
                    break;
                case 16:
                    graphics.setColor(Color.ORANGE);
                    break;
                case 32:
                    graphics.setColor(Color.RED);
                    break;
                case 64:
                    graphics.setColor(Color.CYAN);
                    break;
                case 128:
                    graphics.setColor(Color.YELLOW);
                    break;
                case 256:
                    graphics.setColor(Color.MAGENTA);
                    break;
                case 512:
                    graphics.setColor(Color.blue);
                    break;
                default:
                    graphics.setColor(Color.LIGHT_GRAY);
                    currentSquare.value = 0;
            }
            graphics.fillRect(currentSquare.x, currentSquare.y, currentSquare.width, currentSquare.height);

            if (currentSquare.value != 0) {

                // Print it's value
                graphics.setColor(Color.BLACK);
                graphics.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());

                int textWidth = metrics.stringWidth(String.valueOf(currentSquare.value));

                int xPosition = currentSquare.x + (40 - textWidth) / 2;
                int yPosition = currentSquare.y + (40 + metrics.getAscent()) / 2 - metrics.getDescent();

                graphics.drawString(String.valueOf(currentSquare.value), xPosition, yPosition);
            }

        }

        // Generate squares
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            moveSquaresDown();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            moveSquaresUp();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveSquaresRight();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveSquaresLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            addValueSquare();
        }
        addValueSquare();
        repaint();
    }

    private void moveSquaresLeft() {
        squares.sort(Comparator.comparingInt(square -> square.x));
        for(int i = 0; i < squares.size(); i++) {
           Square square = squares.get(i);
           moveSquareLeft(square);
        }
    }

    private void moveSquareLeft(Square square) {
        if (square.value == 0 || square.x == 80) {
            return;
        }
        Optional<Square> SquareOnLeft = squares.stream().filter(square1 -> (square1.x == square.x - 50) && (square1.y == square.y)).findFirst();
        Square secondSquare = SquareOnLeft.get();
        if (square.value == secondSquare.value) {
            secondSquare.value = secondSquare.value * 2;
            score += secondSquare.value;
            square.value = 0;
        } else if (secondSquare.value == 0){
            secondSquare.value = square.value;
            square.value = 0;
            moveSquareLeft(secondSquare);
        }
    }

    private void moveSquaresRight() {
        squares.sort((square1, square2) -> Integer.compare(square2.x, square1.x));
        for(int i = 0; i < squares.size(); i++) {
            Square square = squares.get(i);
            moveSquareRight(square);
        }
    }

    private void moveSquareRight(Square square) {
        if (square.value == 0 || square.x == 230) {
            return;
        }
        Optional<Square> SquareOnRight = squares.stream().filter(square1 -> (square1.x == square.x + 50) && (square1.y == square.y)).findFirst();
        Square secondSquare = SquareOnRight.get();
        if (square.value == secondSquare.value) {
            secondSquare.value = secondSquare.value * 2;
            score += secondSquare.value;
            square.value = 0;
        } else if (secondSquare.value == 0){
            secondSquare.value = square.value;
            square.value = 0;
            moveSquareRight(secondSquare);
        }
    }

    private void moveSquaresUp() {
        squares.sort(Comparator.comparingInt(square -> square.y));
        for(int i = 0; i < squares.size(); i++) {
            Square square = squares.get(i);
            moveSquareUp(square);
        }
    }

    private void moveSquareUp(Square square) {
        if (square.value == 0 || square.y == 80) {
            return;
        }
        Optional<Square> SquareAbove = squares.stream().filter(square1 -> (square1.x == square.x) && (square1.y == square.y - 50)).findFirst();
        Square secondSquare = SquareAbove.get();
        if (square.value == secondSquare.value) {
            secondSquare.value = secondSquare.value * 2;
            score += secondSquare.value;
            square.value = 0;
        } else if (secondSquare.value == 0){
            secondSquare.value = square.value;
            square.value = 0;
            moveSquareUp(secondSquare);
        }
    }

    private void moveSquareDown(Square square) {
        if (square.value == 0 || square.y == 230) {
            return;
        }
        Optional<Square> SquareBelow = squares.stream().filter(square1 -> (square1.x == square.x) && (square1.y == square.y + 50)).findFirst();
        Square secondSquare = SquareBelow.get();

        if (square.value == secondSquare.value) {
            secondSquare.value = secondSquare.value * 2;
            score += secondSquare.value;
            square.value = 0;
        } else if (secondSquare.value == 0){
            secondSquare.value = square.value;
            square.value = 0;
            moveSquareDown(secondSquare);
        }
    }
    private void moveSquaresDown() {
        squares.sort((square1, square2) -> Integer.compare(square2.y, square1.y));
        for(int i = 0; i < squares.size(); i++) {
            Square square = squares.get(i);
            moveSquareDown(square);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
