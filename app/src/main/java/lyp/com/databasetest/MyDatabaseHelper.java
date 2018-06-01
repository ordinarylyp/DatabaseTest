package lyp.com.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    // Book表的建表语句
    public static final String CREATE_BOOK="create table Book ("
            +"id integer primary key autoincrement,"
            +"author text,"
            +"price real,"
            +"pages integer,"
            +"name text)";
    //Category表的建表语句
    public static final String GREATE_CATEGORY="create table Category ("
            +"id integer primary key autoincrement,"
            +"category_name text,"
            +"category_code integer)";

    private Context mContext;
   /**
   * 构造方法
    * @param context
    * @param name 数据库名
    * @param factory  允许在查询数据库时返回一个自定义的Cursor(光标)，一般为null
    * @param version  当前数据库的版本
    * */
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    /**
     *创建数据库
     * @param db */

    @Override
    public void onCreate(SQLiteDatabase db) {
        //执行建表语句
        db.execSQL(CREATE_BOOK);
        db.execSQL(GREATE_CATEGORY);
        Toast.makeText(mContext,"创建成功",Toast.LENGTH_SHORT).show();

    }

    /**
     * 升级数据库
     * @param db
     * @param oldVersion
     * @param newVersion */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        onCreate(db);

    }
}
