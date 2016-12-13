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
 * {@link PriceAtAddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PriceAtAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceAtAddressFragment extends Fragment {

    TextView priceAtAddressTV;
    String btcAddress;

    private OnFragmentInteractionListener mListener;

    public PriceAtAddressFragment() {
        // Required empty public constructor
    }

    public static PriceAtAddressFragment newInstance(String bitAddress) {
        PriceAtAddressFragment fragment = new PriceAtAddressFragment();
        Bundle args = new Bundle();
        args.putString("btcAddress", bitAddress);
        fragment.setArguments(args);
        return fragment;
    }

    Handler priceAtAddressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            JSONObject responseObject = (JSONObject) msg.obj;

            try {
                priceAtAddressTV.setText(Double.toString(responseObject
                        .getJSONObject("data")
                        .getDouble("balance")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            btcAddress = getArguments().getString(getResources().getString(R.string.btcAddress_key));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_price_at_address, container, false);

        priceAtAddressTV = (TextView)v.findViewById(R.id.priceAtAddressTV);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    URL priceAtAddressURL = null;

                    try {
                        priceAtAddressURL = new URL("http://btc.blockr.io/api/v1/address/info/"
                                                             + btcAddress);//TODO
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    priceAtAddressURL.openStream()));

                    String response = "", tmpResponse;

                    tmpResponse = reader.readLine();
                    while (tmpResponse != null) {
                        response = response + tmpResponse;
                        tmpResponse = reader.readLine();
                    }

                    JSONObject priceAtAddressObject = new JSONObject(response);
                    Message msg = Message.obtain();
                    msg.obj = priceAtAddressObject;
                    priceAtAddressHandler.sendMessage(msg);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
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
