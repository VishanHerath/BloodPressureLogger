package lk.kdu.pulze;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

public class    BottomSheetDialog extends BottomSheetDialogFragment {
    private Button date_picker;
    private TextInputEditText systole,diastole;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout,
                container, false);
        date_picker = v.findViewById(R.id.date_picker);
        systole = v.findViewById(R.id.systole);
        diastole = v.findViewById(R.id.diastole);

        // Register the text view and the button with
        // their appropriate IDs

        // now create instance of the material date picker
        // builder make sure to add the "dateRangePicker"
        // which is material date range picker which is the
        // second type of the date picker in material design
        // date picker we need to pass the pair of Long
        // Long, because the start date and end date is
        // store as "Long" type value
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // handle select date button which opens the
                // material design date picker
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");


                // now handle the positive button click from the
                // material design date picker
                materialDatePicker.addOnPositiveButtonClickListener(
                        new MaterialPickerOnPositiveButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick(Object selection) {

                                // if the user clicks on the positive
                                // button that is ok button update the
                                // selected date
                                date_picker.setText(materialDatePicker.getHeaderText());
                                // in the above statement, getHeaderText
                                // will return selected date preview from the
                                // dialog
                            }
                        });
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
}
