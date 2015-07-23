package com.afsis.yieldestimator.util;

import android.app.Application;

import com.afsis.yieldestimator.R;
import com.parse.Parse;
import com.parse.ParseCrashReporting;

/**
 * Maintain global Parse application state
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_app_key));
    }
  }