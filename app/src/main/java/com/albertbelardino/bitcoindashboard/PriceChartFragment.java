package com.albertbelardino.bitcoindashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class PriceChartFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private WebView priceChartWebView;
    private String[] vals = {"1d", "5d", "7d", "15d", "30d"};
    private Spinner spinner;
    private PriceChartAdapter adapter;
    private int curPosition;

    public PriceChartFragment() {
        // Required empty public constructor
    }

    public static PriceChartFragment newInstance() {
        PriceChartFragment fragment = new PriceChartFragment();
        //TODO -- add to bundle
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_price_chart, container, false);

        spinner = (Spinner)v.findViewById(R.id.priceChartSpinner);
        adapter = new PriceChartAdapter(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                vals);

        spinner.setAdapter(adapter);

        priceChartWebView = (WebView)v.findViewById(R.id.priceChartWebView);


        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            priceChartWebView.loadUrl("https://chart.yahoo.com/z?s=BTCUSD=X&t=" +
                                    spinner.getSelectedItem().toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        };
                            Thread t = new Thread(r);
                            t.start();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
