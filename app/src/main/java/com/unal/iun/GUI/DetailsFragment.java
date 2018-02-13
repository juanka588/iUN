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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.unal.iun.Adapters.ExpandableDetailsAdapter;
import com.unal.iun.R;
import com.unal.iun.data.DetailedInformation;
import com.unal.iun.data.InformationElement;

import java.util.ArrayList;

public class DetailsFragment extends Fragment {

    public static final String ARG_TITLE = "title";
    public static final String ARG_DATA = "data";
    public static final String ARG_BACKGROUND = "background";

    protected ArrayList<DetailedInformation> data = new ArrayList<>();
    protected String title;
    protected int background;
    protected ExpandableListView sc;

    private FrameLayout tl;

    public DetailsFragment() {
        data.add(new DetailedInformation(new ArrayList<InformationElement>(), "never show"));
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            data = getArguments().getParcelableArrayList(ARG_DATA);
            background = getArguments().getInt(ARG_BACKGROUND);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        sc = (ExpandableListView) rootView.findViewById(R.id.expandableListDetails);
        tl = (FrameLayout) rootView.findViewById(R.id.content_frame);
        TextView tx = (TextView) rootView.findViewById(R.id.titleDetailsData);

        tx.setText(title == null ? "" : title.trim());
        int id = R.drawable.ciudad_universitaria;
        if (background != 0) {
            id = background;
        }
        Drawable bitmapDrawable = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), id));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tl.setBackground(bitmapDrawable);
        } else {
            tl.setBackgroundDrawable(bitmapDrawable);
        }
        sc.setDividerHeight(2);
        sc.setClickable(true);

        ExpandableDetailsAdapter adapter = new ExpandableDetailsAdapter(data, getActivity());
        adapter.font = Typeface.createFromAsset(rootView.getContext().getAssets(),
                "Helvetica.ttf");
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
