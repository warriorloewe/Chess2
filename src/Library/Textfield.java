package Library;

import Overlay.GUI;
import java.awt.*;

public class Textfield {
    private final Color bgColor = Color.DARK_GRAY;
    String text = "";
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private boolean visible;
    private Color textColor = Color.LIGHT_GRAY;
    private Font font = new Font(Font.DIALOG, Font.BOLD, 50);

    public Textfield(double xPerc, double yPerc, double widthPerc, double heightPerc, String text) {
        this.x = (int) (xPerc * GUI.window.width);
        this.y = (int) (yPerc * GUI.window.height);
        this.width = (int) (widthPerc * GUI.window.width);
        this.height = (int) (heightPerc * GUI.window.height);
        this.text = text;
    }

    public void paint(Graphics g) {
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);

        int stringX = x + (width - metrics.stringWidth(text)) / 2;
        int stringY = y + metrics.getAscent() + (height - metrics.getHeight()) / 2;

        g.setColor(bgColor);
        g.fillRect(x, y, width, height);

        g.setColor(textColor);
        g.drawString(text, stringX, stringY);
    }
    /*
    public void cutString(Graphics graphics, FontMetrics metrics, String text) {
        for (int i = text.length() - 1; i > 0; i--) {
            if (metrics.stringWidth(s.substring(0, i)) < width - 50 && s.charAt(i) == ' ') {
                graphics.drawString(s.substring(0, i), stringX, stringY);
                stringY += metrics.getHeight();
                if (metrics.stringWidth(s.substring(i + 1)) > width - 50) {
                    cutString(s.substring(i + 1), graphics, metrics);
                } else {
                    graphics.drawString(s.substring(i + 1), stringX, stringY);
                }
                break;
            }
        }
    }
    */

    public void setText(String text) {
        this.text = text;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
