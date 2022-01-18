package com.busrasonmez.socialenglish.education;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.education.Notebook.NotebookFragment;
import com.busrasonmez.socialenglish.education.Work.WorkFragment;
import com.busrasonmez.socialenglish.social_media.AddPost.PostSharing;
import com.busrasonmez.socialenglish.social_media.Home.HomeFragment;
import com.busrasonmez.socialenglish.social_media.Messages.MessagesFragment;
import com.busrasonmez.socialenglish.social_media.Notifications.NotificationActivity;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.Favorites.FavoritesFragment;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.Persons.PersonsFragment;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.ProfileActicity;
import com.busrasonmez.socialenglish.social_media.Search.SearchFragment;
import com.busrasonmez.socialenglish.user_login;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Education extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_mainpage);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar2);
        bottomNavigationView.setOnItemSelectedListener(nav);
        bottomNavigationView.setSelectedItemId(R.id.work);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorkFragment()).commit();

    }

    private NavigationBarView.OnItemSelectedListener nav = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.notebook:
                    selectedFragment = new NotebookFragment();
                    break;
                case R.id.work:
                    selectedFragment = new WorkFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            return true;
        }
    };

}
