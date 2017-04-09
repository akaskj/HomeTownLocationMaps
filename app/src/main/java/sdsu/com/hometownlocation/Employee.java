package sdsu.com.hometownlocation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.List;

public class Employee extends AppCompatActivity {

    EditText editTextName;
    EditText editTextAge;
    EditText editTextNumber;

    Button btnSave, btnShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        final DbHelper dbhelper = new DbHelper(this);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextNumber = (EditText) findViewById(R.id.editTextEmpNumber);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnShow = (Button) findViewById(R.id.btnShow);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeDetails employeeDetails = new EmployeeDetails();

//                UserDetails userDetails = new UserDetails();
//
//
//                dbhelper.addUser(userDetails);



//                employeeDetails.setEmpName(editTextName.getText().toString());
//                employeeDetails.setEmpAge(Integer.parseInt(editTextAge.getText().toString()));
//                employeeDetails.setEmpNumber(Integer.parseInt(editTextNumber.getText().toString()));
//
//                //databaseHandler.addEmployeeDetails(employeeDetails);
//
//                editTextName.setText("");
//                editTextAge.setText("");
//                editTextNumber.setText("");
            }

        });

/**
 * btnShow will print all the records of the Table
 * In the Log OR Logcat
 */

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Reading", "Reading the saved data");
               // List<EmployeeDetails> employeeDetailsList = databaseHandler.getAllEmployeeDetails();
//                for (EmployeeDetails emp : employeeDetailsList) {
//                    String log = "Name: " + emp.getEmpName() + " ,Age: " + emp.getEmpAge() + " , Number: " + emp.getEmpNumber();
//                    Log.d("Record: ", log);
//                }
            }
        });

    }
}