package lk.kdu.pulze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import lk.kdu.pulze.helper.DatabaseHelper;
import lk.kdu.pulze.model.PressureModel;

public class StatisticsFragment extends Fragment {
    ExtendedFloatingActionButton extendedFloatingActionButton;
    private MaterialToolbar toolbar;
    TextView sys_high, sys_low, dia_high, dia_low, pul_high, pul_low;
    private DatabaseHelper databaseHelper;
    private ArrayList<PressureModel> pressureModelArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);
        toolbar = requireActivity().findViewById(R.id.topAppBar);
        sys_high = v.findViewById(R.id.systole_high);
        sys_low = v.findViewById(R.id.systole_low);
        dia_high = v.findViewById(R.id.diastole_high);
        dia_low = v.findViewById(R.id.diastole_low);
        pul_high = v.findViewById(R.id.pulse_high);
        pul_low = v.findViewById(R.id.pulse_low);

        databaseHelper = new DatabaseHelper(getContext());
        pressureModelArrayList = databaseHelper.getPressure();

        PressureModel maxSys = Collections.max(pressureModelArrayList, new Comparator<PressureModel>() {
            @Override
            public int compare(PressureModel o1, PressureModel o2) {
                if (o1.getSystolic() > o2.getSystolic())
                    return 1;
                else if (o1.getSystolic() < o2.getSystolic())
                    return -1;
                return 0;
            }
        });

        PressureModel minSys = Collections.max(pressureModelArrayList, new Comparator<PressureModel>() {
            @Override
            public int compare(PressureModel o1, PressureModel o2) {
                if (o1.getSystolic() < o2.getSystolic())
                    return 1;
                else if (o1.getSystolic() > o2.getSystolic())
                    return -1;
                return 0;
            }
        });

        PressureModel maxDia = Collections.max(pressureModelArrayList, new Comparator<PressureModel>() {
            @Override
            public int compare(PressureModel o1, PressureModel o2) {
                if (o1.getDiastolic() > o2.getDiastolic())
                    return 1;
                else if (o1.getSystolic() < o2.getSystolic())
                    return -1;
                return 0;
            }
        });

        PressureModel minDia = Collections.max(pressureModelArrayList, new Comparator<PressureModel>() {
            @Override
            public int compare(PressureModel o1, PressureModel o2) {
                if (o1.getDiastolic() < o2.getDiastolic())
                    return 1;
                else if (o1.getDiastolic() > o2.getDiastolic())
                    return -1;
                return 0;
            }
        });

        PressureModel maxPul = Collections.max(pressureModelArrayList, new Comparator<PressureModel>() {
            @Override
            public int compare(PressureModel o1, PressureModel o2) {
                if (o1.getPulse() > o2.getPulse())
                    return 1;
                else if (o1.getPulse() < o2.getPulse())
                    return -1;
                return 0;
            }
        });

        PressureModel minPul = Collections.max(pressureModelArrayList, new Comparator<PressureModel>() {
            @Override
            public int compare(PressureModel o1, PressureModel o2) {
                if (o1.getPulse() < o2.getPulse())
                    return 1;
                else if (o1.getPulse() > o2.getPulse())
                    return -1;
                return 0;
            }
        });


        sys_high.setText(String.valueOf(maxSys.getSystolic()));
        sys_low.setText(String.valueOf(minSys.getSystolic()));
        dia_high.setText(String.valueOf(maxDia.getDiastolic()));
        dia_low.setText(String.valueOf(minDia.getDiastolic()));
        pul_high.setText(String.valueOf(maxPul.getPulse()));
        pul_low.setText(String.valueOf(minPul.getPulse()));


        extendedFloatingActionButton = requireActivity().findViewById(R.id.extended_fab);
        toolbar.setTitle("Statistics");
        extendedFloatingActionButton.hide();
        return v;
    }


}
