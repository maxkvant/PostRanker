package com.example.maxim.shortstories2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import java.util.StringTokenizer;

import static android.R.attr.longClickable;
import static android.R.attr.name;
import static android.R.attr.rating;
import static com.example.maxim.shortstories2.walls.WALL_MODE.COMMENTED;
import static java.lang.StrictMath.log;
import static java.lang.StrictMath.max;
import static java.lang.StrictMath.nextAfter;

public class DBHelper extends SQLiteOpenHelper {
    private final static String TABLE_POSTS = "Posts";
    private final static String TABLE_WALLS = "Walls";
    private final static String TABLE_COMMENTS = "Comments";

    private final static int POSTS_PER_GET = 20;
    private final static int POSTS_PER_GET_TOP = 200;
    private final static String DB_NAME = "ShortStoriesDB";
    private final static String wallPath = "com.example.maxim.shortstories2.walls.";

    public DBHelper() {
        super(MyApplication.getInstance(), DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tablePosts = "create table " + TABLE_POSTS + "(" +
                "id integer primary key," +
                "text text," +
                "wall_id integer," +
                "date integer," +
                "load_date integer," +
                "rating real)";
        db.execSQL(tablePosts);

        String tableComments = "create table " + TABLE_COMMENTS + "(" +
                "id integer primary key," +
                "text text," +
                "post_id integer)";
        db.execSQL(tableComments);

        String tableWalls = "create table " + TABLE_WALLS + "(" +
                "id integer primary key," +
                "name text ," +
                "class text," +
                "priority integer," +
                "updated integer," +
                "ratio real)";
        db.execSQL(tableWalls);

        List<String> values = Arrays.asList(MyApplication.getInstance().getBaseContext()
                .getResources().getStringArray(R.array.table_walls_default_items));


        Log.d("DBHelper onCreate", "d");
        String insertInWallsPrefix = "insert into " + TABLE_WALLS + " values ";
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
            values.put("id", post.id);
            values.put("text", post.text);
            values.put("wall_id", post.wall_id);
            values.put("date", post.date);
            values.put("rating", post.rating);
            values.put("load_date", post.load_date);
            db.insertWithOnConflict(TABLE_POSTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public List<Post> getPosts(int offset, WALL_MODE mode, String filter) {
        List<Post> res = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String queryPrefix = "select " +
                    TABLE_POSTS + ".text," +
                    TABLE_POSTS + ".text," +
                    TABLE_POSTS + ".wall_id," +
                    TABLE_WALLS + ".name," +
                    TABLE_POSTS + ".date," +
                    TABLE_POSTS + ".rating" +
                " from " + TABLE_POSTS +
                " inner join " + TABLE_WALLS +
                " on " +
                TABLE_POSTS + ".wall_id = " + TABLE_WALLS + ".id ";

        if (mode == COMMENTED) {
            queryPrefix = queryPrefix +
                    " inner join " + TABLE_COMMENTS +
                    " on " +
                    TABLE_POSTS + ".id = " + TABLE_COMMENTS + ".post_id ";
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
                      " limit " + offset + "," + POSTS_PER_GET + ";";
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
                      " limit " + offset + "," + offset_if_top + ";";
                break;
            case TOP_MONTHLY:
                cal.add(Calendar.MONTH, -1);
                res = " and date > " + (cal.getTimeInMillis() / 1000) +
                      " order by rating desc " +
                      " limit " + offset + "," + offset_if_top + ";";
                break;
            case TOP_ALL:
                res = " order by rating desc" +
                      " limit " + offset + "," + offset_if_top + ";";
                break;
            case COMMENTED:
                res = " order by date desc " +
                      " limit " + offset + "," + offset_if_top + ";";
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
        db.insertWithOnConflict(TABLE_COMMENTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public List<Wall> getAllWalls() {
        List<Wall> res = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_WALLS + " order by priority;";
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
        values.put("id", wall.getId());
        values.put("name", wall.toString());
        values.put("class", wall.getClass().getSimpleName());
        values.put("priority", 3);
        values.put("ratio", wall.getRatio());
        values.put("updated", wall.getUpdated());
        db.insertWithOnConflict(TABLE_WALLS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteWall(long wallId) {
        if (wallId == 0) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WALLS, "id = " + wallId, null);
    }

    public List<Comment> getComments(long post_id) {
        String sql = "select * from " + TABLE_COMMENTS +
                " where post_id = " + post_id + ";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        List<Comment> res = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                String text = cursor.getString(cursor.getColumnIndex("text"));
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
}
