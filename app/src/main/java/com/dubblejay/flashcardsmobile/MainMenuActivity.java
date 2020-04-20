package com.dubblejay.flashcardsmobile;

import android.support.v4.app.Fragment;

public class MainMenuActivity extends FragmentActivityTemplate {
    @Override
    protected Fragment createFragment() {
        return new MainMenuFragment();
    }
}
