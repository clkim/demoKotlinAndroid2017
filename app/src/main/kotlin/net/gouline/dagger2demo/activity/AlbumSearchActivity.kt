package net.gouline.dagger2demo.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_album_search.*
import net.gouline.dagger2demo.DemoApplication
import net.gouline.dagger2demo.R
import net.gouline.dagger2demo.rest.ITunesService
import javax.inject.Inject

/**
 * Activity for search iTunes albums by artist name.
 *
 *
 * Created by mgouline on 23/04/15.
 * Converted by clkim to Kotlin from original java,
 * then substantially refactored, e.g. RecyclerView,
 * hiding soft keyboard, hiding prompt in textview when
 * album list is not empty, handling orientation change
 * e.g. caching what has been fetched, using Retrofit 2,
 * RxJava/RxAndroid and life-cycle handling, Picasso...
 *
 * Added basic unit tests for Activity, written in Java
 *   AlbumSearchActivityTest
 * To run it, connect a device via USB to dev machine,
 * then in Android Studio > Run AlbumSearchActivityTest
 *
 * Acknowledgements:
 * Mike Gouline's blog on Dagger 2
 * Sittiphol Phanvilai’s blog on Retrofit 2
 * Dan Lew’s blogs “Grokking RxJava” and on RxAndroid 1.0
 * Edwin Jose and NILANCHALA for RecyclerView examples
 */
class AlbumSearchActivity : AppCompatActivity(),
        SearchView.OnQueryTextListener {

    // iTunes api service (Retrofit2, injected in by Dagger2)
    @Inject
    lateinit var mITunesService: ITunesService

    // injected properties using Kotlin Android Extensions
    // from ids in the layout file activity_album_search.xml
    //  recycler_view: RecyclerView
    //  empty_view:    TextView

    // adapter for recycler view
    private var mAlbumViewAdapter: AlbumViewAdapter? = null

    // composite disposable used to dispose rxjava2 disposables (was rxjava1 subscriptions)
    private var mCompositeDisposable: CompositeDisposable? = null

    // object with "static" member property used by Log.x
    companion object {
        private val TAG = AlbumSearchActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // ref https://google.github.io/dagger/android.html
        // gets a DispatchingAndroidInjector<Activity> gotten from DemoApplication
        //  and passes this Activity to inject()
        AndroidInjection.inject(this)
        // do that before calling super.onCreate()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_search)

        mAlbumViewAdapter = AlbumViewAdapter()

        // id of RecyclerView in layout, using Kotlin Android Extensions
        this.recycler_view.adapter = mAlbumViewAdapter
        this.recycler_view.layoutManager = LinearLayoutManager(this)

        // Reference - http://blog.danlew.net/2014/10/08/grokking-rxjava-part-4/
        // we follow the pattern in above blog reference, although we have just one disposable
        //  (was subscription in rxjava1) in this demo app
        mCompositeDisposable = CompositeDisposable()

        // if there is an Observable cached, use it to display album items from prior api call;
        // checking for null cache seems ok instead of checking for zero count, e.g.
        //   DemoApplication.albumItemObservableCache?.count()?.blockingGet()?.toLong() ?: 0L
        //  because we're not expiring the cache in the replay() call -- in which case the cache
        //  is not null but is empty and so we don't want to just display cached results
        if (DemoApplication.albumItemObservableCache != null) {
            displayCachedResults(DemoApplication.albumItemObservableCache)
            // hide prompt-textview
            setPromptVisibility(View.GONE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_album_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_item_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onQueryTextSubmit(term: String): Boolean {
        if (term.isNotEmpty()) {
            fetchResults(term)

            // hide soft keyboard
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(this.recycler_view.applicationWindowToken, 0)
        }

        return true
    }

    override fun onQueryTextChange(s: String): Boolean {
        // show prompt-textview if search term is blanked out and no album items are displayed
        if (s.isNotEmpty())
            setPromptVisibility(View.GONE)
        else if (s.isEmpty() && mAlbumViewAdapter?.itemCount == 0)
            setPromptVisibility(View.VISIBLE)

        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.dispose()
    }

    private fun fetchResults(term: String) {
        // clear the items in recyclerview adapter
        mAlbumViewAdapter?.clear()
        // cache newly fetched Observable
        DemoApplication.albumItemObservableCache =
                // using the injected Retrofit service
                mITunesService.search(term, "album")
                        .subscribeOn(Schedulers.io())
                        .flatMapIterable { it.results }
                        .map { AlbumItem(it.collectionName, it.artworkUrl100) }
                        .replay().autoConnect() // equivalent to operator cache() but seems preferred, ref: https://speakerdeck.com/dlew/common-rxjava-mistakes
        displayCachedResults(DemoApplication.albumItemObservableCache)
    }

    private fun displayCachedResults(cache: Observable<AlbumItem>) {
        // subscribe to the Observable so as to display the album items
        val disposable: Disposable = cache
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { mAlbumViewAdapter?.addAlbumItem(it)
                          mAlbumViewAdapter?.notifyItemInserted(
                                  mAlbumViewAdapter?.itemCount?.minus(1) ?: 0)
                        },
                        { Log.w(TAG, "Retrieve albums failed\n" + it.message, it) })
        // add the disposable to the CompositeDisposable
        //  so we can do lifecycle dispose
        mCompositeDisposable?.add(disposable)
    }

    private fun setPromptVisibility(visibility: Int) {
        // using id of TextView - Kotlin Android Extensions
        //  this contains the prompt to search for artists' albums
        this.empty_view.visibility = visibility
    }
}
