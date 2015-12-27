package com.rxjavaexamples;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_retro_rxjava)
    void onClickOfRetrofitAndRxjava() {
       setFragment(new TimerFragment());


    }

    @OnClick(R.id.btn_debounce_search)
    void onClickOfDebounceSearch() {
        setFragment(new DebounceSearchFragment());
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
