package com.github.leanframeworks.propertiesframework.swing.property;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

class TestIcon implements Icon {

    private static final int SIZE = 16;

    @Override
    public int getIconWidth() {
        return SIZE;
    }

    @Override
    public int getIconHeight() {
        return SIZE;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(Color.RED);
        g.drawRect(x, y, getIconWidth(), getIconHeight());
    }
}
