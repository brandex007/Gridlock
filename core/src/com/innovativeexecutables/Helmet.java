package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;

public class Helmet extends Weapon {
    public Helmet(int x, int y) {
        super(x, y);
    }

    @Override
    public void loadTexture() {
        texture = new Texture("helmet.png");
        super.loadTexture();
    }
}
