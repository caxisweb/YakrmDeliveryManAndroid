package com.codeclinic.yakrmdeliveryman.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codeclinic.yakrmdeliveryman.Fragment.OrderlistFragment;
import com.codeclinic.yakrmdeliveryman.LocationUpdates.BackgroundLocationUpdateService;
import com.codeclinic.yakrmdeliveryman.R;
import com.codeclinic.yakrmdeliveryman.Utils.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static DrawerLayout drawer;
    SessionManager sessionManager;
    CoordinatorLayout main_content;
    Drawable drawable;

    private TabLayout tabLayout;

    private String[] tabTitles;
    private int[] tabimageResId = {R.drawable.ic_tab_new_order, R.drawable.ic_tab_new_order,R.drawable.ic_tab_new_order};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, BackgroundLocationUpdateService.class));
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager=new SessionManager(this);

        drawer = findViewById(R.id.drawer_layout);
        main_content = findViewById(R.id.main_content);
        tabLayout = findViewById(R.id.tabLayout);

        tabTitles = getResources().getStringArray(R.array.tab);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                String language = String.valueOf(getResources().getConfiguration().locale);
                if (language.equals("en")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else if (language.equals("en_GB")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else if (language.equals("en_")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else if (language.equals("en_001")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else if (language.equals("en_IN")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else if (language.equals("en_US")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else if (language.equals("en_150")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(-slideX);
                    } else {
                        main_content.setTranslationX(slideX);
                    }
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);

        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("en")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else if (language.equals("en_GB")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else if (language.equals("en_")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else if (language.equals("en_001")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else if (language.equals("en_IN")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else if (language.equals("en_US")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else if (language.equals("en_150")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_small_menu_icon, getTheme());
        }

        toggle.setHomeAsUpIndicator(drawable);
        toggle.syncState();
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        for(int i=0;i<tabTitles.length;i++){

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {

                    Fragment fragment = new OrderlistFragment("1");
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_contaner, fragment).commit();

                } else if (tab.getPosition() == 1) {

                    Fragment fragment = new OrderlistFragment("2");
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_contaner, fragment).commit();

                }else if(tab.getPosition()==2){
                    Fragment fragment = new OrderlistFragment("5");
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_contaner, fragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Fragment fragment = new OrderlistFragment("1");
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_contaner, fragment).commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header1 = navigationView.getHeaderView(0);
        LinearLayout llayout_main_page = header1.findViewById(R.id.llayout_main_page);
        LinearLayout layout_personal_account = header1.findViewById(R.id.layout_personal_account);
        LinearLayout llayout_support_contact = header1.findViewById(R.id.llayout_support_contact);
        LinearLayout llayout_about_app = header1.findViewById(R.id.llayout_about_app);
        LinearLayout llayout_instruction_conditions = header1.findViewById(R.id.llayout_instruction_conditions);
        LinearLayout llayout_signout = header1.findViewById(R.id.llayout_signout);
        TextView tv_signout = header1.findViewById(R.id.tv_signout);

        if (!sessionManager.isLoggedIn()) {
            tv_signout.setText(getResources().getString(R.string.Signup) + " / " + getResources().getString(R.string.Log_in));
        }
        LinearLayout llayout_english = header1.findViewById(R.id.llayout_english);
        final TextView tv_language_version = header1.findViewById(R.id.tv_language_version);

        llayout_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, R.style.CustomDialogFragment);
                    alert.setMessage(getResources().getString(R.string.AreYouSureToLogout));
                    alert.setCancelable(false);
                    alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            stopService(new Intent(MainActivity.this, BackgroundLocationUpdateService.class));
                            drawer.closeDrawer(GravityCompat.START);
                            if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                                findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                                setTitle(getResources().getString(R.string.title_activity_main));
                            }

                            sessionManager.setReminderStatus(false);
                            sessionManager.logoutUser();
                            finish();
                        }
                    }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                        findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                        setTitle(getResources().getString(R.string.title_activity_main));
                    }

                    sessionManager.logoutUser();
                    finish();
                }
            }
        });

        llayout_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.closeDrawer(GravityCompat.START);

                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.delivery_main_title));
                }

                String language_name = "";
                if (tv_language_version.getText().equals("النسخة العربية")) {
                    language_name = "ar";
                } else {
                    language_name = "en";
                }
                sessionManager.putLanguage("Language", language_name);

                Locale locale = new Locale(language_name);

                Resources resources = getResources();
                Configuration configuration = resources.getConfiguration();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                    configuration.setLocale(locale);
                } else{
                    configuration.locale=locale;
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
                    getApplicationContext().createConfigurationContext(configuration);
                } else {
                    resources.updateConfiguration(configuration,displayMetrics);
                }

                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.custome_tab_view, null);
        TextView tv_tab_title = v.findViewById(R.id.tv_tab_title);
        tv_tab_title.setText(tabTitles[position]);
        ImageView img = v.findViewById(R.id.img_tab_icon);
        img.setImageResource(tabimageResId[position]);
        return v;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.deliverymain, menu);

        try {

            final MenuItem menuItem3 = menu.findItem(R.id.action_notification);
            final MenuItem menuItem5 = menu.findItem(R.id.action_user);

            View actionView3 = MenuItemCompat.getActionView(menuItem3);
            View actionView5 = MenuItemCompat.getActionView(menuItem5);

            actionView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOptionsItemSelected(menuItem3);
                }
            });

            actionView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOptionsItemSelected(menuItem5);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            if (sessionManager.isLoggedIn()) {
                //startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
            } else {
                //startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
            return true;
        }  else if (id == R.id.action_user) {
            if (sessionManager.isLoggedIn()) {
                //startActivity(new Intent(DeliveryMain.this, PersonalDataActivity.class));
            } else {
                //startActivity(new Intent(DeliveryMain.this, StartActivity.class));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
