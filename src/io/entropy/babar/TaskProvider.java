package io.entropy.babar;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class TaskProvider extends ContentProvider {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

    private static final UriMatcher sUriMatcher;
    private static final int TASK_TYPE_LIST = 1;
    private static final int TASK_TYPE_ONE = 2;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(TaskMetaData.AUTHORITY, "tasks", TASK_TYPE_LIST);
        sUriMatcher.addURI(TaskMetaData.AUTHORITY, "tasks/#", TASK_TYPE_ONE);
    }

    private static final HashMap<String, String> sTasksProjectionMap;
    static {
        sTasksProjectionMap = new HashMap<String, String>();
        sTasksProjectionMap.put(TaskMetaData.TaskTable._ID, TaskMetaData.TaskTable._ID);
        sTasksProjectionMap.put(TaskMetaData.TaskTable.DESCRIPTION, TaskMetaData.TaskTable.DESCRIPTION);
        sTasksProjectionMap.put(TaskMetaData.TaskTable.DUE_DATE, TaskMetaData.TaskTable.DUE_DATE);
    }

    private TaskDBHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new TaskDBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case TASK_TYPE_LIST:
                builder.setTables(TaskMetaData.TaskTable.TABLE_NAME);
                builder.setProjectionMap(sTasksProjectionMap);
                break;

            case TASK_TYPE_ONE:
                builder.setTables(TaskMetaData.TaskTable.TABLE_NAME);
                builder.setProjectionMap(sTasksProjectionMap);
                builder.appendWhere(TaskMetaData.TaskTable._ID + " = " +
                        uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor queryCursor = builder.query(db, projection, selection, selectionArgs, null, null, null);
        queryCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return queryCursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case TASK_TYPE_LIST:
                return TaskMetaData.CONTENT_TYPE_TASKS_LIST;

            case TASK_TYPE_ONE:
                return TaskMetaData.CONTENT_TYPE_TASK_ONE;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (sUriMatcher.match(uri) != TASK_TYPE_LIST) {
            throw new IllegalArgumentException("[Insert](01)Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long rowId = db.insert(TaskMetaData.TaskTable.TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri taskUri = ContentUris.withAppendedId(TaskMetaData.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(taskUri, null);
            return taskUri;
        }
        throw new IllegalArgumentException("[Insert](02)Unknown URI: " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case TASK_TYPE_LIST:
                count = db.delete(TaskMetaData.TaskTable.TABLE_NAME, selection, selectionArgs);
                break;

            case TASK_TYPE_ONE:
                String rowId = uri.getPathSegments().get(1);
                count = db.delete(TaskMetaData.TaskTable.TABLE_NAME,
                        TaskMetaData.TaskTable._ID + " = " + rowId +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")": ""),
                        selectionArgs
                );
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = 0;

        switch (sUriMatcher.match(uri)) {
            case TASK_TYPE_LIST:
                count = db.update(TaskMetaData.TaskTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            case TASK_TYPE_ONE:
                String rowId = uri.getPathSegments().get(1);
                count = db.update(TaskMetaData.TaskTable.TABLE_NAME, values,
                        TaskMetaData.TaskTable._ID + " = " + rowId +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs);

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return count;
    }

    public static class TaskDBHelper extends SQLiteOpenHelper {
        public TaskDBHelper(Context context) {
            super(context, TaskMetaData.DATABASE_NAME, null, TaskMetaData.DATABASE_VERSION);
        }

        private static final String SQL_QUERY_CREATE =
                "CREATE TABLE " + TaskMetaData.TaskTable.TABLE_NAME + " (" +
                        TaskMetaData.TaskTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TaskMetaData.TaskTable.DESCRIPTION + " TEXT NOT NULL, " +
                        TaskMetaData.TaskTable.DUE_DATE + " TEXT NOT NULL" +
                        ")";

        private static final String SQL_QUERY_DROP =
                "DROP TABLE IF EXISTS " + TaskMetaData.TaskTable.TABLE_NAME + ";";

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_QUERY_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_QUERY_DROP);
            onCreate(db);
        }
    }
}
