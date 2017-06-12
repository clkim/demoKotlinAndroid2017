package net.gouline.dagger2demo.activity;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.gouline.dagger2demo.R;
import net.gouline.dagger2demo.rest.ITunesService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

/**
 * Mostly following:
 *  https://developer.android.com/training/testing/start/index.html
 *  https://developer.android.com/topic/libraries/testing-support-library/index.html#AndroidJUnitRunner
 *  https://developer.android.com/training/testing/unit-testing/instrumented-unit-tests.html#build
 *
 * Created by clkim on 10/19/15, updated 06/11/16, updated 06/11/17
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class AlbumSearchActivityTest {

    @Rule
    public ActivityTestRule<AlbumSearchActivity> asaActivityTestRule = new ActivityTestRule<>(AlbumSearchActivity.class);


    @Test
    public void testPreConditions() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.empty_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testITuneServiceIsInjected() {
        ITunesService mITunesService = asaActivityTestRule.getActivity().getMITunesService();
        // test for successful Dagger 2 injection in the Activity class
        assertNotNull(mITunesService);
    }

    @Test
    public void testEmptyTextView_labelText() {
        onView(withId(R.id.empty_view)).check(matches(withText(R.string.search_empty)));
    }

}
