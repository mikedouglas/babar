package io.entropy.babar;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

public class CreateTaskDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Add Task");
        return inflater.inflate(R.layout.create_task, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getView().findViewById(R.id.cancel_dialog_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        getView().findViewById(R.id.add_task_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText description = (EditText) getView().findViewById(R.id.task_description);
                DatePicker dueDatePicker = (DatePicker) getView().findViewById(R.id.task_due_date);

                ContentValues contentValues = new ContentValues();
                contentValues.put(TaskMetaData.TaskTable.DESCRIPTION, description.getText().toString());
                contentValues.put(TaskMetaData.TaskTable.DUE_DATE,
                        dueDatePicker.getYear() + "/" + dueDatePicker.getMonth() + "/" + dueDatePicker.getDayOfMonth());

                getActivity().getContentResolver().insert(
                        TaskMetaData.CONTENT_URI,
                        contentValues);

                dismiss();
            }
        });
    }
}
