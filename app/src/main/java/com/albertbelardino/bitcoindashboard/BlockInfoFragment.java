package com.albertbelardino.bitcoindashboard;


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
 */
public class BlockInfoFragment extends Fragment {

    public String blockNumber;
    public JSONObject blockInfoObject;
    public TextView sizeDiffTV, hashTV, merkTV;

    public BlockInfoFragment() {
        // Required empty public constructor
    }

    Handler blockInfoHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            JSONObject responseObject = (JSONObject) msg.obj;

            try {
                sizeDiffTV.setText(responseObject
                                    .getJSONObject("data")
                                    .getString("size")
                                    + ", " +
                                    responseObject
                                     .getJSONObject("data")
                                      .getDouble("difficulty"));
                hashTV.setText(responseObject
                                    .getJSONObject("data")
                                    .getString("hash"));
                merkTV.setText(responseObject
                                    .getJSONObject("data")
                                    .getString("merkleroot"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }
    });

    public static BlockInfoFragment newInstance(String blockNum) {
        BlockInfoFragment fragment = new BlockInfoFragment();
        Bundle args = new Bundle();
        args.putString("blockNum", blockNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            blockNumber = getArguments().getString(getResources().getString(R.string.blockNum_key));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_block_info, container, false);

        sizeDiffTV = (TextView)v.findViewById(R.id.sizeDifficultyTV);
        hashTV = (TextView)v.findViewById(R.id.blockHashTV);
        merkTV = (TextView)v.findViewById(R.id.merkleRootTV);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    URL blockInfoURL = null;

                    try {
                        blockInfoURL = new URL("http://btc.blockr.io/api/v1/block/info/" + blockNumber);//TODO
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    blockInfoURL.openStream()));

                    String response = "", tmpResponse;

                    tmpResponse = reader.readLine();
                    while (tmpResponse != null) {
                        response = response + tmpResponse;
                        tmpResponse = reader.readLine();
                    }

                    blockInfoObject = new JSONObject(response);
                    Message msg = Message.obtain();
                    msg.obj = blockInfoObject;
                    blockInfoHandler.sendMessage(msg);


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
}
