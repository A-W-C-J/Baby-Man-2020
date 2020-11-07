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
package com.vision.trumpgame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;
import java.util.Map;

import com.vision.trumpgame.effects.DonaldEffectFactory;
import com.vision.trumpgame.effects.EvaporateEffectFactory;
import com.vision.trumpgame.effects.FightForYouEffectFactory;
import com.vision.trumpgame.effects.GreatWallEffectFactory;
import com.vision.trumpgame.effects.InDreamEffectFactory;
import com.vision.trumpgame.interfaces.IEffectFactory;
import com.vision.trumpgame.screens.MainMenuScreen;
import com.vision.trumpgame.screens.SplashScreen;
import com.vision.trumpgame.screens.TransitionScreen;

public class Klooni extends Game {

    //region Members

    // FIXME theme should NOT be static as it might load textures which will expose it to the race condition iff GDX got initialized before or not
    public static Theme theme;
    public IEffectFactory effect;
    public boolean isRemove = false;
    // ordered list of effects. index 0 will get default if VanishEffectFactory is removed from list
    public final static IEffectFactory[] EFFECTS = {
            new DonaldEffectFactory(),
            new GreatWallEffectFactory(),
            new FightForYouEffectFactory(),
            new EvaporateEffectFactory(),
            new InDreamEffectFactory()
//            new ReallyRichEffectFactory()


    };

    private Map<String, Sound> effectSounds;
    public Skin skin;

    public final ShareChallenge shareChallenge;
    public final IActivityRequestHandler iActivityRequestHandler;

    public static boolean onDesktop;


    public static final int GAME_HEIGHT = 960;
    public static final int GAME_WIDTH = 408;
    private static final long SPLASH_MINIMUM_MILLIS = 4000L;

    //endregion
    //region Creation
    // TODO Possibly implement a 'ShareChallenge'
    //      for other platforms instead passing null
    public Klooni(final ShareChallenge shareChallenge, final IActivityRequestHandler activityRequestHandler) {
        this.shareChallenge = shareChallenge;
        this.iActivityRequestHandler = activityRequestHandler;
    }

    public Klooni(final ShareChallenge shareChallenge) {
        this.shareChallenge = shareChallenge;
        this.iActivityRequestHandler = null;
    }

    @Override
    public void create() {
        onDesktop = Gdx.app.getType().equals(Application.ApplicationType.Desktop);
        prefs = Gdx.app.getPreferences("com.vision.trumpgame.game");
        // Load the best match for the skin (depending on the device screen dimensions)
        skin = SkinLoader.loadSkin();

        // Use only one instance for the theme, so anyone using it uses the most up-to-date
        Theme.skin = skin; // Not the best idea
        final String themeName = prefs.getString("themeName", "dark");
        if (Theme.exists(themeName))
            theme = Theme.getTheme(themeName);
        else
            theme = Theme.getTheme("default");

        Gdx.input.setCatchBackKey(true); // To show the pause menu
//        setScreen(new MainMenuScreen(this));
        setScreen(new SplashScreen());

        final long splash_start_time = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        long splash_elapsed_time = System.currentTimeMillis() - splash_start_time;
                        if (splash_elapsed_time < Klooni.SPLASH_MINIMUM_MILLIS) {
                            Timer.schedule(
                                    new Timer.Task() {
                                        @Override
                                        public void run() {
                                            Klooni.this.setScreen(new MainMenuScreen(Klooni.this));
                                        }
                                    }, (float) (Klooni.SPLASH_MINIMUM_MILLIS - splash_elapsed_time) / 1000f);
                        } else {
                            Klooni.this.setScreen(new MainMenuScreen(Klooni.this));
                        }
                    }
                });
            }
        }).start();
        String effectName = prefs.getString("effectName", "Trump");
        effectSounds = new HashMap<String, Sound>(EFFECTS.length);
        effect = EFFECTS[0];
        for (IEffectFactory e : EFFECTS) {
            loadEffectSound(e.getName());
            if (e.getName().equals(effectName)) {
                effect = e;
            }
        }
    }

    //endregion

    //region Screen

    // TransitionScreen will also dispose by default the previous screen
    public void transitionTo(Screen screen) {
        transitionTo(screen, true);
    }

    public void transitionTo(Screen screen, boolean disposeAfter) {
        setScreen(new TransitionScreen(this, getScreen(), screen, disposeAfter));
    }

    //endregion

    //region Disposing

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        theme.dispose();
        if (effectSounds != null) {
            for (Sound s : effectSounds.values()) {
                s.dispose();
            }
            effectSounds = null;
        }
        getScreen().dispose();
        Gdx.app.exit();
    }

    //endregion

    // region Effects

    private void loadEffectSound(final String effectName) {
        FileHandle soundFile = Gdx.files.internal("sound/effect_" + effectName + ".mp3");
        if (!soundFile.exists())
            soundFile = Gdx.files.internal("sound/effect_trump.mp3");

        effectSounds.put(effectName, Gdx.audio.newSound(soundFile));
    }

    public void playEffectSound() {
        effectSounds.get(effect.getName())
                .play(MathUtils.random(0.7f, 1f), MathUtils.random(0.8f, 1.2f), 0);
    }

    // endregion

    //region Settings

    private static Preferences prefs;

    // Score related
    public static int getMaxScore() {
        return prefs.getInteger("maxScore", 0);
    }

    public static int getMaxTimeScore() {
        return prefs.getInteger("maxTimeScore", 0);
    }

    public static void setMaxScore(int score) {
        prefs.putInteger("maxScore", score).flush();
    }

    public static void setMaxTimeScore(int maxTimeScore) {
        prefs.putInteger("maxTimeScore", maxTimeScore).flush();
    }

    // Settings related
    public static boolean soundsEnabled() {
        return !prefs.getBoolean("muteSound", false);
    }

    public static boolean toggleSound() {
        final boolean result = soundsEnabled();
        prefs.putBoolean("muteSound", result).flush();
        return !result;
    }

    public static boolean shouldSnapToGrid() {
        return prefs.getBoolean("snapToGrid", false);
    }

    public static boolean toggleSnapToGrid() {
        final boolean result = !shouldSnapToGrid();
        prefs.putBoolean("snapToGrid", result).flush();
        return result;
    }

    // Themes related
    public static boolean isThemeBought(Theme theme) {
        if (theme.getPrice().equals("0"))
            return true;

        String[] themes = prefs.getString("boughtThemes", "").split(":");
        for (String t : themes)
            if (t.equals(theme.getName()))
                return true;

        return false;
    }

    public static void updateTheme(Theme newTheme) {
        prefs.putString("themeName", newTheme.getName()).flush();
        theme.update(newTheme.getName());
    }


    // Effects related
    public static boolean isEffectBought(IEffectFactory effect) {
        if (effect.getPrice().equals("0"))
            return true;
        String[] effects = prefs.getString("boughtEffects", "").split(":");
        for (String e : effects)
            if (e.equals(effect.getName()))
                return true;

        return false;
    }


    public void updateEffect(IEffectFactory newEffect) {
        prefs.putString("effectName", newEffect.getName()).flush();
        // Create a new effect, since the one passed through the parameter may dispose later
        effect = newEffect;


    }

    public static void setBoardSize(float boardSize) {
        prefs.putFloat("BoardSize", boardSize).flush();
    }

    public static float getBoardSize() {
        return prefs.getFloat("BoardSize");
    }

    public static void setInAppReview(boolean isReview) {
        Gdx.app.getPreferences("com.vision.trumpgame.game").putBoolean("isReview", isReview).flush();
        
    }

    public static boolean getInAppReview() {
        return Gdx.app.getPreferences("com.vision.trumpgame.game").getBoolean("isReview");
    }

    //endregion
}
