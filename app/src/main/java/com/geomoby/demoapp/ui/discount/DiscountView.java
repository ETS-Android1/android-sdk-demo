package com.geomoby.demoapp.ui.discount;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface DiscountView extends MvpView {
    void onSetBackground(int background);
    void onSetText(final String text);
    void onSetImage(int image);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void onStartMainActivity();
}
