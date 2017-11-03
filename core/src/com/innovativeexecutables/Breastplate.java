package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;

public class Breastplate extends Weapon {
    public Breastplate(int x, int y) {
        super(x, y);
    }

    @Override
    public void loadTexture() {
        texture = new Texture("breastplate.png");
        super.loadTexture();
    }
}
