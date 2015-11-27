package com.rxjavaexamples;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);;
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_retro_rxjava)
    void onClickOfRetrofitAndRxjava() {
            setFragment(new TimerFragment());
        String dateStart = "2015-11-23 15:41:33";
        String dateStop = "2015-11-26 15:41:33";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = format.parse(dateStart);
            secondDate = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("Date","date difference " + TimeUnit.DAYS.convert(secondDate.getTime() - firstDate.getTime(), TimeUnit
                .MILLISECONDS));

    }

    private void setFragment(@NonNull Fragment fragment) {
        final String tag = fragment.getClass().toString();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(android.R.id.content,fragment,tag)
                .commit();
    }
}
