package lk.kdu.pulze;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import lk.kdu.pulze.helper.DatabaseHelper;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    String date;
    private Button dateTimePicker, bottomSheetButton;
    private TextInputLayout systoleLayout, diastoleLayout, pulseLayout;
    private TextInputEditText systole, diastole, pulse, note;
    private CoordinatorLayout bottom_container;
    private LineChart lineChart;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout,
                container, false);

        systoleLayout = v.findViewById(R.id.systole_layout);
        diastoleLayout = v.findViewById(R.id.diastole_layout);
        pulseLayout = v.findViewById(R.id.pulse_layout);

        dateTimePicker = v.findViewById(R.id.date_picker);
        systole = v.findViewById(R.id.systole);
        diastole = v.findViewById(R.id.diastole);
        note = v.findViewById(R.id.note);
        pulse = v.findViewById(R.id.pulse);
//        bottom_container = v.findViewById(R.id.bottom_container);
        bottomSheetButton = v.findViewById(R.id.bottom_sheet_button);

        lineChart = requireActivity().findViewById(R.id.activity_main_lineChart);

        databaseHelper = new DatabaseHelper(getActivity());

        dateTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog();
            }
        });


        /*******************************************************************************************************************/

        bottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    databaseHelper.addPressure(Integer.parseInt(systole.getText().toString()), Integer.parseInt(diastole.getText().toString()), Integer.parseInt(pulse.getText().toString()), date, note.getText().toString());
                    Toast.makeText(getActivity(), "Added Successfully!", Toast.LENGTH_SHORT).show();
                    Intent viewList = new Intent(getActivity(), ViewPressureList.class);
                    startActivity(viewList);
                    getActivity().finish();
                }
            }
        });

        systole.addTextChangedListener(new TextWatcher() {

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
                if (systole.getText().toString().length() != 0) {
                    systoleLayout.setError(null);
                } else {
                    systoleLayout.setError("Systole value is empty!");
                }
            }
        });

        diastole.addTextChangedListener(new TextWatcher() {

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
                if (diastole.getText().length() != 0) {
                    diastoleLayout.setError(null);
                } else {
                    diastoleLayout.setError("Diastole value is empty!");
                }
            }
        });

        pulse.addTextChangedListener(new TextWatcher() {

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
                if (pulse.getText().length() != 0) {
                    pulseLayout.setError(null);
                } else {
                    pulseLayout.setError("Pulse value is empty!");
                }
            }
        });
        return v;
    }

    //Bring the Bottom Sheet above navigation buttons.
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setNavigationBarColor(dialog);
        }

        return dialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setNavigationBarColor(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    // Night mode is not active, we're using the light theme
                    navigationBarDrawable.setColor(Color.WHITE);
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    // Night mode is active, we're using dark theme
                    navigationBarDrawable.setColor(Color.parseColor("#434343"));
                    break;
            }


            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }


    private void showDateTimeDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy HH:mm");
                        date = simpleDateFormat.format(calendar.getTime());
                        dateTimePicker.setText(date);
                    }
                };
                new TimePickerDialog(getContext(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog() {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                dateTimePicker.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(getContext(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }

    private void showDateDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
                dateTimePicker.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean validateFields() {
        if (dateTimePicker.getText().equals("Select Date And Time")) {
            dateTimePicker.setError("Please select date and time");
            dateTimePicker.requestFocus();
            return false;
        }
        if (systole.getText().length() == 0 || Integer.parseInt(String.valueOf(systole.getText())) > 300) {
            systoleLayout.requestFocus();
            systoleLayout.setError("Systole value is invalid!");
            return false;
        }
        if (diastole.getText().length() == 0 || Integer.parseInt(String.valueOf(diastole.getText())) > 300) {
            diastoleLayout.requestFocus();
            diastoleLayout.setError("Diastole value is invalid!");
            return false;
        }
        if (pulse.getText().length() == 0 || Integer.parseInt(String.valueOf(pulse.getText())) > 300) {
            pulseLayout.requestFocus();
            pulseLayout.setError("Pulse value is invalid!");
            return false;
        }
        return true;
    }

}