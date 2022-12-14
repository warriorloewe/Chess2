package Library;

public class ButtonBox {
    private Button[][] buttons;
    private int selectedY;
    private int selectedX;
    private boolean visible = true;

    public ButtonBox(int countHorizontal, int countVertical) {
        this(0, 0, countHorizontal, countVertical);
    }

    public ButtonBox(int selectedY, int selectedX, int countHorizontal, int countVertical) {
        this.buttons = new Button[countHorizontal][countVertical];
        this.selectedY = selectedY;
        this.selectedX = selectedX;
    }

    public void replaceButton(int x, int y, Button button) {
        buttons[x][y] = button;
        if (x == selectedX && y == selectedY) {
            button.setSelected(true);
        }
    }

    public Button[][] getButtons() {
        return buttons;
    }

    public void setButtons(Button[][] buttons) {
        this.buttons = buttons;
    }

    public Button getSelectedButton() {
        return buttons[selectedX][selectedY];
    }

    public int getSelectedY() {
        return selectedY;
    }

    public void setSelectedY(int selectedY) {
        this.selectedY = selectedY;
    }

    public int getSelectedX() {
        return selectedX;
    }

    public void setSelectedX(int selectedX) {
        this.selectedX = selectedX;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
