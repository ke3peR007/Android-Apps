package com.example.user.mycrudapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EmployeeAdapter extends ArrayAdapter<Employee> {

    Context mCtx;
    int layoutRes;
    List<Employee> employeeList;
    SQLiteDatabase mDatabase;

    public EmployeeAdapter(Context mCtx, int layoutRes, List<Employee> employeeList, SQLiteDatabase mDatabase) {
        super(mCtx, layoutRes, employeeList);

        this.mCtx = mCtx;
        this.layoutRes = layoutRes;
        this.employeeList = employeeList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(layoutRes,null);

        TextView tvName = view.findViewById(R.id.textViewName);
        TextView tvSalary = view.findViewById(R.id.textViewSalary);

        final Employee employee = employeeList.get(position);

        tvName.setText(employee.getName());
        tvSalary.setText(String.valueOf(employee.getSalary()));

        view.findViewById(R.id.buttonEditEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee(employee);
            }
        });

        view.findViewById(R.id.buttonDeleteEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmployee(employee);
            }
        });


        return view;
    }

    private void updateEmployee(final Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(R.layout.dialog_update_employee,null);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextSalary = view.findViewById(R.id.editTextSalary);

        editTextName.setText(employee.getName());
        editTextSalary.setText(String.valueOf(employee.getSalary()));

        view.findViewById(R.id.buttonUpdateEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editTextName.getText().toString().trim();
                String salary = editTextSalary.getText().toString().trim();

                if(name.isEmpty()) {
                    editTextName.setError("Name can't be empty");
                    editTextName.requestFocus();
                    return;
                }

                if(salary.isEmpty()) {
                    editTextSalary.setError("salary can't be empty");
                    editTextSalary.requestFocus();
                    return;
                }

                String sql = "UPDATE employees SET name = ?, salary = ? WHERE id =?";
                mDatabase.execSQL(sql, new String[]{name, salary, String.valueOf(employee.getId())});

                Toast.makeText(mCtx, "update employee", Toast.LENGTH_SHORT).show();

                loadEmployeesFromDatabaseAgain();
                alertDialog.dismiss();
            }


        });
    }

    private void deleteEmployee(final Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        builder.setTitle("are you sure");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sql = "DELETE FROM employees WHERE id = ?";
                mDatabase.execSQL(sql,new Integer[]{employee.getId()});
                loadEmployeesFromDatabaseAgain();
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void loadEmployeesFromDatabaseAgain() {

        String sql = "SELECT * FROM employees";
        Cursor cursor = mDatabase.rawQuery(sql,null);

        if(cursor.moveToFirst()) {
            employeeList.clear();
            do {
                employeeList.add(new Employee(

                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2)

                ));
            } while(cursor.moveToNext());

            notifyDataSetChanged();
        }
    }
}
