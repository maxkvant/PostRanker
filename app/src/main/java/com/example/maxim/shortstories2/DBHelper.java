package com.example.maxim.shortstories2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.maxim.shortstories2.post.Comment;
import com.example.maxim.shortstories2.post.Post;
import com.example.maxim.shortstories2.util.SharedPrefs;
import com.example.maxim.shortstories2.walls.WallFactory;
import com.example.maxim.shortstories2.walls.WallMode;
import com.example.maxim.shortstories2.walls.Wall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.maxim.shortstories2.util.Strings.FIRST_RUN;
import static com.example.maxim.shortstories2.walls.WallMode.BY_DATE;
import static com.example.maxim.shortstories2.walls.WallMode.COMMENTED;
import static java.lang.StrictMath.max;
import static java.lang.StrictMath.nextAfter;

public class DBHelper extends SQLiteOpenHelper {
    private final static String COMA_STEP = ",";

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "ShortStoriesDB";

    private static final String DELETE_TABLE =
            "drop table if exists ";
    private final static int POSTS_PER_GET = 20;
    private final static int POSTS_PER_GET_TOP = 200;
    private final static int POSTS_LIMIT = 4000;

    private final static String wallPath = "com.example.maxim.shortstories2.walls.";
    private final static String INT_TYPE = " integer";
    private final static String TEXT_TYPE = " text";
    private final static String PRIMARY_KEY = " primary key";
    private final static String REAL_TYPE = " real";

    public DBHelper() {
        super(MyApplication.getInstance(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SharedPrefs.removeKey(MyApplication.getInstance(), FIRST_RUN);
        db.execSQL(DELETE_TABLE + Post.PostsEntry.TABLE_NAME);
        db.execSQL(DELETE_TABLE + Comment.CommentsEntry.TABLE_NAME);
        db.execSQL(DELETE_TABLE + Wall.WallsEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tablePosts = "create table " + Post.PostsEntry.TABLE_NAME + "(" +
                Post.PostsEntry._ID + INT_TYPE + COMA_STEP +
                Post.PostsEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMA_STEP +
                Post.PostsEntry.COLUMN_NAME_WALL_ID + INT_TYPE + COMA_STEP +
                Post.PostsEntry.COLUMN_NAME_DATE + INT_TYPE + COMA_STEP +
                Post.PostsEntry.COLUMN_NAME_RATING + REAL_TYPE + COMA_STEP +
                PRIMARY_KEY + "(" +
                    Post.PostsEntry._ID + COMA_STEP +
                    Post.PostsEntry.COLUMN_NAME_WALL_ID + ")" +
                ")";
        db.execSQL(tablePosts);

        String tableComments = "create table " + Comment.CommentsEntry.TABLE_NAME + "(" +
                Comment.CommentsEntry.COLUMN_NAME_ID + INT_TYPE + PRIMARY_KEY + COMA_STEP +
                Comment.CommentsEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMA_STEP +
                Comment.CommentsEntry.COLUMN_NAME_POST_ID + INT_TYPE + ")";
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

    public void insertPosts(List<Post> posts) {
        Log.d("insertPosts", "" + posts.size());
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        for (Post post : posts) {
            ContentValues values = new ContentValues();
            values.put(Post.PostsEntry._ID, post.id);
            values.put(Post.PostsEntry.COLUMN_NAME_TEXT, post.text);
            values.put(Post.PostsEntry.COLUMN_NAME_WALL_ID, post.wall_id);
            values.put(Post.PostsEntry.COLUMN_NAME_DATE, post.date);
            values.put(Post.PostsEntry.COLUMN_NAME_RATING, post.rating);
            db.insertWithOnConflict(Post.PostsEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private Cursor getPosts(WallMode mode, String filter) {
        SQLiteDatabase db = this.getReadableDatabase();

        String [] projection = {
            Post.PostsEntry.TABLE_NAME + "." + Post.PostsEntry._ID,
            Post.PostsEntry.TABLE_NAME + "." + Post.PostsEntry.COLUMN_NAME_TEXT,
            Post.PostsEntry.TABLE_NAME + "." + Post.PostsEntry.COLUMN_NAME_WALL_ID,
            Wall.WallsEntry.TABLE_NAME + "." + Wall.WallsEntry.COLUMN_NAME_NAME,
            Post.PostsEntry.TABLE_NAME + "." + Post.PostsEntry.COLUMN_NAME_DATE,
            Post.PostsEntry.TABLE_NAME + "." + Post.PostsEntry.COLUMN_NAME_RATING,
            Wall.WallsEntry.TABLE_NAME + "." + Wall.WallsEntry.COLUMN_NAME_CLASS,
        };

        String table = Post.PostsEntry.TABLE_NAME + " inner join " + Wall.WallsEntry.TABLE_NAME +
                " on " +
                Post.PostsEntry.TABLE_NAME + "." + Post.PostsEntry.COLUMN_NAME_WALL_ID
                + " = " + Wall.WallsEntry.TABLE_NAME + "." + Wall.WallsEntry.COLUMN_NAME_ID;

        if (mode == COMMENTED) {
            table += " inner join " + Comment.CommentsEntry.TABLE_NAME +
                    " on " +
                    Post.PostsEntry.TABLE_NAME + "." + Post.PostsEntry._ID
                    + " = " + Comment.CommentsEntry.TABLE_NAME + "." + Comment.CommentsEntry.COLUMN_NAME_POST_ID;
        }

        return db.query(table,
                projection,
                "1" + filter + getModeFilter(mode),
                null,
                null,
                null,
                getModeOrdering(mode),
                POSTS_LIMIT + "");
    }

    public Post toPost(Cursor cursor) {
        return new Post(
                cursor.getLong(cursor.getColumnIndex(Post.PostsEntry._ID)),
                cursor.getString(cursor.getColumnIndex(Post.PostsEntry.COLUMN_NAME_TEXT)),
                cursor.getLong(cursor.getColumnIndex(Post.PostsEntry.COLUMN_NAME_WALL_ID)),
                cursor.getString(cursor.getColumnIndex(Wall.WallsEntry.COLUMN_NAME_NAME)),
                cursor.getInt(cursor.getColumnIndex(Post.PostsEntry.COLUMN_NAME_DATE)),
                cursor.getDouble(cursor.getColumnIndex(Post.PostsEntry.COLUMN_NAME_RATING)),
                cursor.getString(cursor.getColumnIndex(Wall.WallsEntry.COLUMN_NAME_CLASS)));
    }

    public Cursor getPosts(WallMode mode) {
        return getPosts(mode, "");
    }

    public Cursor getPosts(WallMode mode, long id) {
        return getPosts(mode, " and " + Post.PostsEntry.COLUMN_NAME_WALL_ID + " = " + id + " ");
    }

    public void insertComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Comment.CommentsEntry.COLUMN_NAME_ID, comment.id);
        values.put(Comment.CommentsEntry.COLUMN_NAME_TEXT, comment.text);
        values.put(Comment.CommentsEntry.COLUMN_NAME_POST_ID, comment.post_id);
        db.insertWithOnConflict(Comment.CommentsEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public List<Wall> getAllWalls() {
        List<Wall> res = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Wall.WallsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Wall.WallsEntry.COLUMN_NAME_PRIORITY + COMA_STEP + Wall.WallsEntry.COLUMN_NAME_NAME
        );


        if (cursor.moveToFirst()) {
            do {
                String className = cursor.getString(cursor.getColumnIndex(Wall.WallsEntry.COLUMN_NAME_CLASS));
                String name = cursor.getString(cursor.getColumnIndex(Wall.WallsEntry.COLUMN_NAME_NAME));
                double ratio = cursor.getDouble(cursor.getColumnIndex(Wall.WallsEntry.COLUMN_NAME_RATIO));
                long id = cursor.getLong(cursor.getColumnIndex(Wall.WallsEntry.COLUMN_NAME_ID));
                long updated = cursor.getLong(cursor.getColumnIndex(Wall.WallsEntry.COLUMN_NAME_UPDATED));
                try {
                    String classLongName = wallPath + className;
                    WallFactory factoryWall = (WallFactory) Class.forName(classLongName)
                            .getConstructor()
                            .newInstance();
                    res.add(factoryWall.create(name, id, ratio, updated));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return res;
    }

    public void insertWall(Wall wall) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Wall.WallsEntry.COLUMN_NAME_ID, wall.getId());
        values.put(Wall.WallsEntry.COLUMN_NAME_NAME, wall.toString());
        values.put(Wall.WallsEntry.COLUMN_NAME_CLASS, wall.getFactoryClassName());
        values.put(Wall.WallsEntry.COLUMN_NAME_PRIORITY, 3);
        values.put(Wall.WallsEntry.COLUMN_NAME_RATIO, wall.getRatio());
        values.put(Wall.WallsEntry.COLUMN_NAME_UPDATED, wall.getUpdated());
        Log.d("updated", String.valueOf(new Date(wall.getUpdated() * 1000L)));
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
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Comment.CommentsEntry.TABLE_NAME,
                null,
                Comment.CommentsEntry.COLUMN_NAME_POST_ID + " = " + post_id,
                null,
                null,
                null,
                null,
                null);
        List<Comment> res = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(Comment.CommentsEntry.COLUMN_NAME_TEXT));
                String text = cursor.getString(cursor.getColumnIndex(Comment.CommentsEntry.COLUMN_NAME_TEXT));
                res.add(new Comment(id, text, post_id));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return res;
    }

    private String getModeFilter(WallMode mode) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String prefix = " and " + Post.PostsEntry.COLUMN_NAME_DATE + " > ";
        switch (mode) {
            case TOP_DAILY:
                cal.add(Calendar.DAY_OF_YEAR, -1);
                return prefix + (cal.getTimeInMillis() / 1000);
            case TOP_WEEKLY:
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                return prefix + (cal.getTimeInMillis() / 1000);
            case TOP_MONTHLY:
                cal.add(Calendar.MONTH, -1);
                return prefix + (cal.getTimeInMillis() / 1000);
            default:
                return " and 1";
        }
    }

    private String getModeOrdering(WallMode mode) {
        if (mode == BY_DATE || mode == COMMENTED) {
            return " date desc ";
        } else {
            return " rating desc ";
        }
    }

}

