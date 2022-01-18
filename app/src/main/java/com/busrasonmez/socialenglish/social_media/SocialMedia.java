package com.busrasonmez.socialenglish.social_media;

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

import com.busrasonmez.socialenglish.social_media.AddPost.PostSharing;
import com.busrasonmez.socialenglish.social_media.Messages.MessagePage;
import com.busrasonmez.socialenglish.social_media.Notifications.NotificationActivity;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.Persons.PersonsFragment;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.ProfileActicity;
import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.Favorites.FavoritesFragment;
import com.busrasonmez.socialenglish.social_media.Home.HomeFragment;

import com.busrasonmez.socialenglish.social_media.Messages.MessagesFragment;
import com.busrasonmez.socialenglish.social_media.Search.SearchFragment;
import com.busrasonmez.socialenglish.user_login;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SocialMedia extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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
        setContentView(R.layout.activity_social_media);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.homedrawer);
        navigationView = findViewById(R.id.drawerfragment);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setOnItemSelectedListener(nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    private NavigationBarView.OnItemSelectedListener nav = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.home:

                    selectedFragment = new HomeFragment();
                    break;
                case R.id.star:
                    selectedFragment = new NotificationActivity();
                    break;
                case R.id.persons:
                    selectedFragment = new PostSharing();
                    break;
                case R.id.message:
                    selectedFragment = new MessagesFragment();
                    break;
                case R.id.search:
                    selectedFragment = new SearchFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            return true;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment selectedFragment = null;

        switch (item.getItemId()){
            case R.id.like:

                break;
            case R.id.profile:
                Intent intent = new Intent(this, ProfileActicity.class);
                startActivity(intent);
                break;
            case R.id.favori:
                Intent intent4 = new Intent(this, FavoritesFragment.class);
                startActivity(intent4);
                break;
            case R.id.person:
                Intent intent3 = new Intent(this, PersonsFragment.class);
                startActivity(intent3);
                break;
            case R.id.sign_out:
                auth.signOut();

                Intent intent2 = new Intent(this, user_login.class);
                startActivity(intent2);
                break;
        }
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

}