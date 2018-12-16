package com.example.user.mycrudapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String DATABASE_NAME = "mydatabase";

    SQLiteDatabase mDatabase;

    EditText name,salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       mDatabase = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
       createTable();

       name = (EditText) findViewById(R.id.etName);
       salary = (EditText) findViewById(R.id.etSalary);

       findViewById(R.id.btnAdd).setOnClickListener(this);
       findViewById(R.id.tvRead).setOnClickListener(this);
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS employees (\n" +
                "    id INTEGER NOT NULL CONSTRAINT employees_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    name varchar(200) NOT NULL,\n" +
                "    salary double NOT NULL\n" +
                ");";

        mDatabase.execSQL(sql);
    }

    private  void addEmployee() {
        String username = name.getText().toString().trim();
        String userSalary = salary.getText().toString().trim();

        if(username.isEmpty()) {
            name.setError("Name can't be empty");
            name.requestFocus();
            return;
        }

        if(userSalary.isEmpty()) {
            salary.setError("salary can't be empty");
            salary.requestFocus();
            return;
        }

        String sql = "INSERT INTO employees(name, salary)" +
                "VALUES (?,?)";

        mDatabase.execSQL(sql, new String[]{username,userSalary});
        Toast.makeText(this, "added successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                addEmployee();

                break;
            case R.id.tvRead:

                startActivity(new Intent(this, EmployeeActivity.class));

                break;


        }
    }
}
