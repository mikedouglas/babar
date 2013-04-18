package io.entropy.babar;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class BabarService extends Service {
    public static final String TAG = BabarService.class.getName();

    private int _xDelta;
    private int _yDelta;

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

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.elephant);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick called");
                Intent launchIntent = new Intent(BabarService.this, LaunchActivity.class);
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
            }
        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, event.toString());

                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        _xDelta = X;
                        _yDelta = Y;
                    case MotionEvent.ACTION_MOVE:
                        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) v.getLayoutParams();
                        lp.x = X - _xDelta;
                        lp.y = Y - _yDelta;
                        wm.updateViewLayout(v, lp);
                        break;
                }
                return false;
            }
        });

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
