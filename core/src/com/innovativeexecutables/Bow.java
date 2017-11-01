package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;

public class Bow extends Weapon {
    public Bow(int x, int y) {
        super(x, y);
    }

    @Override
    public void loadTexture() {
        texture = new Texture("bow.png");
        super.loadTexture();
    }
}
