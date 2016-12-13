package com.albertbelardino.bitcoindashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentPriceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentPriceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentPriceFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public TextView currentPriceTV;

    public CurrentPriceFragment() {
        // Required empty public constructor
    }
    public static CurrentPriceFragment newInstance() {
        return new CurrentPriceFragment();
    }

    Handler currentPriceHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            JSONObject responseObject = (JSONObject) msg.obj;

            try {
                 currentPriceTV.setText( "$ " + (responseObject
                                    .getJSONObject("bpi")
                                    .getJSONObject("USD")
                                    .getDouble("rate")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }
    });



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_current_price, container, false);

        currentPriceTV = (TextView)v.findViewById(R.id.currentPrice_tv);

        //wait 15 seconds to do something
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (boolean val = true; val; ) {
                    URL currentPriceURL;

                    try {
                        currentPriceURL = new URL("http://api.coindesk.com/v1/bpi/currentprice.json");

                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(
                                        currentPriceURL.openStream()));

                        String response = "", tmpResponse;

                        tmpResponse = reader.readLine();
                        while (tmpResponse != null) {
                            response = response + tmpResponse;
                            tmpResponse = reader.readLine();
                        }

                        JSONObject bitcoinPriceObject;

                        try {
                            bitcoinPriceObject = new JSONObject(response);
                            Message msg = Message.obtain();
                            msg.obj = bitcoinPriceObject;
                            currentPriceHandler.sendMessage(msg);
                            bitcoinPriceObject = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread t = new Thread(r);
        t.start();

        return v;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
