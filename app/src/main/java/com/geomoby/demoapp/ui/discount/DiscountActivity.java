package com.geomoby.demoapp.ui.discount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.geomoby.demoapp.R;
import com.geomoby.demoapp.ui.main.MainActivity;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;

public class DiscountActivity extends MvpAppCompatActivity implements DiscountView {

    @InjectPresenter
    DiscountPresenter mDiscountPresenter;

    private TextView mDiscountText;
    private ImageView mDiscountImage;
    private ImageView mDiscountBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        mDiscountText = findViewById(R.id.discountText);
        mDiscountImage = findViewById(R.id.discountImage);
        mDiscountBackground = findViewById(R.id.discountBackground);

        ImageView discountBackButton = findViewById(R.id.discountBackButton);
        discountBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDiscountPresenter.finishActivity();
            }
        });

        mDiscountPresenter.activityCreated(getIntent());
    }

    @Override
    public void onSetBackground(int background) {
        mDiscountBackground.setImageResource(background);
    }

    @Override
    public void onSetText(String text) {
        mDiscountText.setText(text);
    }

    @Override
    public void onSetImage(int image) {
        mDiscountImage.setImageResource(image);
    }

    @Override
    public void onStartMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
