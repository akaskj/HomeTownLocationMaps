package sdsu.com.hometownlocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "EmployeeData";

    //Table Name
    private static final String TABLE_NAME = "Employee";

//Table Columns names

    private static final String EMP_NAME = "emp_name";
    private static final String EMP_AGE = "emp_age";
    private static final String EMP_NUMBER = "emp_number";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Override onCreate() method – must do action
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                EMP_NAME + " TEXT, " + EMP_AGE + " INTEGER, " + EMP_NUMBER + " INTEGER" + ")";
        db.execSQL(createTableQuery);
    }

    //Override onUpgrade()
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Commands related to upgrade the table contents ()
        db.execSQL("DROP TABLE IF EXITS " + TABLE_NAME);
    }
// Now all other CURD (Create, Update, Read, Delete)operations are below

    // Add a new Employee Details
    public void addEmployeeDetails(EmployeeDetails employeeDetails) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EMP_NAME, employeeDetails.getEmpName());
        values.put(EMP_AGE, employeeDetails.getEmpAge());
        values.put(EMP_NUMBER, employeeDetails.getEmpNumber());

        db.insert(TABLE_NAME, null, values);
        db.close();

    }

//get the details of a single employee

    public EmployeeDetails getSingleEmployeeDetails(int empNum) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{EMP_NAME, EMP_AGE, EMP_NUMBER}, EMP_NUMBER + "=?",
                new String[]{String.valueOf(empNum)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        EmployeeDetails employeeDetails = new EmployeeDetails(cursor.getString(0), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)));
        return employeeDetails;
    }

//get details of all employees

    public List<EmployeeDetails> getAllEmployeeDetails() {
        List<EmployeeDetails> employeeDetailsList = new ArrayList<EmployeeDetails>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

// Iterate the loop till cursor reaches the end
        if (cursor.moveToFirst()) { // methods returns false if cursor is empty
            do {
                EmployeeDetails employeeDetails = new EmployeeDetails();
                employeeDetails.setEmpName(cursor.getString(0));
                employeeDetails.setEmpAge(Integer.parseInt(cursor.getString(1)));
                employeeDetails.setEmpNumber(Integer.parseInt(cursor.getString(2)));

                employeeDetailsList.add(employeeDetails);
            } while (cursor.moveToNext());
        }
        return employeeDetailsList;
    }

    // Updating single employee details
    public int updateEmployeeDetails(EmployeeDetails employeeDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMP_NAME, employeeDetails.getEmpName());
        values.put(EMP_AGE, employeeDetails.getEmpAge());

// update query
        return db.update(TABLE_NAME, values, EMP_NUMBER + " = ?",
                new String[]{String.valueOf(employeeDetails.getEmpNumber())});
    }

    //Deleting Employee Details
    public void deleteEmployeeDetails(EmployeeDetails employeeDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, EMP_NUMBER + " = ?",
                new String[]{String.valueOf(employeeDetails.getEmpNumber())});
        db.close();
    }

    public int getEmployeeCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
//cursor.close();
        return cursor.getCount();
    }

}
