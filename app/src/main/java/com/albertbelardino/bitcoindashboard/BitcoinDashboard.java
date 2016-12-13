package com.albertbelardino.bitcoindashboard;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicReference;

public class BitcoinDashboard extends AppCompatActivity
        implements PriceAtAddressMasterFragment.OnFragmentInteractionListener{
    public String[] FINAL_nav_arr;
    public FragmentManager fm;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitcoin_dashboard);

        FINAL_nav_arr = getResources().getStringArray(R.array.FINAL_fragment_navigator_array);

        setSupportActionBar((Toolbar)findViewById(R.id.bitcoin_action_bar));

        fm = getFragmentManager();

        intent = new Intent(BitcoinDashboard.this, BitcoinDashboardResults.class);

        fm
                .beginTransaction()
                .add(R.id.dashFrameLayout, new CurrentPriceFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bitcoin_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), BitcoinDashboardResults.class);
                intent.putExtra(getResources().getString(R.string.extra_key), query);

                if(menu.findItem(R.id.block_info).isChecked()){
                    intent.putExtra(getResources().getString(R.string.frag_key), getResources().getString(R.string.block_info_key));
                    startActivity(intent);
                }

                else if(menu.findItem(R.id.price_at_address).isChecked()){
                    ((PriceAtAddressMasterFragment)fm.findFragmentById(R.id.dashFrameLayout))
                                    .addToAdapter(query);
                    intent.putExtra(getResources().getString(R.string.frag_key), getResources().getString(R.string.price_at_address_key));
                    startActivity(intent);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.current_price:
                fm
                        .beginTransaction()
                        .replace(R.id.dashFrameLayout, new CurrentPriceFragment())
                        .commit();
                /*
                intent.putExtra(getResources().getString(R.string.frag_key), getString(R.string.current_price_key));
                startActivity(intent);*/
                break;
            case R.id.price_chart:
                intent.putExtra(getResources().getString(R.string.frag_key), getString(R.string.price_chart_key));
                startActivity(intent);
                break;
            case R.id.block_info:
                item.setChecked(true);
                if(fm.findFragmentById(R.id.dashFrameLayout)!= null) {
                    fm
                            .beginTransaction()
                            .remove(fm.findFragmentById(R.id.dashFrameLayout))
                            .commit();
                }
                break;
            case R.id.price_at_address:
                item.setChecked(true);
                fm
                        .beginTransaction()
                        .replace(R.id.dashFrameLayout, new PriceAtAddressMasterFragment())
                        .commit();
                break;
        }

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
