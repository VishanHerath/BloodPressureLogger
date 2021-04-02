package lk.kdu.pulze;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import lk.kdu.pulze.helper.DatabaseHelper;
import lk.kdu.pulze.model.PressureModel;

public class StatisticsFragment extends Fragment {
    private ExtendedFloatingActionButton extendedFloatingActionButton;
    private MaterialToolbar toolbar;
    private TextView sys_high, sys_low, dia_high, dia_low, pul_high, pul_low;
    private DatabaseHelper databaseHelper;
    private ArrayList<PressureModel> pressureModelArrayList;
    private Chip all, month, three_month;
    private SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMM yy HH:mm");
    private ArrayList<PressureModel> lastMonthArray = new ArrayList<>();
    private ArrayList<PressureModel> lastThreeMonthArray = new ArrayList<>();
    private PressureModel maxSys, minSys, maxDia, minDia, maxPul, minPul;

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

        all = v.findViewById(R.id.all_chip);
        month = v.findViewById(R.id.last_month_chip);
        three_month = v.findViewById(R.id.last_three_month_chip);


        databaseHelper = new DatabaseHelper(getContext());
        pressureModelArrayList = databaseHelper.getPressure();

        setAllSelected();


        for (PressureModel i : pressureModelArrayList) {
            Date date = null;
            try {
                date = inputFormat.parse(i.getDatetime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date current = new Date();
            if (date != null) {
                if (dateDifference(date, current) > 31 && dateDifference(date, current) <= 62) {
                    this.lastMonthArray.add(i);
                } else if (dateDifference(date, current) <= 93) {
                    this.lastThreeMonthArray.add(i);
                }
            }
        }


        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetValues();
                setAllSelected();
                updateText();
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetValues();
                setMonthSelected();
                updateText();
            }
        });

        three_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetValues();
                setLastThreeMonthsSelected();
                updateText();
            }
        });

        extendedFloatingActionButton = requireActivity().findViewById(R.id.extended_fab);
        toolbar.setTitle("Statistics");
        extendedFloatingActionButton.hide();
        return v;
    }

    private void setAllSelected() {
        if (!pressureModelArrayList.isEmpty()) {
            maxSys = Collections.max(pressureModelArrayList, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getSystolic() > o2.getSystolic())
                        return 1;
                    else if (o1.getSystolic() < o2.getSystolic())
                        return -1;
                    return 0;
                }
            });

            minSys = Collections.min(pressureModelArrayList, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getSystolic() > o2.getSystolic())
                        return 1;
                    else if (o1.getSystolic() < o2.getSystolic())
                        return -1;
                    return 0;
                }
            });

            maxDia = Collections.max(pressureModelArrayList, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getDiastolic() > o2.getDiastolic())
                        return 1;
                    else if (o1.getDiastolic() < o2.getDiastolic())
                        return -1;
                    return 0;
                }
            });

            minDia = Collections.min(pressureModelArrayList, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getDiastolic() > o2.getDiastolic())
                        return 1;
                    else if (o1.getDiastolic() < o2.getDiastolic())
                        return -1;
                    return 0;
                }
            });

            maxPul = Collections.max(pressureModelArrayList, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getPulse() > o2.getPulse())
                        return 1;
                    else if (o1.getPulse() < o2.getPulse())
                        return -1;
                    return 0;
                }
            });

            minPul = Collections.min(pressureModelArrayList, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getPulse() > o2.getPulse())
                        return 1;
                    else if (o1.getPulse() < o2.getPulse())
                        return -1;
                    return 0;
                }
            });
        }
    }

    private void setMonthSelected() {
        if (!lastMonthArray.isEmpty()) {
            maxSys = Collections.max(lastMonthArray, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getSystolic() > o2.getSystolic())
                        return 1;
                    else if (o1.getSystolic() < o2.getSystolic())
                        return -1;
                    return 0;
                }
            });

            minSys = Collections.min(lastMonthArray, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getSystolic() > o2.getSystolic())
                        return 1;
                    else if (o1.getSystolic() < o2.getSystolic())
                        return -1;
                    return 0;
                }
            });

            maxDia = Collections.max(lastMonthArray, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getDiastolic() > o2.getDiastolic())
                        return 1;
                    else if (o1.getDiastolic() < o2.getDiastolic())
                        return -1;
                    return 0;
                }
            });

            minDia = Collections.min(lastMonthArray, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getDiastolic() > o2.getDiastolic())
                        return 1;
                    else if (o1.getDiastolic() < o2.getDiastolic())
                        return -1;
                    return 0;
                }
            });

            maxPul = Collections.max(lastMonthArray, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getPulse() > o2.getPulse())
                        return 1;
                    else if (o1.getPulse() < o2.getPulse())
                        return -1;
                    return 0;
                }
            });

            minPul = Collections.min(lastMonthArray, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getPulse() > o2.getPulse())
                        return 1;
                    else if (o1.getPulse() < o2.getPulse())
                        return -1;
                    return 0;
                }
            });
        }
    }

    private void setLastThreeMonthsSelected() {
        if (!lastThreeMonthArray.isEmpty()) {
            maxSys = Collections.max(lastThreeMonthArray, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getSystolic() > o2.getSystolic())
                        return 1;
                    else if (o1.getSystolic() < o2.getSystolic())
                        return -1;
                    return 0;
                }
            });

            minSys = Collections.min(lastThreeMonthArray, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getSystolic() > o2.getSystolic())
                        return 1;
                    else if (o1.getSystolic() < o2.getSystolic())
                        return -1;
                    return 0;
                }
            });

            maxDia = Collections.max(lastThreeMonthArray, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getDiastolic() > o2.getDiastolic())
                        return 1;
                    else if (o1.getDiastolic() < o2.getDiastolic())
                        return -1;
                    return 0;
                }
            });

            minDia = Collections.min(lastThreeMonthArray, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getDiastolic() > o2.getDiastolic())
                        return 1;
                    else if (o1.getDiastolic() < o2.getDiastolic())
                        return -1;
                    return 0;
                }
            });

            maxPul = Collections.max(lastThreeMonthArray, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getPulse() > o2.getPulse())
                        return 1;
                    else if (o1.getPulse() < o2.getPulse())
                        return -1;
                    return 0;
                }
            });

            minPul = Collections.min(lastThreeMonthArray, new Comparator<PressureModel>() {
                @Override
                public int compare(PressureModel o1, PressureModel o2) {
                    if (o1.getPulse() > o2.getPulse())
                        return 1;
                    else if (o1.getPulse() < o2.getPulse())
                        return -1;
                    return 0;
                }
            });
        }
    }

    private void updateText() {
        if (maxSys != null && minSys != null && maxDia != null && minDia != null && maxPul != null && minPul != null) {
            sys_high.setText(String.valueOf(maxSys.getSystolic()));
            sys_low.setText(String.valueOf(minSys.getSystolic()));
            dia_high.setText(String.valueOf(maxDia.getDiastolic()));
            dia_low.setText(String.valueOf(minDia.getDiastolic()));
            pul_high.setText(String.valueOf(maxPul.getPulse()));
            pul_low.setText(String.valueOf(minPul.getPulse()));
        }
    }

    @SuppressLint("SetTextI18n")
    private void resetValues() {
        maxSys = null;
        maxDia = null;
        maxPul = null;
        minSys = null;
        minDia = null;
        minPul = null;
        sys_high.setText("-");
        sys_low.setText("-");
        dia_high.setText("-");
        dia_low.setText("-");
        pul_high.setText("-");
        pul_low.setText("-");
    }

    public long dateDifference(Date startDate, Date endDate) {
        long difference = startDate.getTime() - endDate.getTime();
        long daysBetween = (difference / (1000 * 60 * 60 * 24));
        return Math.abs(daysBetween);
    }

}
