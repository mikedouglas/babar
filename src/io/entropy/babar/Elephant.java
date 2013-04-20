package io.entropy.babar;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.WindowManager.LayoutParams;

public class Elephant extends ImageView {
    public static final String TAG = BabarService.class.getName();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    private int _xLast, _yLast;
    private int _xLastLp, _yLastLp;
    private String _task;
    private TextView _taskTitleView;

    private int highlightResId;

    public Elephant(Context context, String task, String dueDate) {
        super(context);
        _task = task;

        setBackgroundResource(R.drawable.circular_background_off);
        setPadding(10, 10, 10, 10);
        setVisibility(View.INVISIBLE);
        mHandler.postDelayed(mChangePath, (int) (50 * Math.random()));

        final WindowManager wm = (WindowManager)context.getSystemService(Service.WINDOW_SERVICE);
        _taskTitleView = new TextView(getContext());
        _taskTitleView.setGravity(Gravity.CENTER);
        _taskTitleView.setText(task);
        _taskTitleView.setTextSize(35);
        _taskTitleView.setVisibility(View.INVISIBLE);
        _taskTitleView.setTextAppearance(getContext(), R.style.TaskOverlay);
        _taskTitleView.setBackgroundColor(Color.argb(180, 0, 0, 0));

        try {
            Date due = dateFormat.parse(dueDate);

            if (System.currentTimeMillis() > due.getTime()) {
                setImageResource(R.drawable.mike_elephant);
                _taskTitleView.setPaintFlags(_taskTitleView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                _taskTitleView.setTextColor(Color.argb(255, 255, 28, 36));
                highlightResId = R.drawable.circular_background_active;
            } else {
                setImageResource(R.drawable.sara_elephant);
                highlightResId = R.drawable.circular_background_early;
            }
        } catch (ParseException e) {
            setImageResource(R.drawable.elephant);
            highlightResId = R.drawable.circular_background_early;
        }

        LayoutParams lp = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                        LayoutParams.TYPE_PRIORITY_PHONE,
                        LayoutParams.FLAG_HARDWARE_ACCELERATED |
                        LayoutParams.FLAG_NOT_FOCUSABLE |
                        LayoutParams.FLAG_NOT_TOUCHABLE |
                        LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                        PixelFormat.TRANSLUCENT);
        wm.addView(_taskTitleView, lp);
        _xLastLp = lp.x;
        _yLastLp = lp.y;

        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();

                LayoutParams lp = (LayoutParams) v.getLayoutParams();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        _xLast = X;
                        _yLast = Y;
                        _taskTitleView.setVisibility(View.VISIBLE);
                        setBackgroundResource(highlightResId);
                        mHandler.removeCallbacks(mFloatElephant);

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
                        setBackgroundResource(R.drawable.circular_background_off);
                        _taskTitleView.setVisibility(View.GONE);
                        mFloatElephant.run();
                        break;
                }
                return false;
            }
        });
    }

    LayoutParams createLayoutParams() {
        int size = highlightResId == R.drawable.circular_background_active ? 300 : 150;
        return new LayoutParams(
                size, size,
                LayoutParams.TYPE_PRIORITY_PHONE,
                LayoutParams.FLAG_HARDWARE_ACCELERATED |
                        LayoutParams.FLAG_NOT_FOCUSABLE |
                        LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
    }

    public void start() {
        mFloatElephant.run();
    }

    public void stop() {
        mHandler.removeCallbacks(mFloatElephant);
        mHandler.removeCallbacks(mChangePath);
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

    private Handler mHandler = new Handler();
    private int mDirection[] = {1,1};

    private Runnable mChangePath = new Runnable() {
        public void run() {
            double random = Math.random();
            if (random > 0.5) {
                mDirection[0] = -1;
            } else {
                mDirection[0] = 1;
            }

            random = Math.random();
            if (random > 0.5) {
                mDirection[1] = -1;
            } else {
                mDirection[1] = 1;
            }

            mHandler.postDelayed(mChangePath, (int)(3000 * Math.random() + 3000));
        }
    };

    private static final int MOVEMENT = 4;

    private Runnable mFloatElephant = new Runnable() {
        public void run() {
            LayoutParams lp = (LayoutParams) getLayoutParams();
            _xLastLp = lp.x = _xLastLp + (int)((Math.random() * MOVEMENT) - MOVEMENT/2) + (MOVEMENT/2 * mDirection[0]);
            _yLastLp = lp.y = _yLastLp + (int)((Math.random() * MOVEMENT) - MOVEMENT/2) + (MOVEMENT/2 * mDirection[1]);

            int maxX = R.drawable.circular_background_active == highlightResId ? 240 : 300;
            if (_xLastLp > maxX) {
                mDirection[0] = -1;
            } else if (_xLastLp < -maxX) {
                mDirection[0] = 1;
            }

            int minY = R.drawable.circular_background_active == highlightResId ? -390 : -465;
            int maxY = R.drawable.circular_background_active == highlightResId ? 450 : 523;
            if (_yLastLp > maxY) {
                mDirection[1] = -1;
            } else if (_yLastLp < minY) {
                mDirection[1] = 1;
            }

            WindowManager wm = (WindowManager) getContext().getSystemService(Service.WINDOW_SERVICE);
            wm.updateViewLayout(Elephant.this, lp);
            mHandler.postDelayed(mFloatElephant, 60);
        }
    };
}
