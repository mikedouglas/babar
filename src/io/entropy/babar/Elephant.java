package io.entropy.babar;

import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Elephant extends ImageView {
    public static final String TAG = BabarService.class.getName();

    private int _xLast, _yLast;
    private int _xLastLp, _yLastLp;
    private String _task;
    private TextView _taskTitleView;

    public Elephant(Context context, String task) {
        super(context);
        _task = task;

        final WindowManager wm = (WindowManager)context.getSystemService(Service.WINDOW_SERVICE);
        _taskTitleView = new TextView(getContext());
        _taskTitleView.setGravity(Gravity.CENTER);
        _taskTitleView.setText(task);
        _taskTitleView.setTextSize(35);
        _taskTitleView.setVisibility(View.INVISIBLE);
        _taskTitleView.setTextAppearance(getContext(), R.style.TaskOverlay);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                        PixelFormat.TRANSLUCENT);
        wm.addView(_taskTitleView, lp);
        _xLastLp = lp.x;
        _yLastLp = lp.y;

        setImageResource(R.drawable.elephant);
        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();

                WindowManager.LayoutParams lp = (WindowManager.LayoutParams) v.getLayoutParams();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        _xLast = X;
                        _yLast = Y;
                        _taskTitleView.setVisibility(View.VISIBLE);

                        lp.x = _xLastLp;
                        lp.y = _yLastLp;
                        wm.updateViewLayout(v, lp);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        lp.x += X - _xLast;
                        lp.y += Y - _yLast;

                        _xLastLp = lp.x;
                        _yLastLp = lp.y;

                        Log.d(TAG, Elephant.this + ": X " + lp.x + " Y " + lp.y);

                        _xLast = X;
                        _yLast = Y;
                        wm.updateViewLayout(v, lp);
                        break;
                    case MotionEvent.ACTION_UP:
                        /*
                        if ((_xOrig - X) < 5 && (_yOrig - Y) < 5) {
                            Intent dialogIntent = new Intent(getBaseContext(), TaskListActivity.class);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplication().startActivity(dialogIntent);
                        }*/
                        _taskTitleView.setVisibility(View.GONE);
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

    @Override
    public String toString() {
        return "Elephant(" + _task + ")";
    }
}
