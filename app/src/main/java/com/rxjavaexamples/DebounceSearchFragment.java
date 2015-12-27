package com.rxjavaexamples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.rxjavaexamples.adapters.LogAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class DebounceSearchFragment extends Fragment {

    @Bind(R.id.listView)
    ListView listView;

    @Bind(R.id.editTextSearch)
    EditText editTextSearch;

    private List<String> listLogs;
    private LogAdapter logAdapter;
    private Subscription subscription;

    public DebounceSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
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
        logAdapter = new LogAdapter(getActivity(), listLogs);
        listView.setAdapter(logAdapter);
        addSubscription();
    }

    private void addSubscription() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            return;
        }
        subscription = RxTextView.textChangeEvents(editTextSearch).
                debounce(400,TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSearchObserver());
    }

    private Observer<TextViewTextChangeEvent> getSearchObserver() {
        return new Observer<TextViewTextChangeEvent>() {
            @Override
            public void onCompleted() {
                logData("on Complete");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                logData(textViewTextChangeEvent.text().toString());
            }
        };
    }

    private void logData(final String value) {
        listLogs.add(0,value);
        logAdapter.notifyDataSetChanged();
    }
}
