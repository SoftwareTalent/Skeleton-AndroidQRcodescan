package com.luke.skeleton.app;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Context;

import com.luke.skeleton.service.BillingService;
import com.luke.skeleton.service.BillingService.Action;
import com.luke.skeleton.utils.GsonUtils;
import com.orhanobut.hawk.GsonParser;
import com.orhanobut.hawk.Hawk;

import net.danlew.android.joda.JodaTimeAndroid;

public class CustomApp extends Application implements LifecycleObserver {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        JodaTimeAndroid.init(this);
        Hawk.init(context).setParser(new GsonParser(GsonUtils.getDefaultGson())).build();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public static Context getContext() {
        return context;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        BillingService.commandBillingService(this, Action.RESUME);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        BillingService.commandBillingService(this, Action.PAUSE);
    }

}
