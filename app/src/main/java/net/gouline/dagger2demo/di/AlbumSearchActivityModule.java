package net.gouline.dagger2demo.di;

import net.gouline.dagger2demo.activity.AlbumSearchActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by clkim on 7/2/17
 *  reference https://google.github.io/dagger/android.html
 */
@Module
abstract class AlbumSearchActivityModule {
    @ContributesAndroidInjector
    abstract AlbumSearchActivity contributeAlbumSearchActivityInjector();
}

/* below works too but seems can be simplified to above as suggested by reference https://google.github.io/dagger/android.html
@Module(subcomponents = AlbumSearchActivityModule.AlbumSearchActivitySubcomponent.class)
abstract class AlbumSearchActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(AlbumSearchActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindAlbumSearchActivityInjectorFactory(
            AlbumSearchActivitySubcomponent.Builder builder);

    // inner interface of class just for convenience
    @Subcomponent
    public interface AlbumSearchActivitySubcomponent extends AndroidInjector<AlbumSearchActivity> {
        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<AlbumSearchActivity> {} // public, since inner class of interface
    }
}
*/
