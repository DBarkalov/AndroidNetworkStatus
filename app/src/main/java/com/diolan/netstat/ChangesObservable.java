package com.diolan.netstat;

import android.database.Observable;

/**
 * Created by d.barkalov on 06.08.2014.
 */
public class ChangesObservable extends Observable<ChangesObservable.ChangesObserver> {

    public void notifyChanges() {
        synchronized(mObservers) {
            for (final ChangesObserver observer : mObservers) {
                observer.onChanged();
            }
        }
    }

    public interface ChangesObserver {
            void onChanged();
    }
}
