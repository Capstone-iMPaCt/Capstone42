package com.project.ilearncentral.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.ilearncentral.Activity.AddEditFeed;
import com.project.ilearncentral.Activity.NveJobPost;
import com.project.ilearncentral.Adapter.JobPostAdapter;
import com.project.ilearncentral.Adapter.PostFeedAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.JobVacancy;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.Model.Post;
import com.project.ilearncentral.MyClass.JobPosts;
import com.project.ilearncentral.MyClass.Posts;
import com.project.ilearncentral.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class JobPost extends Fragment {

    private JobPostAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<JobVacancy> jobs;

    private ObservableBoolean done;
    private ObservableString editOrView;

    private FloatingActionButton addNewPostBtn;
    private final int NEW_POST = 1, UPDATE_POST = 2;

    private SearchView searchView;
    private TextView toggleView;

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

        if (Account.isType("LearningCenter"))
            view.findViewById(R.id.feed_add_fab).setVisibility(View.VISIBLE);

        editOrView = new ObservableString();
        editOrView.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String newValue) {
                if (!newValue.isEmpty()) {
                    Intent i = new Intent(getContext(), NveJobPost.class);
                    if (newValue.charAt(0)=='y') {
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

        searchView = view.findViewById(R.id.feed_searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                jobs.clear();
                jobs.addAll(JobPosts.searchText(query));
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

        toggleView = view.findViewById(R.id.feed_toggle_view);
        if (Account.isType("Educator")) {
            view.findViewById(R.id.feed_searchview_line_divider).setVisibility(View.GONE);
            toggleView.setVisibility(View.GONE);
        } else {
            toggleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setToggleView(toggleView);
                }
            });
        }

        addNewPostBtn = view.findViewById(R.id.feed_add_fab);
        addNewPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), NveJobPost.class), NEW_POST);
            }
        });

        done = new ObservableBoolean();
        done.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean newValue) {
                jobs.clear();
                jobs.addAll(JobPosts.getJobPosts());
                adapter.notifyDataSetChanged();
            }
        });
        JobPosts.retrievePostsFromDB(done);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.feed_pullToRefresh);
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
            }
        });

        jobs = new ArrayList<>();
        recyclerView = view.findViewById(R.id.feed_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new JobPostAdapter(getContext(), editOrView, jobs);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode + " ");
        if (requestCode == NEW_POST && resultCode == RESULT_OK) {
            JobPosts.retrievePostFromDB(data.getStringExtra("jobId"), done);
        } else if (requestCode == UPDATE_POST && resultCode == RESULT_OK) {
            JobPosts.retrievePostFromDB(data.getStringExtra("jobId"), done);
        }
    }

    private void setToggleView(TextView v) {
        if (v.getText().toString() == "All") {
            v.setBackgroundResource(R.drawable.bg_unselected_day_rounded);
            v.setText("Mine");
            jobs.clear();
            jobs.addAll(JobPosts.searchText(""));
            adapter.notifyDataSetChanged();
        } else {
            v.setBackgroundResource(R.drawable.bg_selected_day_rounded);
            v.setText("All");
            jobs.clear();
            jobs.addAll(JobPosts.searchText(""));
            adapter.notifyDataSetChanged();
        }
    }
}
