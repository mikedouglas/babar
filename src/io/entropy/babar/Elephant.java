package io.entropy.babar;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class Elephant extends ImageView {
    public static final String TAG = BabarService.class.getName();
    public static final int DEFAULT_SIZE = 300;

    private int _xLast, _yLast;
    private int _xOrig, _yOrig;

    public Elephant(Context context) {
        super(context);

        final WindowManager wm = (WindowManager)context.getSystemService(Service.WINDOW_SERVICE);

        setImageResource(R.drawable.elephant);
        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                //Log.d(TAG, event.toString());

                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        _xOrig = _xLast = X;
                        _yOrig = _yLast = Y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) v.getLayoutParams();
                        lp.x += X - _xLast;
                        lp.y += Y - _yLast;
                        _xLast = X;
                        _yLast = Y;
                        Log.d(TAG, String.format("x: %d, y: %d", lp.x, lp.y));
                        wm.updateViewLayout(v, lp);
                        break;
                    case MotionEvent.ACTION_UP:
                        /*
                        if ((_xOrig - X) < 5 && (_yOrig - Y) < 5) {
                            Intent dialogIntent = new Intent(getBaseContext(), LaunchActivity.class);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplication().startActivity(dialogIntent);
                        }*/
                        break;
                }
                return false;
            }
        });
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }
}
