package com.jvmfrog.ffsettings.utils;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jvmfrog.ffsettings.R;

public class FragmentUtils {

    public static void changeFragment(FragmentActivity activity, Fragment to, int frameId, Bundle bundle) {
        to.setArguments(bundle);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, to);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    public static void changeFragmentWithBackStack(FragmentActivity activity, Fragment to, int frameId, String backstack, Bundle bundle) {
        to.setArguments(bundle);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, to);
        transaction.addToBackStack(backstack);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    public static void changeFragmentWithAnimOne(FragmentActivity activity, Fragment to, int frameId) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(frameId, to);
        transaction.commit();
    }

    public static void changeFragmentWithAnimTwo(FragmentActivity activity, Fragment to, int frameId) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(frameId, to);
        transaction.commit();
    }

    private static FragmentTransaction defaultFragmentTranslation(FragmentActivity activity, Fragment to, int frameId) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameId, to);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        return fragmentTransaction;
    }
}
