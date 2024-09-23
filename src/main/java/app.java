import javax.swing.*;

public class app {
    public static void main(String[] args) {
        int boardWidth = 500;
        int boardHeight = 500;

        JFrame frame = new JFrame("2048");

        // Positions in middle of screen
        frame.setLocationRelativeTo(null);

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(boardWidth, boardHeight);

        GameBoard gameBoard = new GameBoard();
        frame.add(gameBoard);
        frame.pack();
        gameBoard.requestFocus();


        frame.setVisible(true);
    }
}
