import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
public class HighScoreManager {
    private static final String SCORE_FILE = "highscore.properties";
    private Properties properties;
    private int highScore;

    public HighScoreManager() {
        properties = new Properties();
        loadHighScore();
    }

    public void loadHighScore() {
        try (FileInputStream in = new FileInputStream(SCORE_FILE)) {
            properties.load(in);
            String score = properties.getProperty("highScore");
            highScore = score != null ? Integer.parseInt(score) : 0;
        } catch (IOException e) {
            highScore = 0;
        }
    }

    public void setHighScore(int score) {
        if (score > highScore) {
            highScore = score;
            saveHighScore();
        }
    }

    public int getHighScore() {
        return highScore;
    }

    private void saveHighScore() {
        properties.setProperty("highScore", String.valueOf(highScore));
        try (FileOutputStream out = new FileOutputStream(SCORE_FILE)) {
            properties.store(out, "High Score Data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
