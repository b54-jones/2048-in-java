import java.awt.*;

public class Square {
    int x;

    int y;

    Color color;

    int height = 40;
    int width = 40;

    int value;

    public void setValue(int value) {

        this.value = value;
        updateColor();
    }

    public void doubleValue() {
        this.value *= 2;
        updateColor();
    }

    public void updateColor() {
        switch (this.value) {
            case 0:
                this.color = Color.LIGHT_GRAY;
                break;
            case 2:
                this.color = Color.WHITE;
                break;
            case 4:
                this.color = Color.GREEN;
                break;
            case 8:
                this.color = Color.PINK;
                break;
            case 16:
                this.color = Color.ORANGE;
                break;
            case 32:
                this.color = Color.RED;
                break;
            case 64:
                this.color = Color.CYAN;
                break;
            case 128:
                this.color = Color.YELLOW;
                break;
            case 256:
                this.color = Color.MAGENTA;
                break;
            case 512:
                this.color = Color.blue;
                break;
            default:
                this.value = 0;
                updateColor();
        }
    }

    Square(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = 0;
    }
}
