package lk.kdu.pulze;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class GetDetailsActivity extends AppCompatActivity {

    private Button mPickDateButton;
    private AutoCompleteTextView gender_list;
    private TextView mShowSelectedDateText;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_get_details);

        // now register the text view and the button with
        // their appropriate IDs
        mPickDateButton = findViewById(R.id.pick_date_btn);
        mShowSelectedDateText = findViewById(R.id.pick_date_btn);

        // now create instance of the material date picker
        // builder make sure to add the "datePicker" which
        // is normal material date picker which is the first
        // type of the date picker in material design date
        // picker
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        // handle select date button which opens the
        // material design date picker
        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        mShowSelectedDateText.setText( materialDatePicker.getHeaderText());
                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });

        TextInputEditText name = findViewById(R.id.outlinedEditTextField);
        TextInputLayout namelayout = findViewById(R.id.outlinedTextField);
        AutoCompleteTextView gender=findViewById(R.id.gender_list);
        Button getDate =findViewById(R.id.pick_date_btn);

        Button btn = findViewById(R.id.startBtn);

        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(this).edit();

        SharedPreferences.Editor sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(name.getText().length()==0){
                        namelayout.requestFocus();
                        namelayout.setError("Please enter your name");
                    }
                    if(getDate.getText().equals("Select Birthday")){
                        getDate.setError("Please enter your birthday");
                    }
                    else{
                        namelayout.setError(null);
                        getDate.setError(null);
                        String n  = name.getText().toString();
                        String g  = gender.getText().toString();
                        String d  = getDate.getText().toString();

                        preferences.putString("Name", n);
                        preferences.putString("Gender", g);
                        preferences.putString("Birthday", d);
                        preferences.apply();
                    }
                }
                catch(Exception ex){
                    ex.toString();

                }
                finally {
                    startActivity(new Intent(GetDetailsActivity.this, MainActivity.class));
                    sharedPreferencesEditor.putBoolean("flag", true);
                    sharedPreferencesEditor.apply();
                }
            }
        });

        //gender items
        createList();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void createList(){

        gender_list = findViewById(R.id.gender_list);

        String[] gender_option = {"Male", "Female"};

        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.gender_list_item, gender_option);
        gender_list.setText(adapter.getItem(0).toString(),false);
        gender_list.setAdapter(adapter);
    }
}