/*
    1010! Klooni, a free customizable puzzle game for Android and Desktop
    Copyright (C) 2017-2019  Lonami Exo @ lonami.dev

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.vision.trumpgame.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.vision.trumpgame.interfaces.IEffect;
import com.vision.trumpgame.interfaces.IEffectFactory;
import com.vision.trumpgame.game.Cell;


public class ReallyRichEffectFactory implements IEffectFactory {
    private Texture dropTexture;

    @Override
    public String getName() {
        return "rich";
    }

    @Override
    public String getDisplay() {
        return "Rich";
    }

    @Override
    public String  getPrice() {
        return "0";
    }

    @Override
    public IEffect create(Cell deadCell, Vector2 culprit) {

        IEffect effect = new WaterdropEffect();
        effect.setInfo(deadCell, culprit);
        return effect;
    }


    private class WaterdropEffect implements IEffect {
        private Vector2 pos;
        private boolean dead;

        private Color cellColor;
        private Color dropColor;
        private float cellSize;

        private final float fallAcceleration;
        private float fallSpeed;

        private static final float FALL_ACCELERATION = 500.0f;
        private static final float FALL_VARIATION = 50.0f;
        private static final float COLOR_SPEED = 7.5f;

        WaterdropEffect() {
            fallAcceleration = FALL_ACCELERATION + MathUtils.random(-FALL_VARIATION, FALL_VARIATION);
        }

        @Override
        public void setInfo(Cell deadCell, Vector2 culprit) {
            pos = deadCell.pos.cpy();
            cellSize = deadCell.size;
            cellColor = deadCell.getColorCopy();
            dropColor = new Color(cellColor.r, cellColor.g, cellColor.b, 0.0f);
        }

        @Override
        public void draw(Batch batch) {
            final float dt = Gdx.graphics.getDeltaTime();
            fallSpeed += fallAcceleration * dt;
            pos.y -= fallSpeed * dt;

            cellColor.set(
                    cellColor.r, cellColor.g, cellColor.b,
                    Math.max(cellColor.a - COLOR_SPEED * dt, 0.0f)
            );
            dropColor.set(
                    cellColor.r, cellColor.g, cellColor.b,
                    Math.min(dropColor.a + COLOR_SPEED * dt, 1.0f)
            );

            Cell.draw(cellColor, batch, pos.x, pos.y, cellSize);
            Cell.draw(dropTexture, dropColor, batch, pos.x, pos.y, cellSize);

            final Vector3 translation = batch.getTransformMatrix().getTranslation(new Vector3());
            dead = translation.y + pos.y + dropTexture.getHeight() < 0;
        }

        @Override
        public boolean isDone() {
            return dead;
        }
    }
}
