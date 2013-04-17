package io.entropy.babar;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;

public class TaskView extends FrameLayout {
    public TaskView(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(90, 90));
        setBackgroundColor(Color.RED);
    }
}
