//package com.india.timecapture;
//
//import android.content.ContentProvider;
//import android.content.ContentValues;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.net.Uri;
//
//public class DBContentProvider extends ContentProvider {
//    private DB_Helper dbHelper;
//    private static final String AUTHORITY = "com.india.timecapture.db";
//    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//
//    static {
//        uriMatcher.addURI(AUTHORITY, "datetime_table", 1);
//    }
//
//    @Override
//    public boolean onCreate() {
//        dbHelper = new DB_Helper(getContext());
//        return true;
//    }
//
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = null;
//
//        switch (uriMatcher.match(uri)) {
//            case 1:
//                cursor = db.query(DB_Helper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//
//        cursor.setNotificationUri(getContext().getContentResolver(), uri);
//        return cursor;
//    }
//
//    @Override
//    public String getType(Uri uri) {
//        switch (uriMatcher.match(uri)) {
//            case 1:
//                return "vnd.android.cursor.dir/vnd.com.india.timecapture.datetime_table";
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//    }
//
//    @Override
//    public Uri insert(Uri uri, ContentValues values) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        long rowId = 0;
//
//        switch (uriMatcher.match(uri)) {
//            case 1:
//                rowId = db.insert(DB_Helper.TABLE_NAME, null, values);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//
//        Uri resultUri = Uri.parse("content://" + AUTHORITY + "/" + DB_Helper.TABLE_NAME + "/" + rowId);
//        getContext().getContentResolver().notifyChange(resultUri, null);
//        return resultUri;
//    }
//
//    @Override
//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        int count = 0;
//
//        switch (uriMatcher.match(uri)) {
//            case 1:
//                count = db.delete(DB_Helper.TABLE_NAME, selection, selectionArgs);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//
//        getContext().getContentResolver().notifyChange(uri, null);
//        return count;
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        int count = 0;
//
//        switch (uriMatcher.match(uri)) {
//            case 1:
//                count = db.update(DB_Helper.TABLE_NAME, values, selection, selectionArgs);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//
//        getContext().getContentResolver().notifyChange(uri, null);
//        return count;
//    }
//}