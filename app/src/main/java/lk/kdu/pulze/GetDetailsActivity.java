package lk.kdu.pulze;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GetDetailsActivity extends AppCompatActivity {

    private Button mPickDateButton;
    private AutoCompleteTextView gender_list;
    private TextInputEditText name;
    private TextInputLayout nameLayout;
    private AutoCompleteTextView gender;
    private Button startBtn;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_get_details);
        mPickDateButton = findViewById(R.id.pick_date_btn);
        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDateDialog();
                    }
                });


        name = findViewById(R.id.outlinedEditTextField);
        nameLayout = findViewById(R.id.outlinedTextField);
        gender = findViewById(R.id.gender_list);
        startBtn = findViewById(R.id.startBtn);

        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(this).edit();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validateData()) {
                        nameLayout.setError(null);
                        mPickDateButton.setError(null);
                        String n = name.getText().toString();
                        String g = gender.getText().toString();
                        String d = mPickDateButton.getText().toString();

                        preferences.putString("name", n);
                        preferences.putString("gender", g);
                        preferences.putString("dob", d);

                        //Commit returns a boolean while apply is asynchronous
                        preferences.apply();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (validateData()) {
                        preferences.putBoolean("flag", true);
                        preferences.apply();
                        startActivity(new Intent(GetDetailsActivity.this, MainActivity.class));
                        finish();
                    }

                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(name.getText().length() != 0){
                    nameLayout.setError(null);
                }else{
                    nameLayout.setError("Please enter your name");
                }
            }
        });



        //Gender items
        createList();
    }

    private void showDateDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
                mPickDateButton.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };

        new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void createList() {

        gender_list = findViewById(R.id.gender_list);

        String[] gender_option = {"Male", "Female"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.gender_list_item, gender_option);
        gender_list.setText(adapter.getItem(0).toString(), false);
        gender_list.setAdapter(adapter);
    }

    private boolean validateData() {
        if (name.getText().length() == 0) {
            nameLayout.requestFocus();
            nameLayout.setError("Please enter your name");
            return false;
        }
        if (mPickDateButton.getText().equals("Select Birthday")) {
            mPickDateButton.setError("Please enter your birthday");
            return false;
        }
        return true;
    }
}