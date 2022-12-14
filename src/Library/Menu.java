package Library;

import java.awt.*;
import java.util.ArrayList;

public class Menu {

    private ButtonBox buttonBox;
    private final ArrayList<Textfield> textFields;

    public Menu() {
        textFields = new ArrayList<>();
    }

    public void paint(Graphics g) {
        if (buttonBox != null) {
            if (buttonBox.isVisible()) {
                drawButtons(g);
            }
        }
        for (Textfield textField : textFields) {
            if (textField.isVisible()) {
                textField.paint(g);
            }
        }
    }

    private void drawButtons(Graphics g) {
        for (Button[] buttonsX : buttonBox.getButtons()) {
            for (Button button : buttonsX) {
                g.drawImage(button.getImage(), button.getX(), button.getY(), button.getWidth(), button.getHeight(), null);
                /*
                if (button.getCheckBox() != null) {
                    CheckBox cb = button.getCheckBox();
                    graphics.drawImage(cb.getImage(), cb.getX(), cb.getX(), cb.getWidth(), cb.getHeight(), null);
                }
                */
            }
        }
    }

    public void perform() {
        if(buttonBox != null) {
            buttonBox.getSelectedButton().getActionListener().perform();
        }
    }

    public ButtonBox getButtonBox() {
        return buttonBox;
    }

    public void setButtonBox(ButtonBox box) {
        this.buttonBox = box;
    }

    public void addTextField(Textfield field) {
        textFields.add(field);
    }

    public ArrayList<Textfield> getTextFields() {
        return textFields;
    }
}
