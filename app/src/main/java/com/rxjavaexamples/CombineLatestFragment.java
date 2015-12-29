package com.rxjavaexamples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func3;
import static android.text.TextUtils.isEmpty;
import static android.util.Patterns.EMAIL_ADDRESS;

public class CombineLatestFragment extends Fragment {

    @Bind(R.id.btn_demo_form_valid)
    TextView textValidIndicator;
    @Bind(R.id.demo_combl_email)
    EditText editTextEmail;
    @Bind(R.id.demo_combl_password)
    EditText editTextPassword;
    @Bind(R.id.demo_combl_num)
    EditText editTextNumber;

    private Observable<CharSequence> emailChangeObservable;
    private Observable<CharSequence> passwordChangeObservable;
    private Observable<CharSequence> numberChangeObservable;

    private Subscription subscription;

    public CombineLatestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_combine_latest, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUi();
    }

    private void initUi() {
        emailChangeObservable = RxTextView.textChanges(editTextEmail).skip(1);
        passwordChangeObservable = RxTextView.textChanges(editTextPassword).skip(1);
        numberChangeObservable = RxTextView.textChanges(editTextNumber).skip(1);

        combineLatestEvents();
    }

    private void combineLatestEvents() {
        subscription = Observable.combineLatest(emailChangeObservable, passwordChangeObservable,
                numberChangeObservable, new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence newEmail,CharSequence newPassword,CharSequence newNumber) {
                        boolean emailValid = !isEmpty(newEmail) &&
                                EMAIL_ADDRESS.matcher(newEmail).matches();
                        if (!emailValid) {
                            editTextEmail.setError("Invalid Email!");
                        }

                        boolean passValid = !isEmpty(newPassword) && newPassword.length() > 8;
                        if (!passValid) {
                            editTextPassword.setError("Invalid Password!");
                        }

                        boolean numValid = !isEmpty(newNumber);
                        if (numValid) {
                            int num = Integer.parseInt(newNumber.toString());
                            numValid = num > 0 && num <= 100;
                        }
                        if (!numValid) {
                            editTextNumber.setError("Invalid Number!");
                        }

                        return emailValid && passValid && numValid;
                    }
                })
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean isFormValid) {
                        if(isFormValid) {
                            textValidIndicator.setText("Form Valid");
                        } else {
                            textValidIndicator.setText("Invalid");
                        }
                    }
                });

    }
}
