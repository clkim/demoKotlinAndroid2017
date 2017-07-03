package net.gouline.dagger2demo.di;

import net.gouline.dagger2demo.rest.ITunesService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Application-wide dependencies.
 * <p/>
 * Now it only contains the injectable objects and has no direct pairing
 * to the injection targets - that's what components are now for.
 * <p/>
 * Created by mgouline on 23/04/15.
 * Refactored by clkim to use RxJava.
 */
@Module
public class ITunesServiceModule {
    private ITunesService mITunesService;

    public ITunesServiceModule() {
        mITunesService = new Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ITunesService.class);
    }

    @Provides
    ITunesService provideITunesService() {
        return mITunesService;
    }
}
