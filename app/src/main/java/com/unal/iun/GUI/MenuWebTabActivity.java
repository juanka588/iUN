package com.unal.iun.GUI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unal.iun.Adapters.WebMenuRecyclerViewAdapter;
import com.unal.iun.DataSource.DirectoryContract;
import com.unal.iun.LN.LinnaeusDatabase;
import com.unal.iun.LN.Util;
import com.unal.iun.R;
import com.unal.iun.data.WebItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuWebTabActivity extends AppCompatActivity {

    private static String GENERAL_INTEREST = "0";
    private static String UN_COMMUNITY = "1";
    private static String COMMUNITY_SERVICES = "2";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private SearchView sv;
    private double[] lat;
    private double[] lon;
    private String[] titulos;
    private String[] descripciones;

    public static List<WebItem> initData(Context context, String filter) {
        LinnaeusDatabase ln = new LinnaeusDatabase(context);
        SQLiteDatabase db = ln.dataBase;
        List<WebItem> items = new ArrayList<>();
        Cursor cursor = db.query(DirectoryContract.EnlacesProvider.TABLE_NAME,
                DirectoryContract.EnlacesProvider.COLUMN_NAMES,
                DirectoryContract.EnlacesProvider.COLUMN_ICON + " IS NOT NULL AND " +
                        DirectoryContract.EnlacesProvider.COLUMN_COMMUNITY_SERVICE + "=?",
                new String[]{filter},
                null,
                null,
                DirectoryContract.EnlacesProvider.COLUMN_ORDER);
        String[][] mat = Util.imprimirLista(cursor);
        for (int i = 0; i < mat.length; i++) {
            String cad = mat[i][1] + "";
            if (mat[i][1].contains(".")) {
                try {
                    cad = cad.substring(0, cad.length() - 4);
                    Util.log("imagen", cad);
                } catch (Exception e) {
                    Util.log("error data base", e.toString());
                }
            }
            int icon = context.getResources().getIdentifier("drawable/" + cad, null,
                    context.getPackageName());
            items.add(new WebItem(mat[i][0], icon, mat[i][2], false));
        }
        cursor.close();
        Util.log("Cambio", filter);
        db.close();
        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_web_tab);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                home();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void navegar(String cad) {
        Util.irA(cad, this);
    }

    @Override
    public void onBackPressed() {
        home();
        super.onBackPressed();
    }

    public void home(View v) {
        home();
    }

    public void home() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu_web_tab, menu);
        sv = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_busqueda));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                String cad = "http://unal.edu.co/resultados-de-la-busqueda/?q="
                        + arg0;
                navegar(cad);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_menu_web_tab, container, false);
            RecyclerView list = rootView.findViewById(R.id.list);
            TextView title = rootView.findViewById(R.id.fragment_title);
            int spaces = 3;
            //Util.log("spaces ", screenWidth + " " + density + " " + spaces);
            int selected = getArguments().getInt(ARG_SECTION_NUMBER);
            String filter = "";
            String fragmentTitle = "";
            switch (selected) {
                case 1:
                    filter = GENERAL_INTEREST;
                    fragmentTitle = rootView.getContext().getString(R.string.general_interest);
                    break;
                case 2:
                    filter = UN_COMMUNITY;
                    fragmentTitle = rootView.getContext().getString(R.string.community_un);
                    break;
                case 3:
                    filter = COMMUNITY_SERVICES;
                    fragmentTitle = rootView.getContext().getString(R.string.community_services);
                    break;
            }
            title.setText(fragmentTitle);
            Typeface font = Typeface
                    .createFromAsset(rootView.getContext().getAssets(), "Helvetica.ttf");
            WebMenuRecyclerViewAdapter adapter = new WebMenuRecyclerViewAdapter(
                    initData(rootView.getContext(), filter),
                    getActivity(), font);
            GridLayoutManager gridLayoutManager =
                    new GridLayoutManager(rootView.getContext(), spaces);
            list.setLayoutManager(gridLayoutManager);
            list.setAdapter(adapter);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.general_interest).toUpperCase(l);
                case 1:
                    return getString(R.string.community_un).toUpperCase(l);
                case 2:
                    return getString(R.string.community_services).toUpperCase(l);
            }
            return null;
        }
    }
}

