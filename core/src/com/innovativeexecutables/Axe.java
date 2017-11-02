package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;

public class Axe extends Weapon {
    public Axe(int x, int y) {
        super(x, y);
    }

    @Override
    public void loadTexture() {
        texture = new Texture("axe.png");
        super.loadTexture();
    }
}
