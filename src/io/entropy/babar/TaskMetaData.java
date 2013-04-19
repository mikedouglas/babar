package io.entropy.babar;

import android.net.Uri;
import android.provider.BaseColumns;

public class TaskMetaData {
    private TaskMetaData() {}

    public static final String AUTHORITY = "io.entropy.babar.provider.Tasks";
    public static final Uri CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY + "/tasks"
    );

    public static final String DATABASE_NAME = "tasks.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CONTENT_TYPE_TASKS_LIST = "vnd.android.cursor.dir/vnd.entropy.tasks";
    public static final String CONTENT_TYPE_TASK_ONE = "vnd.android.cursor.item/vnd.entropy.tasks";

    public class TaskTable implements BaseColumns {
        private TaskTable() { }

        public static final String TABLE_NAME = "tbl_tasks";

        public static final String DESCRIPTION = "description";
        public static final String DUE_DATE = "due_date";
    }
}
