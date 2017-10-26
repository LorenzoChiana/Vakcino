package com.example.loren.vaccinebooklet.listeners;

import android.support.v7.widget.RecyclerView;

import com.github.clans.fab.FloatingActionMenu;


public class MyOnScrollListener extends RecyclerView.OnScrollListener {

    FloatingActionMenu mFab;

    public MyOnScrollListener(FloatingActionMenu mFab) {
        this.mFab = mFab;
    }
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            mFab.showMenuButton(true);
        }
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0 || dy < 0 && mFab.isShown()) {
            mFab.hideMenuButton(true);
        }
    }
}
