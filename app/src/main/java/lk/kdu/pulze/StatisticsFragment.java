package lk.kdu.pulze;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import lk.kdu.pulze.adapter.PressureListAdapter;
import lk.kdu.pulze.helper.DatabaseHelper;
import lk.kdu.pulze.model.PressureModel;

public class StatisticsFragment extends Fragment {
    private ExtendedFloatingActionButton extendedFloatingActionButton;
    private MaterialToolbar toolbar;
    private ListView statListView;
    private PressureListAdapter customAdapter;
    private TextView sys_high, sys_low, dia_high, dia_low, pul_high, pul_low, filter, filterSize;
    private DatabaseHelper databaseHelper;
    private ArrayList<PressureModel> pressureModelArrayList;
    private Chip all, month, three_month;
    private SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMM yy HH:mm");
    private ArrayList<PressureModel> lastMonthArray = new ArrayList<>();
    private ArrayList<PressureModel> lastThreeMonthArray = new ArrayList<>();
    private PressureModel maxSys, minSys, maxDia, minDia, maxPul, minPul;

    @SuppressLint("ClickableViewAccessibility")
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

        filter = v.findViewById(R.id.filter_text);
        filterSize = v.findViewById(R.id.filter_size);

        statListView = v.findViewById(R.id.statListView);


        all = v.findViewById(R.id.all_chip);
        month = v.findViewById(R.id.last_month_chip);
        three_month = v.findViewById(R.id.last_three_month_chip);


        databaseHelper = new DatabaseHelper(getContext());
        pressureModelArrayList = databaseHelper.getPressure();
        Date current = new Date();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate thisMonth = LocalDate.now();
            LocalDate earlier = LocalDate.now().minusMonths(3);
            three_month.setText(earlier.getMonth() + " - " + thisMonth.getMonth());
        }


        for (PressureModel i : pressureModelArrayList) {
            Date date = null;
            try {
                date = inputFormat.parse(i.getDatetime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date != null) {
                if (dateDifference(date, current) <= 30) {
                    this.lastMonthArray.add(i);
                }
                if (dateDifference(date, current) <= 90) {
                    this.lastThreeMonthArray.add(i);
                }
            }
        }

        setAllSelected();
        updateText();

        //By Default Display All records
        customAdapter = new PressureListAdapter(getContext(), pressureModelArrayList);
        statListView.setAdapter(customAdapter);
        filterSize.setText(pressureModelArrayList.size() + " Records");


        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetValues();
                setAllSelected();
                updateText();
                customAdapter = new PressureListAdapter(getContext(), pressureModelArrayList);
                statListView.setAdapter(customAdapter);
                filter.setText("All");
                filterSize.setText(pressureModelArrayList.size() + " Records");
                refreshListView();
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetValues();
                setMonthSelected();
                updateText();
                customAdapter = new PressureListAdapter(getContext(), lastMonthArray);
                statListView.setAdapter(customAdapter);
                filter.setText("Last 30 Days");
                filterSize.setText(lastMonthArray.size() + " Records");
                refreshListView();
            }
        });

        three_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetValues();
                setLastThreeMonthsSelected();
                updateText();
                customAdapter = new PressureListAdapter(getContext(), lastThreeMonthArray);
                statListView.setAdapter(customAdapter);
                filter.setText("Last 90 Days");
                filterSize.setText(lastThreeMonthArray.size() + " Records");
                refreshListView();
            }
        });

        statListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
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

    private void refreshListView() {
        customAdapter.notifyDataSetChanged();
        statListView.invalidateViews();
        statListView.refreshDrawableState();
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
