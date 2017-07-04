package net.gouline.dagger2demo.di;

import android.app.Activity;

import net.gouline.dagger2demo.activity.AlbumSearchActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * Created by clkim on 7/2/17
 *  reference https://google.github.io/dagger/android.html
 */

@Module(subcomponents = DemoActivityModule.DemoActivitySubcomponent.class)
abstract class DemoActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(AlbumSearchActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindAlbumSearchActivityInjectorFactory(
            DemoActivitySubcomponent.Builder builder);

    // inner interface of class just for convenience
    @Subcomponent
    public interface DemoActivitySubcomponent extends AndroidInjector<AlbumSearchActivity> {
        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<AlbumSearchActivity> {} // public, since inner class of interface
    }
}
