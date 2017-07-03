package net.gouline.dagger2demo;

import android.app.Activity;
import android.app.Application;

import net.gouline.dagger2demo.activity.AlbumItem;
import net.gouline.dagger2demo.di.DaggerDemoApplicationComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.reactivex.Observable;

/**
 * Custom application definition.
 * <p/>
 * Created by mgouline on 23/04/15.
 * Major refactoring to use AndroidInjector by clkim on 7/4/2017.
 *  reference https://google.github.io/dagger/android.html
 */
public class DemoApplication extends Application implements HasActivityInjector {
    // cache what is obtained from iTunes api service
    static public Observable<AlbumItem> albumItemObservableCache;

    // injected in by DemoApplicationComponent
    @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // pass this DemoApplication to @Component's doInject()
        DaggerDemoApplicationComponent.create().doInject(this);
    }
}
