package com.albertbelardino.bitcoindashboard;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BitcoinDashboardResults extends AppCompatActivity
                                    implements PriceChartFragment.OnFragmentInteractionListener,
                                        PriceAtAddressFragment.OnFragmentInteractionListener{

    public FragmentManager fm;
    public String query;
    public Integer queryNumber = 0;
    Button prevButton, nextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitcoin_dashboard_results);

        handleIntent(getIntent());
        fm = getFragmentManager();

        if (getIntent().getExtras().getString(getResources().getString(R.string.frag_key)).matches(getString(R.string.current_price_key))) {
            fm
                    .beginTransaction()
                    .add(R.id.resultsLayout, new CurrentPriceFragment())
                    .commit();
        }
        else if(getIntent().getExtras().getString(getResources().getString(R.string.frag_key)).matches(getString(R.string.price_chart_key))){
            fm
                    .beginTransaction()
                    .add(R.id.resultsLayout, new PriceChartFragment())
                    .commit();
        }
        else if(getIntent().getExtras().getString(getResources().getString(R.string.frag_key))
                .matches(getResources().getString(R.string.block_info_key))){
            fm
                    .beginTransaction()
                    .add(R.id.resultsLayout, BlockInfoFragment.newInstance(query))
                    .commit();

            queryNumber += Integer.parseInt(query);

            prevButton = (Button)findViewById(R.id.previousBlockButton);
            nextButton = (Button)findViewById(R.id.nextBlockButton);

            prevButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);

            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fm
                            .beginTransaction()
                            .replace(R.id.resultsLayout,
                                    BlockInfoFragment.newInstance((queryNumber++).toString()))
                            .commit();
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fm
                            .beginTransaction()
                            .replace(R.id.resultsLayout,
                                    BlockInfoFragment.newInstance((queryNumber--).toString()))
                            .commit();
                }
            });
        }
        else if(getIntent().getExtras().getString(getResources().getString(R.string.frag_key))
                .matches(getResources().getString(R.string.price_at_address_key))){
                    fm
                            .beginTransaction()
                            .replace(R.id.resultsLayout, PriceAtAddressFragment.newInstance(
                                    getIntent().getExtras().getString(getResources().getString(R.string.extra_key))))
                            .commit();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
            query = intent.getExtras().getString(getResources().getString(R.string.extra_key));
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
