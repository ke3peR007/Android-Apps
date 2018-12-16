package com.example.user.mycrudapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {


    SQLiteDatabase mDatabase;

    List<Employee> employeeList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME,MODE_PRIVATE,null);


        employeeList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listViewEmployees);
        loadEmployeesFromDatabase();
    }


    private void loadEmployeesFromDatabase() {
        String sql = "SELECT * FROM employees";
        Cursor cursor = mDatabase.rawQuery(sql,null);

        if(cursor.moveToFirst()) {
            do {
                employeeList.add(new Employee(

                  cursor.getInt(0),
                  cursor.getString(1),
                  cursor.getDouble(2)

                ));
            } while(cursor.moveToNext());

            EmployeeAdapter adapter = new EmployeeAdapter(this,R.layout.list_layout_employees, employeeList, mDatabase);
            listView.setAdapter(adapter);
        }
    }

}
