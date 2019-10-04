package com.geomoby.demoapp.ui.discount;

import android.content.Intent;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import com.geomoby.demoapp.R;

@InjectViewState
public class DiscountPresenter extends MvpPresenter<DiscountView> {

    public void activityCreated(Intent intent) {
        String value = intent.getStringExtra("id");

        assert value != null;
        switch (value) {
            case "Enter":
                changeState("Welcome to our venue", R.mipmap.hotel, R.mipmap.bg_1);
                break;
            case "Exit":
                changeState("Good Bye and see you again soon", R.mipmap.good_bye, R.mipmap.bg_3);
                break;
            case "Drink":
                changeState("Get one drink for only $5", R.mipmap.drink, R.mipmap.bg_4);
                break;
            case "Offer":
                changeState("Today's Special offer", R.mipmap.offer, R.mipmap.bg_2);
                break;
        }
    }

    private void changeState(String text, int image, int background) {
        getViewState().onSetText(text);
        getViewState().onSetImage(image);
        getViewState().onSetBackground(background);
    }

    public void finishActivity() {
        getViewState().onStartMainActivity();
    }
}
