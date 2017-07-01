package net.gouline.dagger2demo.rest;

import net.gouline.dagger2demo.model.ITunesResultSet;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * iTunes service.
 * <p/>
 * Created by mgouline on 23/04/15.
 */
public interface ITunesService {
    @GET("search")
    // Use Observable and not Flowable; we don't have a "request-produce" mechanism to support backpressure
    //  ref: https://artemzin.com/blog/reply-to-kaushik-gopals-aricle-rxjava-1-rxjava-2-understanding-the-changes/?utm_source=Android+Weekly&utm_campaign=db675e4c05-android-weekly-263&utm_medium=email&utm_term=0_4eb677ad19-db675e4c05-338015473
    Observable<ITunesResultSet> search(@Query("term") String term,
                                       @Query("entity") String entity);
}
