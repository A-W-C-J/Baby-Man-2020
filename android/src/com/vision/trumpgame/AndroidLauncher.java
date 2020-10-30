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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.lang.reflect.Method;

import es.dmoral.toasty.Toasty;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {
    public RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        final AndroidShareChallenge shareChallenge = new AndroidShareChallenge(this);
        Klooni game = new Klooni(shareChallenge, this);
        View gameView = initializeForView(game, config);
        RelativeLayout.LayoutParams gameViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        gameView.setLayoutParams(gameViewParams);
        RelativeLayout.LayoutParams adViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        adViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        RelativeLayout.LayoutParams adViewBottomParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adViewBottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        adViewBottomParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layout.addView(gameView);
        setContentView(layout);
    }


    @Override
    public void inAppReview() {
//        runOnUiThread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        if (Klooni.getInAppReview()) {
//                            manager = ReviewManagerFactory.create(AndroidLauncher.this);
//                            Task<ReviewInfo> request = manager.requestReviewFlow();
//                            request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
//                                @Override
//                                public void onComplete(Task<ReviewInfo> task) {
//                                    if (task.isSuccessful()) {
//                                        ReviewInfo reviewInfo = task.getResult();
//                                        Task<Void> flow = manager.launchReviewFlow(AndroidLauncher.this, reviewInfo);
//                                        Log.e("review", "onComplete: " + flow.isComplete());
//                                        flow.addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(Task<Void> task) {
//                                                if (task != null && task.getResult() != null)
//                                                    Log.e("inAppReview", "onComplete: " + task.getResult().toString());
//                                            }
//                                        });
//                                        Klooni.setInAppReview(true);
//                                    } else {
//                                        Gdx.net.openURI("https://play.google.com/store/apps/dev?id=7544462411468692462");
//                                        Log.e("inAppReview", "onComplete: error");
//                                    }
//                                }
//                            });
//                        } else
//                            Gdx.net.openURI("https://play.google.com/store/apps/dev?id=7544462411468692462");
//                    }
//                }
//        );
        Gdx.net.openURI("https://play.google.com/store/apps/dev?id=7544462411468692462");
    }


    @Override
    public void showToast(final String msg, final boolean isError) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isError)
                    Toasty.error(AndroidLauncher.this, msg, Toast.LENGTH_LONG, true).show();
                else
                    Toasty.info(AndroidLauncher.this, msg, Toast.LENGTH_SHORT, true).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e("TAG", "onStart: ");

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
