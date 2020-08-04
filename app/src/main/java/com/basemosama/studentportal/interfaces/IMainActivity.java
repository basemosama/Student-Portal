package com.basemosama.studentportal.interfaces;

import androidx.navigation.NavController;

import com.basemosama.studentportal.utility.MyPreferenceManger;

public interface IMainActivity {

    void showLoading();

    void hideLoading();

    void showAddFab();

    void setFabClickListener(FabClickListener fabClickListener);

    void setDrawerListener(DrawerListener drawerListener);

    MyPreferenceManger getMyPreferenceManger();

    NavController getNavController();
}
