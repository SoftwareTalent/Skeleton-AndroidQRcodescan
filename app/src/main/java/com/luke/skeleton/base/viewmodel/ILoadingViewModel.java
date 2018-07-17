package com.luke.skeleton.base.viewmodel;

import android.databinding.Bindable;

public interface ILoadingViewModel extends IViewModel {

    @Bindable
    boolean isLoadingIndicatorEnabled();

    @Bindable
    int getLoadingVisibility();

}
