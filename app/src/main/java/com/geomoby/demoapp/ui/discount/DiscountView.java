package com.geomoby.demoapp.ui.discount;


import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface DiscountView extends MvpView {
    void onSetBackground(int background);
    void onSetText(final String text);
    void onSetImage(int image);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void onStartMainActivity();
}
