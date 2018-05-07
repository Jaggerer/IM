package com.example.im;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Created by ganchenqing on 2018/3/28.
 */

public class IMNotificationManager extends ContextWrapper {
    private static volatile IMNotificationManager INSTANCE;


    public IMNotificationManager(Context base) {
        super(base);
    }

    public static IMNotificationManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (IMNotificationManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IMNotificationManager(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    private void showNotification(String title, String content, String ticker, Intent intent) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getApplicationInfo().loadIcon(this.getPackageManager());
        Bitmap appIcon = bitmapDrawable.getBitmap();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder mBuilder = new Notification.Builder(this)
                .setTicker(ticker)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(this.getApplicationInfo().icon)
                .setLargeIcon(appIcon)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        manager.notify(0, notification);
    }

    public void showNotification(Intent intent, String from) {
        showNotification("提示", "您收到一条新消息", "来自:" + from, intent);
    }

}
