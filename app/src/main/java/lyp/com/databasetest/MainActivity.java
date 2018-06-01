package lyp.com.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 构建MyDatabaseHelper对象，指定数据库名为"BookStore.db、版本号为1
//        databaseHelper=new MyDatabaseHelper(this,"BookStore.db",null,1);
        //指定数据库版本号为2
        databaseHelper=new MyDatabaseHelper(this,"BookStore.db",null,2);
        //创建数据库
        Button createDatabase= findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建或打开一个现有的数据库（已存在则打开，否则创建一个新的）
                databaseHelper.getWritableDatabase();
            }
        });
        //添加数据
        Button addData=findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                //开始组装第一粗数据
                values.put("name","The Da Vinci Code");
                values.put("author","Dan Brown");
                values.put("pages",454);
                values.put("price",16.96);
                db.insert("book",null,values);//插入第一条数据
                values.clear();
                //第二条数据
                values.put("name","The Lost Symbol");
                values.put("author","Dan Brown");
                values.put("pages",510);
                values.put("price",19.95);
                db.insert("book",null,values);//插入第二条数据
            }
        });
        //更新数据
        Button updateData=findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=databaseHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                //将 The Da Vinci Code这本书的价格更新为10.66
                values.put("price",10.99);
                db.update("Book",values,"name=?",new String[]{"The Da Vinci Code"});
            }
        });
        //删除数据
        Button deleteData=findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=databaseHelper.getWritableDatabase();
                //删除页数大于500的数据
                db.delete("Book","pages>?",new String[]{"500"});
            }
        });
        //查询数据
        Button queryData=findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=databaseHelper.getWritableDatabase();
                //查询Book表中的所有数据
                Cursor cursor=db.query("Book",null,null,null,null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        //遍历
                        String name=cursor.getString(cursor.getColumnIndex("name"));
                        String author=cursor.getString(cursor.getColumnIndex("author"));
                        int pages=cursor.getInt(cursor.getColumnIndex("pages"));
                        double price=cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("MainActivity","book name is "+name);
                        Log.d("MainActivity","book author is "+author);
                        Log.d("MainActivity","book pages is "+pages);
                        Log.d("MainActivity","book price is "+price);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
    }
}
