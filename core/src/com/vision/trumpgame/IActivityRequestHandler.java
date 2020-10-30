package com.vision.trumpgame;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.vision.trumpgame.actors.SoftButton;
import com.vision.trumpgame.game.Board;
import com.vision.trumpgame.screens.GameScreen;

public interface IActivityRequestHandler {


    void inAppReview();

    void showToast(String msg, boolean isError);
}
