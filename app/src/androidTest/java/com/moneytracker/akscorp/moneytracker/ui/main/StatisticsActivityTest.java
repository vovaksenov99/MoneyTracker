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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StatisticsActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void statisticsActivityTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.welcome_next_btn), withText("Продолжить"),
                        childAtPosition(
                                allOf(withId(R.id.welcome_card),
                                        childAtPosition(
                                                withId(R.id.layout),
                                                3)),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.et_account_name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_account_name),
                                        0),
                                0)));
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.et_account_name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_account_name),
                                        0),
                                0)));
        appCompatEditText2.perform(scrollTo(), replaceText("а"), closeSoftKeyboard());

        ViewInteraction mDButton = onView(
                allOf(withId(R.id.md_buttonDefaultPositive), withText("ОК"),
                        childAtPosition(
                                allOf(withId(R.id.md_root),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        mDButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction mDButton2 = onView(
                allOf(withId(R.id.md_buttonDefaultPositive), withText("Продолжить"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        mDButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.statisticsButton),
                        childAtPosition(
                                allOf(withId(R.id.bar_container),
                                        childAtPosition(
                                                withId(R.id.appBar),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Перейти вверх"),
                        childAtPosition(
                                allOf(withId(R.id.statistics_toolbar),
                                        childAtPosition(
                                                withId(R.id.statistics_appbar_layout),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.payment_button), withText("Добавить транзакцию"),
                        childAtPosition(
                                allOf(withId(R.id.appBar),
                                        childAtPosition(
                                                withId(R.id.layout),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction materialEditText = onView(
                allOf(withId(R.id.sum_et),
                        childAtPosition(
                                allOf(withId(R.id.appBar),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        materialEditText.perform(replaceText("600"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.confirm_btn), withText("Подтвердить"),
                        childAtPosition(
                                allOf(withId(R.id.appBar),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.payment_button), withText("Добавить транзакцию"),
                        childAtPosition(
                                allOf(withId(R.id.appBar),
                                        childAtPosition(
                                                withId(R.id.layout),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction materialEditText2 = onView(
                allOf(withId(R.id.sum_et),
                        childAtPosition(
                                allOf(withId(R.id.appBar),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        materialEditText2.perform(replaceText("-900"), closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.confirm_btn), withText("Подтвердить"),
                        childAtPosition(
                                allOf(withId(R.id.appBar),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.statisticsButton),
                        childAtPosition(
                                allOf(withId(R.id.bar_container),
                                        childAtPosition(
                                                withId(R.id.appBar),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.sum), withText("-900,00\u20BD"),
                        childAtPosition(
                                allOf(withId(R.id.layout_one),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("-900,00\u20BD")));

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.income_btn), withText("Доходы"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.statistics_scroll_children_container),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sum), withText("600,00\u20BD"),
                        childAtPosition(
                                allOf(withId(R.id.layout_one),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("600,00\u20BD")));

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.statistics_period_fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.period_4_radio_button), withText("Все время"),
                        childAtPosition(
                                allOf(withId(R.id.period_radio_group),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatRadioButton.perform(click());

        ViewInteraction mDButton3 = onView(
                allOf(withId(R.id.md_buttonDefaultPositive), withText("ОК"),
                        childAtPosition(
                                allOf(withId(R.id.md_root),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        mDButton3.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.period_text_view), withText("Все время"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.statistics_scroll_children_container),
                                        0),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("Все время")));

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
