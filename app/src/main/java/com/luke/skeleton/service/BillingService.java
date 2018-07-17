package com.luke.skeleton.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.luke.skeleton.R;
import com.luke.skeleton.app.Constants;
import com.luke.skeleton.model.Bill;
import com.luke.skeleton.storage.BillDataProvider;
import com.orhanobut.hawk.Hawk;

import org.joda.time.DateTime;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BillingService extends Service {

    public enum Action {
        START,
        STOP,
        PAUSE,
        RESUME
    }

    private static final String EXTRA_ACTION = "EXTRA_ACTION";
    private static final String CHANNEL_ID = "com.luke.skeleton.notif_channel";
    private static final int NOTIF_ID = 1;

    public static void commandBillingService(Context context, Action action) {
        Intent intent = new Intent(context, BillingService.class);
        intent.putExtra(EXTRA_ACTION, action);
        ContextCompat.startForegroundService(context, intent);
    }

    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> scheduledFuture = null;

    private Action lastAction = null;
    private float currentBilling;

    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        executor = new ScheduledThreadPoolExecutor(1);
        createNotificationChannel();
        handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Action action = (Action) intent.getSerializableExtra(EXTRA_ACTION);
        if(action == null) {
            action = Action.START;
        }

        if(lastAction == null && action != Action.START) {
            updateNotification(true);
            stopSelf(startId);
        }

        switch (action) {
            case START:
                startBilling();
                break;
            case STOP:
                stopBilling();
                break;
            case RESUME:
                resumeBilling();
                break;
            case PAUSE:
                pauseBilling();
                break;
        }

        updateNotification(action == Action.PAUSE);

        if(action == Action.STOP) {
            stopSelf(startId);
            lastAction = null;
        } else {
            lastAction = action;
        }

        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable billingTask = new Runnable() {

        @Override
        public void run() {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    currentBilling += Constants.RATE_PER_MINUTE;
                    updateNotification();
                }
            });
        }
    };

    private void checkForPrevUnexpectedStopBilling() {
        if(Hawk.contains(Constants.KEY_LAST_BILL_VALUE)) {
            Bill bill = new Bill((float) Hawk.get(Constants.KEY_LAST_BILL_VALUE), DateTime.now());
            BillDataProvider.getInstance().addBill(bill);
            Hawk.delete(Constants.KEY_LAST_BILL_VALUE);
        }
    }

    private void startBilling() {
        if(scheduledFuture == null) {
            checkForPrevUnexpectedStopBilling();
            currentBilling = Constants.DEFAULT_FLAT_RATE;
            scheduleBillingTask();
        }
    }

    private void stopBilling() {
        if(lastAction != Action.STOP) {
            float finalBilling = cancelBillingTask();

            Bill bill = new Bill(finalBilling, DateTime.now());
            BillDataProvider.getInstance().addBill(bill);
            Hawk.delete(Constants.KEY_LAST_BILL_VALUE);
        }
    }

    private void pauseBilling() {
        if(lastAction == Action.START) {
            currentBilling = cancelBillingTask();
            Hawk.put(Constants.KEY_LAST_BILL_VALUE, currentBilling);
        }
    }

    private void resumeBilling() {
        if(lastAction == Action.PAUSE) {
            scheduleBillingTask();
        }
    }

    private void scheduleBillingTask() {
        scheduledFuture = executor.scheduleWithFixedDelay(billingTask,
                Constants.BILLING_PERIOD_SEC, Constants.BILLING_PERIOD_SEC, TimeUnit.SECONDS);
    }

    private float cancelBillingTask() {
        boolean shouldAddLastRate = false;
        if(scheduledFuture != null) {
            shouldAddLastRate = scheduledFuture.getDelay(TimeUnit.SECONDS) <=
                    Constants.BILLING_PERIOD_SEC / 2;
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
        return currentBilling + (shouldAddLastRate ? Constants.RATE_PER_MINUTE : 0);
    }

    private void updateNotification() {
        updateNotification(false);
    }

    private void updateNotification(boolean paused) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(
                        paused ? R.string.notification_text_paused : R.string.notification_text, currentBilling));

        if(paused) {
            notificationBuilder.addAction(getStopAction());
        }

        startForeground(NOTIF_ID, notificationBuilder.build());
    }

    private NotificationCompat.Action getStopAction() {
        Intent intent = new Intent(this, BillingService.class);
        intent.putExtra(EXTRA_ACTION, Action.STOP);
        PendingIntent pendingIntent =
                PendingIntent.getService(this, 0, intent, 0);
        return new NotificationCompat.Action(-1, getString(R.string.stop), pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = getString(R.string.notif_channel_name);
            String description = getString(R.string.notif_channel_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
