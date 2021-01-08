package com.project.ilearncentral.Fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.ilearncentral.Activity.Applicants;
import com.project.ilearncentral.Activity.NveJobPost;
import com.project.ilearncentral.Adapter.JobPostAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.CSVFile;
import com.project.ilearncentral.Model.JobVacancy;
import com.project.ilearncentral.Model.ResumeItem;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.JobPosts;
import com.project.ilearncentral.MyClass.Resume;
import com.project.ilearncentral.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class JobPost extends Fragment {

    private final String TAG = "Chat job post user";
    private JobPostAdapter adapter;
    private RecyclerView recyclerView;
    private List<JobVacancy> jobs;
    private List<JobVacancy> recommendation;
    private List<JobVacancy> temp;

    private ObservableBoolean done;
    private ObservableString editOrView;

    private FloatingActionButton addNewPostBtn;
    private final int NEW_POST = 1, UPDATE_POST = 2;

    private SwipeRefreshLayout pullToRefresh;
    private SearchView searchView;
    private TextView toggleView, searchOption, applicants, closed, all, applied, pageMessage;
    private View horizontalDivider;
    private LinearLayout options;
    private ImageButton toggleRecommend;
    private boolean isAll, recommend = false;
    public static boolean applying;

    private InputStream inputStream;

    public JobPost() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        bindLayout(view);
        isAll = true;
        applying = false;
        all.setTextColor(Color.CYAN);

        if (Account.isType("Educator")) {
            view.findViewById(R.id.educator_tab_app_bar_horizontal_line_divider).setVisibility(View.VISIBLE);
            view.findViewById(R.id.feed_app_bar_edu_options_layout).setVisibility(View.VISIBLE);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                jobs.clear();
                jobs.addAll(JobPosts.searchText(query, isAll));
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                done.set(true);
                return false;
            }
        });
//        applicants.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(), Applicants.class));
//            }
//        });
        applicants.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    applicants.setTextColor(Color.CYAN);
                    closed.setTextColor(Color.GRAY);
                    startActivity(new Intent(getContext(), Applicants.class));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    applicants.setTextColor(Color.GRAY);
                }
                return false;
            }
        });
        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closed.setTextColor(Color.CYAN);
                applicants.setTextColor(Color.GRAY);

                isAll = false;
                jobs.clear();
                jobs.addAll(JobPosts.getClosedJobVacancies(JobPosts.getJobPosts(), Account.getCenterId()));
                adapter.notifyDataSetChanged();
                toggleView.setText("Mine");
                toggleView.setBackgroundResource(R.drawable.bg_unselected_day_rounded);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all.setTextColor(Color.CYAN);
                applied.setTextColor(Color.GRAY);

                isAll = true;
                jobs.clear();
                jobs.addAll(JobPosts.searchText("", isAll));
//                adapter.notifyDataSetChanged();
                setRecommendationOff();
            }
        });
        applied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applied.setTextColor(Color.CYAN);
                all.setTextColor(Color.GRAY);
                pageMessage.setVisibility(View.GONE);

                isAll = false;
                jobs.clear();
                jobs.addAll(JobPosts.eduAppliedTo(JobPosts.getJobPosts(), Account.getUsername()));
                adapter.notifyDataSetChanged();
                if (jobs.size() == 0) {
                    pageMessage.setVisibility(View.VISIBLE);
                    pageMessage.setText("No job post applied yet.");
                }
            }
        });
        editOrView = new ObservableString();
        editOrView.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String newValue) {
                if (!newValue.isEmpty()) {
                    Intent i = new Intent(getContext(), NveJobPost.class);
                    if (newValue.charAt(0) == 'y') {
                        i.putExtra("jobId", newValue.substring(1));
                        startActivityForResult(i, UPDATE_POST);
                    } else {
                        i.putExtra("jobId", newValue.substring(1));
                        i.putExtra("View", true);
                        startActivity(i);
                    }
                }

            }
        });
        addNewPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), NveJobPost.class), NEW_POST);
            }
        });

        toggleView = view.findViewById(R.id.feed_app_bar_toggle_view);
        if (!Account.isType("Student")) {
            view.findViewById(R.id.feed_app_bar_vertical_line_divider).setVisibility(View.VISIBLE);
        }
        if (Account.isType("LearningCenter")) {
            addNewPostBtn.setVisibility(View.VISIBLE);
            searchOption.setVisibility(View.VISIBLE);
            searchOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        if (Account.isType("Educator")) {
            addNewPostBtn.setVisibility(View.GONE);
            toggleView.setVisibility(View.GONE);
            toggleRecommend.setVisibility(View.VISIBLE);

            final List<String> profile = getProfileLibrary();
            toggleRecommend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recommend) {
                        all.callOnClick();
                    } else {
                        toggleRecommend.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 235, 59)));
                        isAll = false;
                        all.setTextColor(Color.GRAY);
                        applied.setTextColor(Color.GRAY);

                        temp = new ArrayList<>();
                        temp.addAll(jobs);
                        jobs.clear();
                        for (JobVacancy jobPost : temp) {
                            for (String keyWord : profile) {
                                if (jobPost.getPosition().toLowerCase().contains(keyWord.trim())
                                        || jobPost.getJobDescription().toLowerCase().contains(keyWord.trim())) {
                                    if (!jobs.contains(jobPost)) {
                                        jobs.add(jobPost);
//                                        Log.d(TAG, "KEYWORD: " + keyWord);
                                    }
                                }
                            }
                        }
                        if (jobs.size() == 0) {
                            pageMessage.setVisibility(View.VISIBLE);
                            pageMessage.setText("Sorry, there are no recommendations for you at the moment.");
                        }
                        adapter.notifyDataSetChanged();
                        recommend = true;
                    }
                }
            });
        } else if (Account.isType("LearningCenter")) {
            horizontalDivider.setVisibility(View.VISIBLE);
            options.setVisibility(View.VISIBLE);
            toggleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setToggleView();
                }
            });
        }

        done = new ObservableBoolean();
        done.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean newValue) {
                jobs.clear();
                jobs.addAll(JobPosts.searchText("", isAll));
                adapter.notifyDataSetChanged();
            }
        });
        JobPosts.retrievePostsFromDB(done);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!searchView.isIconified()) {
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                }
                searchView.clearFocus();
                JobPosts.retrievePostsFromDB(done);
                pullToRefresh.setRefreshing(false);
                setRecommendationOff();
            }
        });

        jobs = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new JobPostAdapter(getContext(), editOrView, jobs);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void setRecommendationOff() {
        toggleRecommend.setBackgroundTintList(null);
        adapter.notifyDataSetChanged();
        recommend = false;
        isAll = true;
        all.setTextColor(Color.CYAN);
        applied.setTextColor(Color.GRAY);
        pageMessage.setVisibility(View.GONE);
    }

    private void bindLayout(View view) {
        searchView = view.findViewById(R.id.educator_tab_app_bar_searchview);
        pullToRefresh = view.findViewById(R.id.educator_tab_pullToRefresh);
        searchOption = view.findViewById(R.id.feed_app_bar_toggle_view);
        toggleRecommend = view.findViewById(R.id.educator_tab_app_bar_toggle_recommend);
        horizontalDivider = view.findViewById(R.id.educator_tab_app_bar_horizontal_line_divider);
        options = view.findViewById(R.id.feed_app_bar_lc_options_layout);
        applicants = view.findViewById(R.id.feed_app_bar_lc_option_applicants);
        closed = view.findViewById(R.id.feed_app_bar_lc_option_closed_posts);
        all = view.findViewById(R.id.feed_app_bar_edu_option_all);
        applied = view.findViewById(R.id.feed_app_bar_edu_option_applied);
        recyclerView = view.findViewById(R.id.educator_tab_recylerview);
        pageMessage = view.findViewById(R.id.educator_tab_job_posts_message);

        addNewPostBtn = view.findViewById(R.id.feed_add_fab);

//        searchOption.setBackgroundResource();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_POST && resultCode == RESULT_OK) {
            JobPosts.retrievePostFromDB(data.getStringExtra("jobId"), done);
        } else if (requestCode == UPDATE_POST && resultCode == RESULT_OK) {
            JobPosts.retrievePostFromDB(data.getStringExtra("jobId"), done);
        }
    }

    private void setToggleView() {
        if (toggleView.getText().toString().equalsIgnoreCase("All")) {
            isAll = false;
            toggleView.setText("Mine");
            toggleView.setBackgroundResource(R.drawable.bg_unselected_day_rounded);
        } else {
            isAll = true;
            toggleView.setBackgroundResource(R.drawable.bg_selected_day_rounded);
            toggleView.setText("All");
        }
        jobs.clear();
        jobs.addAll(JobPosts.searchText("", isAll));
        adapter.notifyDataSetChanged();
        searchView.clearFocus();
        closed.setTextColor(Color.GRAY);
        applicants.setTextColor(Color.GRAY);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (applying) {
            applying = false;
            JobPosts.retrievePostsFromDB(done);
        }
    }

    private List<String> getProfileLibrary() {
        inputStream = getResources().openRawResource(R.raw.root_word_collection);
        List<String> library = new ArrayList<>();
        CSVFile csvFile = new CSVFile(inputStream);
        List<String> rootWords = csvFile.read();

        if (Resume.getObjective() != null) {
            Collections.addAll(library, Resume.getObjective().toLowerCase().trim().split("\\W+"));
        }
        if (Resume.getSkills() != null) {
            for (ResumeItem item : Resume.getSkills()) {
                Collections.addAll(library, item.getDetail().toLowerCase().trim().split("\\W+"));
            }
        }
        if (Resume.getAwards() != null) {
            for (ResumeItem item : Resume.getAwards()) {
                Collections.addAll(library, item.getDetail().toLowerCase().trim().split("\\W+"));
            }
        }
        if (Resume.getQualities() != null) {
            for (ResumeItem item : Resume.getQualities()) {
                Collections.addAll(library, item.getDetail().toLowerCase().trim().split("\\W+"));
            }
        }
        if (Resume.getInterest() != null) {
            for (ResumeItem item : Resume.getInterest()) {
                Collections.addAll(library, item.getDetail().toLowerCase().trim().split("\\W+"));
            }
        }
        List<String> profileKeyWords = new ArrayList<>();
        for (String item : library) {
            for (String word : rootWords) {
                if (item.contains(word) && !profileKeyWords.contains(word)) {
                    profileKeyWords.add(word);
                }
            }
        }
        profileKeyWords.removeAll(Arrays.asList("", null));
//        for (String item : profileKeyWords) {
//            Log.d(TAG, "WORDS: " + item);
//        }
        return profileKeyWords;
    }
}
