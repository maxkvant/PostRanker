package com.example.maxim.shortstories2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.maxim.shortstories2.post.Comment;
import com.example.maxim.shortstories2.post.Post;
import com.example.maxim.shortstories2.walls.WALL_MODE;
import com.example.maxim.shortstories2.walls.Wall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import static android.R.attr.longClickable;
import static android.R.attr.name;
import static android.R.attr.rating;
import static com.example.maxim.shortstories2.walls.WALL_MODE.COMMENTED;
import static java.lang.StrictMath.log;
import static java.lang.StrictMath.max;
import static java.lang.StrictMath.nextAfter;

public class DBHelper extends SQLiteOpenHelper {
    private final static int POSTS_PER_GET = 20;
    private final static int POSTS_PER_GET_TOP = 200;
    private final static String DB_NAME = "ShortStoriesDB";
    private final static String wallPath = "com.example.maxim.shortstories2.walls.";
    private final static String INT_TYPE = " integer";
    private final static String TEXT_TYPE = " text";
    private final static String PRIMARY_KEY = " primary key";
    private final static String REAL_TYPE = " real";
    private final static String COMA_STEP = ",";

    public DBHelper() {
        super(MyApplication.getInstance(), DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tablePosts = "create table " + Post.PostsEntry.TABLE_NAME + "(" +
                Post.PostsEntry.COLUMN_NAME_ID + INT_TYPE + PRIMARY_KEY + COMA_STEP +
                Post.PostsEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMA_STEP +
                Post.PostsEntry.COLUMN_NAME_WALL_ID + INT_TYPE + COMA_STEP +
                Post.PostsEntry.COLUMN_NAME_DATE + INT_TYPE + COMA_STEP +
                Post.PostsEntry.COLUMN_NAME_LOAD_DATE + INT_TYPE + COMA_STEP +
                Post.PostsEntry.COLUMN_NAME_RATING + REAL_TYPE + ")";
        db.execSQL(tablePosts);

        String tableComments = "create table " + CommentsEntry.TABLE_NAME + "(" +
                CommentsEntry.COLUMN_NAME_ID + INT_TYPE + PRIMARY_KEY + COMA_STEP +
                CommentsEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMA_STEP +
                CommentsEntry.COLUMN_NAME_POST_ID + INT_TYPE + ")";
        db.execSQL(tableComments);

        String tableWalls = "create table " + Wall.WallsEntry.TABLE_NAME + "(" +
                Wall.WallsEntry.COLUMN_NAME_ID + INT_TYPE + PRIMARY_KEY + COMA_STEP +
                Wall.WallsEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMA_STEP +
                Wall.WallsEntry.COLUMN_NAME_CLASS + TEXT_TYPE + COMA_STEP +
                Wall.WallsEntry.COLUMN_NAME_PRIORITY + INT_TYPE + COMA_STEP +
                Wall.WallsEntry.COLUMN_NAME_UPDATED + INT_TYPE + COMA_STEP +
                Wall.WallsEntry.COLUMN_NAME_RATIO + REAL_TYPE + ")";
        db.execSQL(tableWalls);

        List<String> values = Arrays.asList(MyApplication.getInstance().getBaseContext()
                .getResources().getStringArray(R.array.table_walls_default_items));


        Log.d("DBHelper onCreate", "d");
        String insertInWallsPrefix = "insert into " + Wall.WallsEntry.TABLE_NAME + " values ";
        for (String value : values) {
            String sql = insertInWallsPrefix + value + ";";
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insertPosts(List<Post> posts) {
        Log.d("insertPosts", "" + posts.size());
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        for (Post post : posts) {
            ContentValues values = new ContentValues();
            values.put(Post.PostsEntry.COLUMN_NAME_ID, post.id);
            values.put(Post.PostsEntry.COLUMN_NAME_TEXT, post.text);
            values.put(Post.PostsEntry.COLUMN_NAME_WALL_ID, post.wall_id);
            values.put(Post.PostsEntry.COLUMN_NAME_DATE, post.date);
            values.put(Post.PostsEntry.COLUMN_NAME_RATING, post.rating);
            values.put(Post.PostsEntry.COLUMN_NAME_LOAD_DATE, post.load_date);
            db.insertWithOnConflict(Post.PostsEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public List<Post> getPosts(int offset, WALL_MODE mode, String filter) {
        List<Post> res = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String queryPrefix = "select " +
                    Post.PostsEntry.TABLE_NAME + ".text," +
                    Post.PostsEntry.TABLE_NAME + ".text," +
                    Post.PostsEntry.TABLE_NAME + ".wall_id," +
                    Wall.WallsEntry.TABLE_NAME + ".name," +
                    Post.PostsEntry.TABLE_NAME + ".date," +
                    Post.PostsEntry.TABLE_NAME + ".rating" +
                " from " + Post.PostsEntry.TABLE_NAME +
                " inner join " + Wall.WallsEntry.TABLE_NAME +
                " on " +
                Post.PostsEntry.TABLE_NAME + ".wall_id = " + Wall.WallsEntry.TABLE_NAME + ".id ";

        if (mode == COMMENTED) {
            queryPrefix = queryPrefix +
                    " inner join " + CommentsEntry.TABLE_NAME +
                    " on " +
                    Post.PostsEntry.TABLE_NAME + ".id = " + CommentsEntry.TABLE_NAME + ".post_id ";
        }

        queryPrefix = queryPrefix + " where 1";

        String querySuffix = getModeSql(offset, mode);

        String sql = queryPrefix + filter + querySuffix;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Post post = new Post(
                        cursor.getString(cursor.getColumnIndex("text")),
                        cursor.getLong(cursor.getColumnIndex("wall_id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getInt(cursor.getColumnIndex("date")),
                        cursor.getDouble(cursor.getColumnIndex("rating"))
                );
                res.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return res;
    }

    private String getModeSql(int offset, WALL_MODE mode) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        String res = "";
        int offset_if_top = (offset == 0 ? POSTS_PER_GET_TOP : 0);
        switch (mode) {
            case BY_DATE:
                res = " order by date desc " +
                      " limit " + offset + COMA_STEP + POSTS_PER_GET + ";";
                break;
            case TOP_DAILY:
                cal.add(Calendar.DAY_OF_YEAR,-1);
                res = " and date > " + (cal.getTimeInMillis() / 1000) +
                        " order by rating desc ";
                break;
            case TOP_WEEKLY:
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                res = " and date > " + (cal.getTimeInMillis() / 1000) +
                      " order by rating desc " +
                      " limit " + offset + COMA_STEP + offset_if_top + ";";
                break;
            case TOP_MONTHLY:
                cal.add(Calendar.MONTH, -1);
                res = " and date > " + (cal.getTimeInMillis() / 1000) +
                      " order by rating desc " +
                      " limit " + offset + COMA_STEP + offset_if_top + ";";
                break;
            case TOP_ALL:
                res = " order by rating desc" +
                      " limit " + offset + COMA_STEP + offset_if_top + ";";
                break;
            case COMMENTED:
                res = " order by date desc " +
                      " limit " + offset + COMA_STEP + offset_if_top + ";";
                break;
        }
        return res;
    }

    public void insertComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", comment.id);
        values.put("text", comment.text);
        values.put("post_id", comment.post_id);
        db.insertWithOnConflict(CommentsEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public List<Wall> getAllWalls() {
        List<Wall> res = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + Wall.WallsEntry.TABLE_NAME + " order by priority;";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                String className = cursor.getString(cursor.getColumnIndex("class"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                double ratio = cursor.getDouble(cursor.getColumnIndex("ratio"));
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                long updated = cursor.getLong(cursor.getColumnIndex("updated"));
                try {
                    res.add((Wall)Class.forName(wallPath + className)
                            .getConstructor(String.class, long.class, double.class, long.class)
                            .newInstance(name, id, ratio, updated)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return res;
    }

    public void insertWall(Wall wall) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Wall.WallsEntry.COLUMN_NAME_ID, wall.getId());
        values.put(Wall.WallsEntry.COLUMN_NAME_NAME, wall.toString());
        values.put(Wall.WallsEntry.COLUMN_NAME_CLASS, wall.getClass().getSimpleName());
        values.put(Wall.WallsEntry.COLUMN_NAME_PRIORITY, 3);
        values.put(Wall.WallsEntry.COLUMN_NAME_RATIO, wall.getRatio());
        values.put(Wall.WallsEntry.COLUMN_NAME_UPDATED, wall.getUpdated());
        db.insertWithOnConflict(Wall.WallsEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteWall(long wallId) {
        if (wallId == 0) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Wall.WallsEntry.TABLE_NAME, Wall.WallsEntry.COLUMN_NAME_ID + " = " + wallId, null);
    }

    public List<Comment> getComments(long post_id) {
        String sql = "select * from " + CommentsEntry.TABLE_NAME +
                " where post_id = " + post_id + ";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        List<Comment> res = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(CommentsEntry.COLUMN_NAME_TEXT));
                String text = cursor.getString(cursor.getColumnIndex(CommentsEntry.COLUMN_NAME_TEXT));
                try {
                    res.add(new Comment(id, text, post_id));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return res;
    }
    
    public final static class CommentsEntry implements BaseColumns {
        final public static String TABLE_NAME = "comments";
        final public static String COLUMN_NAME_ID = "id";
        final public static String COLUMN_NAME_TEXT = "text";
        final public static String COLUMN_NAME_POST_ID = "post_id";
    }
}

