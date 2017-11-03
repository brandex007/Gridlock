package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;

public class Cuisses extends Weapon {
    public Cuisses(int x, int y) {
        super(x, y);
    }

    @Override
    public void loadTexture() {
        texture = new Texture("cuisses.png");
        super.loadTexture();
    }
}
