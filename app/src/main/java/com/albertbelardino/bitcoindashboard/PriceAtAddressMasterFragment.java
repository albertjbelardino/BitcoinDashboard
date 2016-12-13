package com.albertbelardino.bitcoindashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PriceAtAddressMasterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PriceAtAddressMasterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceAtAddressMasterFragment extends Fragment {

    public ArrayList<String> prevAddressArray = new ArrayList<>();
    public Spinner prevAddressSpinner;
    public ArrayAdapter<String> prevAddressAdapter;

    private OnFragmentInteractionListener mListener;

    public PriceAtAddressMasterFragment() {
        // Required empty public constructor
    }

    public static PriceAtAddressMasterFragment newInstance() {
        return new PriceAtAddressMasterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> tempList = Arrays.asList(getResources().getStringArray(R.array.sample_address_array));
        prevAddressArray.addAll(tempList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_price_at_address_master, container, false);

        prevAddressSpinner = (Spinner)v.findViewById(R.id.prevSearchedSpinner);
        prevAddressAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                prevAddressArray);

        prevAddressSpinner.setAdapter(prevAddressAdapter);

        prevAddressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent((BitcoinDashboard)getActivity(), BitcoinDashboardResults.class);
                intent.putExtra(getResources().getString(R.string.extra_key), prevAddressArray.get(position));
                intent.putExtra(getResources().getString(R.string.frag_key), getResources().getString(R.string.price_at_address_key));
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    public void addToAdapter(String s){
        prevAddressAdapter.add(s);
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
