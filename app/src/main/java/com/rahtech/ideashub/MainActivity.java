package com.rahtech.ideashub;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.rahtech.ideashub.fragments.ChallengesFragment;
import com.rahtech.ideashub.fragments.ProjectIdeasFragment;
import com.rahtech.ideashub.fragments.QuickTipsFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ChallengesFragment challengesFragment;
    ProjectIdeasFragment projectIdeasFragment;
    QuickTipsFragment quickTipsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout=findViewById(R.id.tab_layout_main_activity);
        viewPager=findViewById(R.id.view_pager_main_activity);

        challengesFragment=new ChallengesFragment();
        projectIdeasFragment=new ProjectIdeasFragment();
        quickTipsFragment=new QuickTipsFragment();

        MyViewPageAdapter viewPageAdapter= new MyViewPageAdapter(getSupportFragmentManager(), 0);
        viewPageAdapter.addFragment(challengesFragment,"Challenges");
        viewPageAdapter.addFragment(projectIdeasFragment,"Project Ideas");
        viewPageAdapter.addFragment(quickTipsFragment,"QuickTips");
        viewPager.setAdapter(viewPageAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    private static class MyViewPageAdapter extends FragmentPagerAdapter{

        ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
        ArrayList<String> titlesArrayList=new ArrayList<>();
        public MyViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment,String title){
            fragmentArrayList.add(fragment);
            titlesArrayList.add(title);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titlesArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.signOut) {
//            new SignInActivity().signout();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .requestProfile()
                    .build();

            GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(this,gso);
            googleSignInClient.signOut()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        }
                    });

            return true;
        }
        else if (item.getItemId()==R.id.profile){
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}