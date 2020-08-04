package com.basemosama.studentportal.utility;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class CombinedLiveData<A, B> extends MediatorLiveData<Pair<A, B>> {
    private A a;
    private B b;

    public CombinedLiveData(LiveData<A> ld1, LiveData<B> ld2) {
        setValue(Pair.create(a, b));

        addSource(ld1, new Observer<A>() {
            @Override
            public void onChanged(A a) {
                if (a != null) {
                    CombinedLiveData.this.a = a;
                }
                CombinedLiveData.this.setValue(Pair.create(a, b));
            }
        });

        addSource(ld2, new Observer<B>() {
            @Override
            public void onChanged(B b) {
                if (b != null) {
                    CombinedLiveData.this.b = b;
                }
                CombinedLiveData.this.setValue(Pair.create(a, b));
            }
        });
    }
}

