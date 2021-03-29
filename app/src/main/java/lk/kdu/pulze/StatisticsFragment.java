package lk.kdu.pulze;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class StatisticsFragment extends Fragment {
    private AutoCompleteTextView filterList;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);

        filterList = v.findViewById(R.id.filterData);

        String[] filter = {"This Week", "This Month", "Older"};

        final ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, filter);
        filterList.setText(adapter.getItem(0).toString(), false);
        filterList.setAdapter(adapter);


        return v;
    }

}
