package com.basemosama.studentportal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.basemosama.studentportal.database.CacheDataBase;
import com.basemosama.studentportal.database.executor.AppExecutor;
import com.basemosama.studentportal.databinding.ActivityMainBinding;
import com.basemosama.studentportal.databinding.NavHeaderMainBinding;
import com.basemosama.studentportal.interfaces.DrawerListener;
import com.basemosama.studentportal.interfaces.FabClickListener;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.User;
import com.basemosama.studentportal.repository.LoginRepository;
import com.basemosama.studentportal.repository.ProfessorRepository;
import com.basemosama.studentportal.repository.StudentRepository;
import com.basemosama.studentportal.utility.Constants;
import com.basemosama.studentportal.utility.MyPreferenceManger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements IMainActivity, FloatingActionButton.OnClickListener {
    AppBarConfiguration mAppBarConfiguration;
    NavController navController;
    ActivityMainBinding mainBinding;
    private DrawerLayout drawerLayout;
    private User user;
    private MyPreferenceManger myPreferenceManger;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View headerView;
    private NavHeaderMainBinding navHeaderMainBinding;
    private ProgressBar progressBar;
    private FloatingActionButton addFab;
    private FabClickListener fabClickListener;
    private DrawerListener drawerListener;
    private CacheDataBase dataBase;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpUI();
        setUpNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setUpUI() {
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        toolbar = mainBinding.mainBarLayout.toolbar;
        setSupportActionBar(toolbar);
        drawerLayout = mainBinding.drawerLayout;
        navigationView = mainBinding.navView;
        bottomNavigationView = mainBinding.mainBarLayout.mainContent.bottomNavigation;
        progressBar = mainBinding.mainBarLayout.mainContent.progressBar;
        addFab = mainBinding.mainBarLayout.fab;
        headerView = navigationView.getHeaderView(0);
        navHeaderMainBinding = NavHeaderMainBinding.bind(headerView);
        myPreferenceManger = new MyPreferenceManger(this);
        user = myPreferenceManger.getCurrentUser();
        addFab.setOnClickListener(this);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
                if (drawerListener != null)
                    drawerListener.onDrawerStateChanged();
                super.onDrawerStateChanged(newState);
            }
        });
        dataBase = CacheDataBase.getInstance(this);

    }

    private void setUpNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_group, R.id.nav_subject, R.id.nav_assignments, R.id.nav_source
                , R.id.nav_table, R.id.nav_events, R.id.nav_result)
                .setOpenableLayout(drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                logout();
                return false;
            }
        });
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getParent() != null && destination.getParent().getId() == R.id.login_navigation) {
                    toolbar.setVisibility(View.GONE);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    hideLoading();
                    user = myPreferenceManger.getCurrentUser();
                    setUpMenuHeader();
                    if (destination.getParent().getId() == R.id.student_navigation) {
                        navigationView.getMenu().findItem(R.id.nav_source).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_subject).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_result).setTitle(R.string.result_text);
                        String tag = String.valueOf(bottomNavigationView.getTag());
                        if (!TextUtils.equals(tag, Constants.STUDENT_TAG)) {
                            bottomNavigationView.setTag(Constants.STUDENT_TAG);
                            bottomNavigationView.getMenu().clear();
                            bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);
                        }
                    } else {
                        navigationView.getMenu().findItem(R.id.nav_source).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_subject).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_result).setTitle(R.string.year_work_menu_text);
                        String tag = String.valueOf(bottomNavigationView.getTag());
                        if (!TextUtils.equals(tag, Constants.PROFESSOR_TAG)) {
                            bottomNavigationView.setTag(Constants.PROFESSOR_TAG);
                            bottomNavigationView.getMenu().clear();
                            bottomNavigationView.inflateMenu(R.menu.prof_bottom_nav_menu);

                        }
                    }
                }
                // To return the toolbar in place when entering new destination
                mainBinding.mainBarLayout.appBarLayout.setExpanded(true, true);
                addFab.setVisibility(View.GONE);

            }
        });
    }

    private void setUpMenuHeader() {
        navHeaderMainBinding.setUser(user);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.PROFILE_INFO_BUNDLE_KEY, user);
                navController.navigate(R.id.profileFragment, bundle);
                drawerLayout.closeDrawers();
            }
        });
    }

    private void logout() {
        showLoading();
        myPreferenceManger.clearData();
        LoginRepository.clear();
        ProfessorRepository.clear();
        StudentRepository.clear();
        AppExecutor.getExecutor().getDiskIo().execute(new Runnable() {
            @Override
            public void run() {
                dataBase.clearAllTables();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        navController.navigate(R.id.login_navigation);
                        Toast.makeText(getApplicationContext(), R.string.logged_out_message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showAddFab() {
        addFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFabClickListener(FabClickListener fabClickListener) {
        this.fabClickListener = fabClickListener;
    }

    @Override
    public void setDrawerListener(DrawerListener drawerListener) {
        this.drawerListener = drawerListener;
    }

    @Override
    public MyPreferenceManger getMyPreferenceManger() {
        return myPreferenceManger;
    }

    @Override
    public NavController getNavController() {
        return navController;
    }

    @Override
    public void onClick(View view) {
        if (fabClickListener != null) fabClickListener.onFabClickListener(navController);
    }
}
