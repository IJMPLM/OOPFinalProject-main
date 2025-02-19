package finalproject.Components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class dbButtons extends JButton {

    public static dbButtons selectedButton;

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        setBackground(color);
    }

    public Color getColorOver() {
        return colorOver;
    }

    public void setColorOver(Color colorOver) {
        this.colorOver = colorOver;
    }

    public Color getColorClick() {
        return colorClick;
    }

    public void setColorClick(Color colorClick) {
        this.colorClick = colorClick;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            selectedButton = this;
            setBackground(colorOver);
        } else {
            setBackground(color);
        }
    }


    public dbButtons() {
        setColor(new Color(55, 81, 107));
        colorOver = new Color(65, 100, 134);
        colorClick = new Color(87, 132, 177);
        borderColor = new Color(48, 71, 94);
        setContentAreaFilled(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                if (selectedButton != dbButtons.this) {
                    setBackground(colorOver);
                }
                over = true;
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if (selectedButton != dbButtons.this) {
                    setBackground(color);
                }
                over = false;
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (selectedButton != null && selectedButton != dbButtons.this) {
                    selectedButton.setBackground(selectedButton.getColor());
                }
                selectedButton = dbButtons.this; 
                setBackground(colorClick);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (over) {
                    setBackground(colorOver);
                } else {
                    setBackground(color);
                }
            }
        });
    }

    private boolean over;
    private Color color;
    private Color colorOver;
    private Color colorClick;
    private Color borderColor;
    private int radius = 0;

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(getBackground());

        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
        super.paintComponent(grphcs);
    }
}
