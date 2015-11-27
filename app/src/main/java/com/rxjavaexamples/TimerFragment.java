package com.rxjavaexamples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rxjavaexamples.adapters.LogAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;

public class TimerFragment extends Fragment {

    @Bind(R.id.listView)
    ListView listView;

    private List<String> listLogs;
    private LogAdapter logAdapter;
    private Subscription subscription;
    public TimerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUi();
    }

    private void initUi() {
        listLogs = new ArrayList<>();
        logAdapter = new LogAdapter(getActivity(),listLogs);
        listView.setAdapter(logAdapter);
    }

    private void run_background_task() {

    }
}
