package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;

public class Spear extends Weapon {
    public Spear(int x, int y) {
        super(x, y);
    }

    @Override
    public void loadTexture() {
        texture = new Texture("spear.png");
        super.loadTexture();
    }
}
