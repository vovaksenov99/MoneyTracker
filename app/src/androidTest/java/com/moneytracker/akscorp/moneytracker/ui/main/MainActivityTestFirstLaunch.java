package com.moneytracker.akscorp.moneytracker.ui.main;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.moneytracker.akscorp.moneytracker.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTestFirstLaunch {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Fire this test only on application first launch
     */
    @Test
    public void mainActivityTest() {
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.welcome_card), isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.welcome_next_btn), withText("Продолжить"), isDisplayed()));
        appCompatButton.perform(click());


        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction viewGroup = onView(withId(R.id.md_root));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText = onView(withId(R.id.et_account_name));
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(withId(R.id.et_account_name));
        appCompatEditText2.perform(scrollTo(), replaceText("а"), closeSoftKeyboard());

        ViewInteraction mDButton = onView(allOf(
                withId(R.id.md_buttonDefaultPositive), withText("ОК")));
        mDButton.perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction mDButton2 = onView(
                allOf(withId(R.id.md_buttonDefaultPositive), withText("Продолжить")));
        mDButton2.perform(click());

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction imageView = onView(withId(R.id.lbl_empty_operation_history));
        imageView.check(matches(isDisplayed()));

        ViewInteraction viewGroup3 = onView(
                allOf(childAtPosition(
                        withParent(withId(R.id.accountViewPager)),
                        0)));
        viewGroup3.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.account_name), withText("а")));
        textView.check(matches(withText("а")));

        ViewInteraction button = onView(
                allOf(withId(R.id.payment_button),
                        childAtPosition(withId(R.id.appBar), 3)));
        button.check(matches(isDisplayed()));

        ViewInteraction actionBar$Tab = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.accounts_tabDots),
                                0),
                        0)));
        actionBar$Tab.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.amountTextView), withText("0,00")));
        textView2.check(matches(withText("0,00")));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.accountsButton),
                        childAtPosition(
                                allOf(withId(R.id.bar_container),
                                        childAtPosition(
                                                withId(R.id.appBar),
                                                0)),
                                1)));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.infoButton),
                        childAtPosition(
                                allOf(withId(R.id.bar_container),
                                        childAtPosition(
                                                withId(R.id.appBar),
                                                0)),
                                2)));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.payment_button), withText("Добавить платеж")));
        appCompatButton2.perform(click());

        ViewInteraction textView3 = onView(withText("а"));
        textView3.check(matches(withText("а")));

        ViewInteraction materialEditText = onView(withId(R.id.sum_et));
        materialEditText.perform(replaceText("900"), closeSoftKeyboard());

        ViewInteraction button2 = onView(withId(R.id.confirm_btn));
        button2.check(matches(isDisplayed()));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.sum_et), withText("900")));
        editText2.check(matches(withText("900")));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.currency_btn), withText("\u20BD")));
        appCompatButton3.perform(click());

        ViewInteraction viewGroup4 = onView(
                childAtPosition(
                        allOf(withId(R.id.choose_rv),
                                childAtPosition(
                                        withId(R.id.rv_container),
                                        0)),
                        0));
        viewGroup4.check(matches(isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.choose_rv),
                        childAtPosition(
                                withId(R.id.rv_container),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction appCompatImageButton = onView(withId(R.id.date_btn));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("ОК")));
        appCompatButton4.check(matches(isDisplayed()));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton2 = onView(withId(R.id.categoty_btn));
        appCompatImageButton2.perform(click());

        ViewInteraction viewGroup5 = onView(childAtPosition(
                        allOf(withId(R.id.choose_rv),
                                childAtPosition(
                                        withId(R.id.rv_container),
                                        0)),
                        0));
        viewGroup5.check(matches(isDisplayed()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.choose_rv),
                        childAtPosition(
                                withId(R.id.rv_container),
                                0)));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatImageView = onView(withId(R.id.repeat_btn));
        appCompatImageView.perform(click());

        ViewInteraction viewGroup6 = onView(childAtPosition(withId(android.R.id.content), 0));
        viewGroup6.check(matches(isDisplayed()));

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.md_contentRecyclerView),
                        childAtPosition(
                                withId(R.id.md_contentListViewFrame),
                                0)));
        recyclerView3.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.confirm_btn), withText("Подтвердить")));
        appCompatButton5.perform(click());

        try {
            Thread.sleep(1400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction frameLayout2 = onView(childAtPosition(
                        allOf(withId(R.id.last_transactions),
                                childAtPosition(
                                        withId(R.id.container),
                                        0)),
                        0));
        frameLayout2.check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.sum), withText("900,00"),
                        childAtPosition(
                                allOf(withId(R.id.layout_balance),
                                        childAtPosition(
                                                withId(R.id.layout_one),
                                                2)),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("900,00")));

        ViewInteraction appCompatImageButton3 = onView(withId(R.id.accountsButton));
        appCompatImageButton3.perform(click());

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton = onView(withId(R.id.fab_new_account));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText3 = onView(withId(R.id.et_account_name));
        appCompatEditText3.perform(scrollTo(), click());
        appCompatEditText3.perform(scrollTo(), replaceText("а2"), closeSoftKeyboard());

        ViewInteraction mDButton3 = onView(
                allOf(withId(R.id.md_buttonDefaultPositive), withText("ОК")));
        mDButton3.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withContentDescription("Перейти вверх"),
                        childAtPosition(
                                allOf(withId(R.id.accounts_toolbar),
                                        childAtPosition(
                                                withId(R.id.accounts_appbar_layout),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction viewPager = onView(withId(R.id.accountViewPager));
        viewPager.perform(swipeLeft());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
