package com.book.gourav.bookapp.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.book.gourav.bookapp.GetterAndSetter.Books;

import com.book.gourav.bookapp.R;
import com.book.gourav.bookapp.fragment.OneFragment;
import com.book.gourav.bookapp.fragment.ThreeFragment;
import com.book.gourav.bookapp.fragment.TwoFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    DatabaseReference databaseReference;

    CircleImageView   profileimage;
    TextView user_name,user_email;

    FirebaseUser user;
    ProgressDialog progressDialog;

    RecyclerView recyclerView;
    List<Books> books=new ArrayList<Books>();
    List<String> booksname= Arrays.asList("SCIENCE","MATHEMATICS","SOCIAL SCIENCE","ENGLISH","HINDI","SANSKRIT");
    RecyclerView.Adapter adapter;

    public static InterstitialAd mInterstitialAd;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        progressDialog=new ProgressDialog(this);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        AppRater.app_launched(this);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());





        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        user= FirebaseAuth.getInstance().getCurrentUser();

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener( this);
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        profileimage=(CircleImageView)view.findViewById(R.id.user_image);
        user_name =(TextView)view.findViewById( R.id.user_name );
        user_email = (TextView)view.findViewById( R.id.email );
        Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(profileimage);
        user_name.setText(user.getDisplayName());
        user_email.setText(user.getEmail());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        },5000);





    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Books) {

        } else if (id == R.id.AboutUs) {
            Intent intent=new Intent(this,Aboutus_Activity.class);
            startActivity(intent);

        } else if (id == R.id.Share) {

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "https://play.google.com/store/apps/details?id=com.book.gourav.bookapp.activity";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));


        } else if (id == R.id.Website) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://lakshyacoachinglc.blogspot.com/")));


        } else if (id == R.id.Privacy) {

            startActivity(new Intent(this,Privacy_Policy.class));

        }
//        else if (id == R.id.Logout) {
//
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_dice, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.AboutUs) {
//                startActivity(new Intent(this,Aboutus_Activity.class));
//
//        }
//        if (id == R.id.Share) {
//            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//            sharingIntent.setType("text/plain");
//            String shareBody = "";
//            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
//            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
//            startActivity(Intent.createChooser(sharingIntent, "Share via"));
//
//
//        }
//        if (id == R.id.Review) {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/")));
//        }
//        if (id == R.id.Privacy) {
//            startActivity(new Intent(this,Privacy_Policy.class));
//
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Learn");
        adapter.addFragment(new TwoFragment(), "Videos");
        adapter.addFragment(new ThreeFragment(), "Updates");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public static boolean isNetworkAvaible(Context context)
    {
        ConnectivityManager conMan = (ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
        return conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        int pid=android.os.Process.myPid();
        android.os.Process.killProcess(pid);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        int pid=android.os.Process.myPid();
        android.os.Process.killProcess(pid);

    }
}
