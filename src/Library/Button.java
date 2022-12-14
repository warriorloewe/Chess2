package Library;

import Overlay.GUI;

import java.awt.image.BufferedImage;

public class Button {
    private final BufferedImage buttonImageUnselected;
    private final BufferedImage buttonImageSelected;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private Listener listener;
    private boolean selected = false;

    public Button(double xPerc, double yPerc, double widthPerc, double heightPerc, BufferedImage buttonImageUnselected, BufferedImage buttonImageSelected, Listener listener) {
        this.x = (int) (xPerc * GUI.window.width);
        this.y = (int) (yPerc * GUI.window.height);
        this.width = (int) (widthPerc * GUI.window.width);
        this.height = (int) (heightPerc * GUI.window.height);

        this.buttonImageUnselected = buttonImageUnselected;
        this.buttonImageSelected = buttonImageSelected;
        this.listener = listener;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public BufferedImage getImage() {
        return selected ? buttonImageSelected : buttonImageUnselected;
    }

    public Listener getActionListener() {
        return listener;
    }

    public void setActionListener(Listener listener) {
        this.listener = listener;
    }

}
