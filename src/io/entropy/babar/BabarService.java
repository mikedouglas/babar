package io.entropy.babar;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

public class BabarService extends Service {
    public static final String TAG = BabarService.class.getName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "BabarService started.");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                300, 300,
                WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.elephant);
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        wm.addView(imageView, lp);

        lp = (WindowManager.LayoutParams)imageView.getLayoutParams();
        lp.y = lp.y - 300;
        wm.updateViewLayout(imageView, lp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "BabarService finished.");
    }
}
