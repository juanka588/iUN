package com.unal.iun.GUI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.unal.iun.R;

import java.util.ArrayList;

/**
 * Created by JuanCamilo on 8/02/2016.
 */
public class DirectoryFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    public DirectoryFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DirectoryFragment newInstance(String sectionNumber) {
        DirectoryFragment fragment = new DirectoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_directory, container, false);
        ListView list = (ListView) rootView.findViewById(R.id.directoryList);

        ArrayList<String> arr = new ArrayList<>();
        arr.add("Test 1");
        arr.add("Test 2");
        arr.add("Test 3");
        int layout = android.R.layout.simple_list_item_activated_1;
        list.setAdapter(new ArrayAdapter<String>(rootView.getContext(), layout, arr));
        Bundle b = getArguments();
        ((Button) rootView.findViewById(R.id.testB)).setText(b.getString(ARG_SECTION_NUMBER));
        return rootView;
    }


}
