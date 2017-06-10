package net.gouline.dagger2demo.rest;

import net.gouline.dagger2demo.model.ITunesResultSet;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * iTunes service.
 * <p/>
 * Created by mgouline on 23/04/15.
 */
public interface ITunesService {
    @GET("search")
    Flowable<ITunesResultSet> search(@Query("term") String term,
                                     @Query("entity") String entity);
}
