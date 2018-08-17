package com.morris.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.morris.myapplication.adapter.UserSection;
import com.morris.myapplication.model.User;
import com.morris.myapplication.model.UsersResponse;
import com.morris.myapplication.network.ApiClient;
import com.morris.myapplication.network.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // finish();
            }
        });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Interview");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                Log.d(TAG, "onTabSelected: pos: " + tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        // TODO: 31/03/17
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new UsersFragment(
                ContextCompat.getColor(this, R.color.colorAccent)), "All Users");
        adapter.addFrag(new DriverStatusFragment(
                ContextCompat.getColor(this, R.color.colorAccent)), "Driving Status");
        adapter.addFrag(new AgeFragment(
                ContextCompat.getColor(this, R.color.colorAccent)), "User By Age");
        adapter.addFrag(new EducationFragment(
                ContextCompat.getColor(this, R.color.colorAccent)), "Users By Education");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {

    }


    private static class ViewPagerAdapter extends FragmentPagerAdapter {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static class UsersFragment extends Fragment {
        private ApiService apiService;
        private CompositeDisposable disposable = new CompositeDisposable();
        Unbinder unbinder;
        int color;
        private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        private List<User> userList = new ArrayList<>();
        ArrayList<User> nonDupList = new ArrayList<>();
        public UsersFragment() {
        }

        @SuppressLint("ValidFragment")
        public UsersFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_users, container, false);
            apiService = ApiClient.getClient(getActivity()).create(ApiService.class);
            // bind view using butter knife
            unbinder = ButterKnife.bind(this, view);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            getAllUsers();

            return view;
        }

        private void getAllUsers() {
            JSONObject request = new JSONObject();
            try {
                request.put("", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("Request", request.toString());

            disposable.add(
                    apiService.getAllUsers(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<UsersResponse>() {
                                @Override
                                public void onSuccess(UsersResponse users) {


                                    userList.clear();
                                    userList.addAll(users.getUsers());
                                    userList = new ArrayList<User>(new LinkedHashSet<User>(userList));

                                    for(int i = 0; i < userList.size(); i++) {
                                        for(int j = i + 1; j < userList.size(); j++) {
                                            if(userList.get(i).getFullname().equalsIgnoreCase(userList.get(j).getFullname())){
                                                userList.remove(j);
                                                j--;
                                            }
                                        }
                                    }
                                    UserSection allUsers = new UserSection("List of All Users Without Duplicates",userList);
                                    sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
                                    sectionedRecyclerViewAdapter.addSection(allUsers);
                                    recyclerView.setAdapter(sectionedRecyclerViewAdapter);

                                    sectionedRecyclerViewAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("", "onError: " + e.getMessage());
                                    String m = "users not found";

                                }
                            })
            );

        }
    }


    public static class DriverStatusFragment extends Fragment {
        private ApiService apiService;
        private CompositeDisposable disposable = new CompositeDisposable();
        Unbinder unbinder;
        int color;
        private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        private List<User> usersList = new ArrayList<>();
        private List<User> usersDriving = new ArrayList<>();
        private List<User> usersNDriving = new ArrayList<>();
        public DriverStatusFragment() {
        }

        @SuppressLint("ValidFragment")
        public DriverStatusFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_users, container, false);
            apiService = ApiClient.getClient(getActivity()).create(ApiService.class);
            // bind view using butter knife
            unbinder = ButterKnife.bind(this, view);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            getAllUsers();

            return view;
        }

        private void getAllUsers() {
            JSONObject request = new JSONObject();
            try {
                request.put("", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("Request", request.toString());

            disposable.add(
                    apiService.getAllUsers(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<UsersResponse>() {
                                @Override
                                public void onSuccess(UsersResponse users) {
                                    Set setItems = new HashSet(users.getUsers());
                                    usersList.clear();
                                    usersList.addAll(setItems);
                                    usersNDriving.clear();
                                    usersDriving.clear();
                                    for(int i = 0; i < usersList.size(); i++) {
                                        for(int j = i + 1; j < usersList.size(); j++) {
                                            if(usersList.get(i).getFullname().equalsIgnoreCase(usersList.get(j).getFullname())){
                                                usersList.remove(j);
                                                j--;
                                            }
                                        }
                                    }

                                    for (int i = 0; i < usersList.size(); i++) {
                                        User user = usersList.get(i);
                                        if(user.isIsdriver() == true){
                                            usersDriving.add(usersList.get(i));
                                        }
                                            if (user.isIsdriver() != true) {
                                                usersNDriving.add(usersList.get(i));
                                            }



                                    }
                                    UserSection allUsersDriving = new UserSection("Users Driving",usersDriving);
                                    UserSection allUsersNotDriving = new UserSection("Users Not Driving",usersNDriving);
                                    sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
                                    sectionedRecyclerViewAdapter.addSection(allUsersDriving);
                                    sectionedRecyclerViewAdapter.addSection(allUsersNotDriving);
                                    recyclerView.setAdapter(sectionedRecyclerViewAdapter);
                                    sectionedRecyclerViewAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("", "onError: " + e.getMessage());
                                    String m = "users not found";

                                }
                            })
            );

        }

    }

    public static class AgeFragment extends Fragment {
        private ApiService apiService;
        private CompositeDisposable disposable = new CompositeDisposable();
        Unbinder unbinder;
        int color;
        private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.age_layout)
        LinearLayout age_layout;
        @BindView(R.id.total_age)
        TextView total_age;
        private List<User> usersList = new ArrayList<>();
        private List<User> youngUsers = new ArrayList<>();
        private List<User> youthUsers = new ArrayList<>();
        private List<User> oldUsers = new ArrayList<>();
        public AgeFragment() {
        }

        @SuppressLint("ValidFragment")
        public AgeFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_users, container, false);
            apiService = ApiClient.getClient(getActivity()).create(ApiService.class);
            // bind view using butter knife
            unbinder = ButterKnife.bind(this, view);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            getAllUsers();

            return view;
        }

        private void getAllUsers() {
            JSONObject request = new JSONObject();
            try {
                request.put("", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("Request", request.toString());

            disposable.add(
                    apiService.getAllUsers(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<UsersResponse>() {
                                @Override
                                public void onSuccess(UsersResponse users) {
                                    Set setItems = new HashSet(users.getUsers());
                                    usersList.clear();
                                    usersList.addAll(setItems);
                                    youngUsers.clear();
                                    youthUsers.clear();
                                    oldUsers.clear();
                                    for(int i = 0; i < usersList.size(); i++) {
                                        for(int j = i + 1; j < usersList.size(); j++) {
                                            if(usersList.get(i).getFullname().equalsIgnoreCase(usersList.get(j).getFullname())){
                                                usersList.remove(j);
                                                j--;
                                            }
                                        }
                                    }
                                    int sum = 0;

                                    for (int i = 0; i < usersList.size(); i++) {
                                        User user = usersList.get(i);
                                        try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        Date parse = null;

                                            parse = sdf.parse(user.getYob());

                                        Calendar c = Calendar.getInstance();
                                        c.setTime(parse);
                                        int age =  getAge(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
                                        Log.i("Age", String.valueOf(age));
                                        sum +=age;
                                        if(age <= 21){
                                            youngUsers.add(usersList.get(i));
                                        }
                                        if (age <=40 && age > 21) {
                                           youthUsers.add(usersList.get(i));
                                        }

                                        if(age > 40){
                                            oldUsers.add(usersList.get(i));
                                        }

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }



                                    }
                                    age_layout.setVisibility(View.VISIBLE);
                                    total_age.setText(String.valueOf(sum));
                                    UserSection young = new UserSection("Young Users",youngUsers);
                                    UserSection youth = new UserSection("Youth",youthUsers);
                                    UserSection old = new UserSection("Old Users",oldUsers);
                                    sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
                                    sectionedRecyclerViewAdapter.addSection(young);
                                    sectionedRecyclerViewAdapter.addSection(youth);
                                    sectionedRecyclerViewAdapter.addSection(old);
                                    recyclerView.setAdapter(sectionedRecyclerViewAdapter);

                                    sectionedRecyclerViewAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("", "onError: " + e.getMessage());
                                    String m = "users not found";

                                }
                            })
            );

        }
        public int getAge (int year, int month, int day) {

            GregorianCalendar cal = new GregorianCalendar();
            int y, m, d, noofyears;

            y = cal.get(Calendar.YEAR);// current year ,
            m = cal.get(Calendar.MONTH);// current month
            d = cal.get(Calendar.DAY_OF_MONTH);//current day
            cal.set(year, month, day);// here ur date
            noofyears = y - cal.get(Calendar.YEAR);
            if ((m < cal.get(Calendar.MONTH))
                    || ((m == cal.get(Calendar.MONTH)) && (d < cal
                    .get(Calendar.DAY_OF_MONTH)))) {
                --noofyears;
            }
            if (noofyears < 0)
                throw new IllegalArgumentException("age < 0");
            System.out.println(noofyears);
            return noofyears;
        }
    }


    public static class EducationFragment extends Fragment {
        private ApiService apiService;
        private CompositeDisposable disposable = new CompositeDisposable();
        Unbinder unbinder;
        int color;
        private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        private List<User> usersList = new ArrayList<>();
        private List<User> graduateList = new ArrayList<>();
        private List<User> postgraduateList = new ArrayList<>();
        private List<User> phdList = new ArrayList<>();
        public EducationFragment() {
        }

        @SuppressLint("ValidFragment")
        public EducationFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_users, container, false);
            apiService = ApiClient.getClient(getActivity()).create(ApiService.class);
            // bind view using butter knife
            unbinder = ButterKnife.bind(this, view);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            getAllUsers();

            return view;
        }
        @Override
        public void onStop() {
            super.onStop();
            if (disposable!=null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }

        private void getAllUsers() {
            JSONObject request = new JSONObject();
            try {
                request.put("", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("Request", request.toString());


            disposable.add(
                    apiService.getAllUsers(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<UsersResponse>() {
                                @Override
                                public void onSuccess(UsersResponse users) {
                                    Set setItems = new HashSet(users.getUsers());
                                    usersList.clear();
                                    usersList.addAll(setItems);
                                    for(int i = 0; i < usersList.size(); i++) {
                                        for(int j = i + 1; j < usersList.size(); j++) {
                                            if(usersList.get(i).getFullname().equalsIgnoreCase(usersList.get(j).getFullname())){
                                                usersList.remove(j);
                                                j--;
                                            }
                                        }
                                    }

                                    phdList.clear();
                                    graduateList.clear();
                                    postgraduateList.clear();
                                    for (int i = 0; i < usersList.size(); i++) {
                                        User user = usersList.get(i);
                                        if(user.getEducation().equalsIgnoreCase("PHD")){
                                            phdList.add(usersList.get(i));
                                        }
                                        if (user.getEducation().equalsIgnoreCase("GRADUATE")) {
                                            graduateList.add(usersList.get(i));
                                        }
                                        if (user.getEducation().equalsIgnoreCase("POST GRAD")) {
                                            postgraduateList.add(usersList.get(i));
                                        }



                                    }
                                    UserSection phd = new UserSection("PHD users",phdList);
                                    UserSection graduate = new UserSection("Graduate Users",graduateList);
                                    UserSection post_graduate = new UserSection("Post Graduate Users",postgraduateList);
                                    sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
                                    sectionedRecyclerViewAdapter.addSection(phd);
                                    sectionedRecyclerViewAdapter.addSection(graduate);
                                    sectionedRecyclerViewAdapter.addSection(post_graduate);
                                    recyclerView.setAdapter(sectionedRecyclerViewAdapter);
                                    sectionedRecyclerViewAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("", "onError: " + e.getMessage());
                                    String m = "users not found";

                                }
                            })
            );

        }



    }




}
