package com.diolan.wifistatelogger;

import android.app.Application;

/**
 * Created by d.barkalov on 06.08.2014.
 */
public class App extends Application {
    private  ChangesObservable mChangesObservable;

    public ChangesObservable getChangesObservable(){
        if(mChangesObservable == null){
            mChangesObservable = new ChangesObservable();
        }
        return mChangesObservable;
    }
}
