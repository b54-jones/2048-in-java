import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.awt.*;
import java.util.List;

public class GameBoard extends JPanel implements KeyListener {

    ArrayList<Square> squares;
    int score = 0;
    boolean gameOver = false;

    HighScoreManager manager;

    GameBoard() {
        int boardWidth = 350;
        int boardHeight = 350;

        squares = getSquares();
        manager = new HighScoreManager();

        addValueSquare();
        addValueSquare();

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);
        repaint();
    }

    private void addValueSquare() {
        List<Square> filledSquares = squares.stream().filter(square -> square.value != 0).toList();

        if (filledSquares.size() == 15) {
            gameOver = true;
            return;
        }

        Random rand = new Random();
        boolean placed = false;

        while (!placed) {
            int randomSquareIndex = rand.nextInt(squares.size());
            Square square = squares.get(randomSquareIndex);

            if (square.value == 0) {
                square.setValue(rand.nextInt(10) > 7 ? 4 : 2);
                placed = true;
            }
        }
    }

    private ArrayList<Square> getSquares() {
        ArrayList<Square> squares = new ArrayList<>();
        int x = 80;
        int y = 80;
        for (int i = 0; i < 16; i++) {
            Square newSquare = new Square(x, y);
            x += 50;
            if (x == 280) {
                x = 80;
                y += 50;
            }
            newSquare.setValue(0);
            squares.add(newSquare);
        }
        return squares;
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void drawCentralText(String text, Graphics graphics, int outerX, int outerY, int outerWidth, int outerHeight) {
        FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();

        int x = outerX + (outerWidth - textWidth) / 2;
        int y = outerY + (outerHeight - textHeight) / 2 + metrics.getAscent();

        graphics.drawString(text, x, y);
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.darkGray);
        graphics.fillRect(75, 75, 200, 200);

        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));

        graphics.drawString("Score: " + score, 60, 60);

        graphics.drawString("High Score: " + manager.getHighScore(), 170, 60);

        graphics.setColor(Color.BLACK);
        graphics.drawRect(74, 74, 201, 201);

        if (gameOver) {
            graphics.setColor(Color.white);

            drawCentralText("Game Over", graphics, 75, 75, 200, 200);
            return;
        }

        for (int i = 0; i < squares.size(); i++) {
            Square currentSquare = squares.get(i);
            graphics.setColor(currentSquare.color);
            graphics.fillRect(currentSquare.x, currentSquare.y, currentSquare.width, currentSquare.height);

            if (currentSquare.value != 0) {
                graphics.setColor(Color.BLACK);
                drawCentralText(String.valueOf(currentSquare.value), graphics, currentSquare.x, currentSquare.y, 40, 40);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
            gameOver = false;
            if (score > manager.getHighScore()) {
                manager.setHighScore(score);
            }
            score = 0;
            squares.forEach(square -> square.setValue(0));
            addValueSquare();
            addValueSquare();
        } else if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                moveSquaresDown();
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                moveSquaresUp();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                moveSquaresRight();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                moveSquaresLeft();
            }
            addValueSquare();
        }
        repaint();
    }

    private void moveSquaresLeft() {
        squares.sort(Comparator.comparingInt(square -> square.x));
        for (int i = 0; i < squares.size(); i++) {
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
        if (moveAgain(square, secondSquare)) {
            moveSquareLeft(secondSquare);
        }
    }

    private void moveSquaresRight() {
        squares.sort((square1, square2) -> Integer.compare(square2.x, square1.x));
        for (int i = 0; i < squares.size(); i++) {
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
        if (moveAgain(square, secondSquare)) {
            moveSquareRight(secondSquare);
        }
    }

    private boolean moveAgain(Square square, Square secondSquare) {
        if (square.value == secondSquare.value) {
            secondSquare.doubleValue();
            score += secondSquare.value;
            square.setValue(0);
            return false;
        } else if (secondSquare.value == 0){
            secondSquare.setValue(square.value);
            square.setValue(0);
            return true;
        }
        return false;
    }

    private void moveSquaresUp() {
        squares.sort(Comparator.comparingInt(square -> square.y));
        for (int i = 0; i < squares.size(); i++) {
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
        if (moveAgain(square, secondSquare)) {
            moveSquareUp(secondSquare);
        }
    }

    private void moveSquareDown(Square square) {
        if (square.value == 0 || square.y == 230) {
            return;
        }
        Optional<Square> SquareBelow = squares.stream().filter(square1 -> (square1.x == square.x) && (square1.y == square.y + 50)).findFirst();
        Square secondSquare = SquareBelow.get();

        if (moveAgain(square, secondSquare)) {
            moveSquareDown(secondSquare);
        }
    }

    private void moveSquaresDown() {
        squares.sort((square1, square2) -> Integer.compare(square2.y, square1.y));
        for (int i = 0; i < squares.size(); i++) {
            Square square = squares.get(i);
            moveSquareDown(square);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
