package com.rxjavaexamples;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rxjavaexamples.adapters.LogAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

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

    @OnClick(R.id.btnSimpleTimer)
    public void onSimpleTimerClicked(View view) {
        runTaskWithDelay();
    }

    @OnClick(R.id.btnIntervalTimer)
    public void onIntervalTimerClicked(View view) {
        runTaskInIntervalOfOneSecondForFiveTimes();
    }

    private void initUi() {
        listLogs = new ArrayList<>();
        logAdapter = new LogAdapter(getActivity(), listLogs);
        listView.setAdapter(logAdapter);
    }

    private void runTaskWithDelay() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            return;
        }
        subscription = Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        logData("On Complete method called");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final Long aLong) {
                        logData("in OnNextMethod " + aLong);
                    }
                });
    }

    private void runTaskInIntervalOfOneSecondForFiveTimes() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            return;
        }
        subscription = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(5)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        logData("On Complete method called");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final Long aLong) {
                        logData("in OnNextMethod " + aLong);
                    }
                });
    }

    private void logData(final String value) {
        listLogs.add(value);
        logAdapter.notifyDataSetChanged();
    }
}
