package io.entropy.babar;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class BabarService extends Service {
    public static final String TAG = BabarService.class.getName();

    private List<Elephant> elephants;

    private int _xLast, _yLast;
    private int _xOrig, _yOrig;

    private int bindCount = 0;

    @Override
    public IBinder onBind(Intent intent) {
        bindCount++;
        for (Elephant e : elephants) {
            e.hide();
        }
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);

        bindCount++;
        for (Elephant e : elephants) {
            e.hide();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        bindCount--;
        if (bindCount == 0) {
            for (Elephant e : elephants) {
                e.show();
            }
        }
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "BabarService started.");

        final WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                150, 150,
                WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        elephants = new ArrayList<Elephant>();
        elephants.add(new Elephant(this, "TaskProvider Title"));
        elephants.add(new Elephant(this, "TaskProvider Title 2"));

        for (View elephant : elephants) {
            wm.addView(elephant, lp);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "BabarService finished.");
    }
}
