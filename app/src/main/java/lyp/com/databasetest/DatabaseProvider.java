package lyp.com.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class DatabaseProvider extends ContentProvider {

    public static final int BOOK_DIR=0;
    public static final int BOOK_ITEM=1;
    public static final int CATEGORY_DIR=2;
    public static final int CATEGORY_ITEM=3;

    public static final String AUTHORITY="lyp.com.databasetest.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;

    static {
        // 创建 UriMatcher 实例
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        // 调用 addURI() 方法，此方法接收3个参数：authority、path、自定义代码
        uriMatcher.addURI(AUTHORITY,"book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#",BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category",CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY,"category/#",CATEGORY_ITEM);
    }

    /**
    *  初始化内容提供器
     * */
    @Override
    public boolean onCreate() {
        // 创建 MyDatabaseHelper 实例
        dbHelper= new MyDatabaseHelper(getContext(),"BookStore.db",null,2);
        return true;
    }

    /**
     * 查询数据*/
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            //Book表的所有数据
            case BOOK_DIR:
                cursor=db.query("Book",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            //Book表的单条数据
            case BOOK_ITEM:
                String bookId=uri.getPathSegments().get(1);
                cursor=db.query("Book",projection,"id=?",new String[]{bookId},null,null,sortOrder);
                break;
            case CATEGORY_DIR:
                cursor=db.query("Category",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                cursor=db.query("Cateory",projection,"id=?",new String[]{categoryId},null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    /**
     *添加数据
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Uri uriReturn=null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId=db.insert("Book",null,values);
                uriReturn=Uri.parse("content://"+AUTHORITY+"/book/"+newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId=db.insert("Category",null,values);
                uriReturn=Uri.parse("content://"+AUTHORITY+"/category/"+newCategoryId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    /**
     * 更新数据
     * */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int updateRows=0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows=db.update("Book",values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId=uri.getPathSegments().get(1);
                updateRows=db.update("Book",values,"id=?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows=db.update("Category",values,selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                updateRows=db.update("Category",values,"id=?",new String[]{categoryId});
                break;
            default:
                break;
        }
        return updateRows;
    }

    /**
     * 删除数据
     * */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int deleteRows=0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRows=db.delete("Book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId=uri.getPathSegments().get(1);
                deleteRows=db.delete("Book","id=?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRows=db.delete("Book",selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                deleteRows=db.delete("Category","id=?",new String[]{categoryId});
                break;
            default:
                break;
        }
        return deleteRows;
    }


    /**
     * 获取 Uri 对象所对应的 MIME(描述消息内容类型的因特网标准)类型
     */
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.lyp.com.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.lyp.com.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.lyp.com.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.lyp.com.databasetest.provider.category";
        }
        return null;
    }

}
