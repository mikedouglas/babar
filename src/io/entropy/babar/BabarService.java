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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "BabarService started.");

        final WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                300, 300,
                WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        elephants = new ArrayList<Elephant>();
        elephants.add(new Elephant(this));

        for (View elephant : elephants) {
            lp.y -= Math.random() * 300;
            wm.addView(elephant, lp);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "BabarService finished.");
    }
}
