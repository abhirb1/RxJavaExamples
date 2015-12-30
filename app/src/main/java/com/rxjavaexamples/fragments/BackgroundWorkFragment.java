package com.rxjavaexamples.fragments;

import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.rxjavaexamples.R;
import com.rxjavaexamples.adapters.LogAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class BackgroundWorkFragment extends Fragment {


    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.btnLongOperation)
    Button btnLongOperation;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private Subscription subscription;
    private List<String> listLogs;
    private LogAdapter logAdapter;

    public BackgroundWorkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_background_work, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUi();
    }

    @OnClick(R.id.btnLongOperation)
    public void onClickOfStartLongOperation() {
        logData("Button clicked ");
        progressBar.setVisibility(View.VISIBLE);
        addSubscription();
    }

    private void initUi() {
        listLogs = new ArrayList<>();
        logAdapter = new LogAdapter(getActivity(), listLogs);
        listView.setAdapter(logAdapter);
    }

    private void addSubscription() {
        subscription = getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<Boolean> getObservable() {
        return Observable.just(true).map(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean) {
                logData("Within Observable");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Timber.d("Operation was interrupted");
                }
                return aBoolean;
            }
        });
    }

    private Observer<Boolean> getObserver() {
        return new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                logData("onCompleted ");
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                logData("Error in Concurrency");
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                logData("OnNext with return value " + aBoolean);
            }
        };
    }

    private boolean isCurrentlyOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private void logData(final String value) {
        if(isCurrentlyOnMainThread()) {
            listLogs.add(0,value + " [On Main Thread]");
        } else {
            listLogs.add(0,value + " [Not On Main Thread]");
        }

        logAdapter.notifyDataSetChanged();
    }
}
