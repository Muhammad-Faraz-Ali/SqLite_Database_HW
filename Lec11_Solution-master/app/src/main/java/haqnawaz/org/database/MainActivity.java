package haqnawaz.org.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.sql.Ref;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;

    EditText editName, editAge,id;
    Switch switchIsActive;
    Button buttonAdd, buttonViewAll,buttonUpdate,buttonDelete;

    ListView listViewCustomer;

    ArrayAdapter<CustomerModel> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonViewAll = findViewById(R.id.buttonViewAll);
        buttonUpdate=findViewById(R.id.updateBtn);
        buttonDelete=findViewById(R.id.buttonDelete);

        editName = findViewById(R.id.editTextName);
        editAge = findViewById(R.id.editTextAge);
        id=findViewById(R.id.id);

        switchIsActive = findViewById(R.id.switchCustomer);
        listViewCustomer = findViewById(R.id.listViewCustomer);

        RefreshData();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            CustomerModel customerModel;

            @Override
            public void onClick(View v) {
                try {
                    customerModel = new CustomerModel(editName.getText().toString(), Integer.parseInt(editAge.getText().toString()), switchIsActive.isChecked(), 1);
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                boolean b = dbHelper.addCustomer(customerModel);
                RefreshData();
            }
        });
        buttonUpdate.setOnClickListener(v -> {
            boolean updated = dbHelper.update(id.getText().toString(),editName.getText().toString(),editAge.getText().toString());
            if(updated)
            {
                Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Error in Updating", Toast.LENGTH_SHORT).show();
            }
            RefreshData();

        });
        buttonDelete.setOnClickListener(v -> {
            Integer delete = dbHelper.delete(id.getText().toString());
            if(delete > 0)
            {
                Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Data Not Deleted", Toast.LENGTH_SHORT).show();
            }
            RefreshData();
        });
        
        buttonViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshData();
            }
        });

        // New Event Listener
        listViewCustomer.setOnItemClickListener((parent, view, position, id) ->  {
           CustomerModel customerModel = (CustomerModel) parent.getAdapter().getItem(position);
           // Deleting Customer Model
            dbHelper = new DBHelper(MainActivity.this);
            dbHelper.deleteCustomer(customerModel.getId());
            // Refresh Data
            RefreshData();
        });


    }

    private void RefreshData() {
        dbHelper = new DBHelper(MainActivity.this);
        List<CustomerModel> customerModelList = dbHelper.getAllRecords();
        arrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, customerModelList);
        listViewCustomer.setAdapter(arrayAdapter);
    }
}