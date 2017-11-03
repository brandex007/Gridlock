package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;

public class Greaves extends Weapon {
    public Greaves(int x, int y) {
        super(x, y);
    }

    @Override
    public void loadTexture() {
        texture = new Texture("greaves.png");
        super.loadTexture();
    }
}