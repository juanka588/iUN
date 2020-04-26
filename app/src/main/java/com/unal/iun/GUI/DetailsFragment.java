package com.unal.iun.GUI;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unal.iun.Adapters.ExpandableDetailsAdapter;
import com.unal.iun.R;
import com.unal.iun.data.DetailedInformation;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {

    public static final String ARG_TITLE = "title";
    public static final String ARG_DATA = "data";
    public static final String ARG_BACKGROUND = "background";

    protected List<DetailedInformation> data;
    protected String title;
    protected int background;
    protected ExpandableListView sc;

    public DetailsFragment() {
        data = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        sc = rootView.findViewById(R.id.expandableListDetails);
        LinearLayout tl = rootView.findViewById(R.id.content_frame);
        TextView tx = rootView.findViewById(R.id.titleDetailsData);
        if (args != null) {
            title = args.getString(ARG_TITLE);
            data = args.getParcelableArrayList(ARG_DATA);
            background = args.getInt(ARG_BACKGROUND);
            if (data == null) {
                return rootView;
            }
        }

        tx.setText(title == null ? "" : title.trim());
        int id = R.drawable.ciudad_universitaria;
        if (background != 0) {
            id = background;
        }
        Drawable bitmapDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), id));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tl.setBackground(bitmapDrawable);
        } else {
            tl.setBackgroundDrawable(bitmapDrawable);
        }
        sc.setDividerHeight(2);
        sc.setClickable(true);

        ExpandableDetailsAdapter adapter = new ExpandableDetailsAdapter(data, getActivity());
        sc.setAdapter(adapter);
        sc.expandGroup(0);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
