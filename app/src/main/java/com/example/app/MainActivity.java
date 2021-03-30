package com.example.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("MARG");
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_chart, R.id.nav_parameter)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        /*Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();*/
                        navController.setGraph(R.navigation.mobile_navigation);
                        navController.navigate(R.id.nav_home);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_chart:
                        /*Toast.makeText(MainActivity.this, "chart", Toast.LENGTH_SHORT).show();*/
                        navController.setGraph(R.navigation.mobile_navigation);
                        navController.navigate(R.id.nav_chart);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_parameter:
                        /*Toast.makeText(MainActivity.this,"parameter",Toast.LENGTH_SHORT).show();*/
                        navController.setGraph(R.navigation.mobile_navigation);
                        navController.navigate(R.id.nav_parameter);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_readme:
                        navController.setGraph(R.navigation.mobile_navigation);
                        navController.navigate(R.id.nav_readme);
                        drawer.closeDrawers();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}