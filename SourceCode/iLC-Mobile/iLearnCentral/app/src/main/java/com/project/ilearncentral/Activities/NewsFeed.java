package com.project.ilearncentral.Activities;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Adapter.NewsFeedAdapter;
import com.project.ilearncentral.Model.News;
import com.project.ilearncentral.R;

import java.util.ArrayList;

public class NewsFeed extends Fragment {

    public NewsFeed() {
        // Required empty public constructor
    }
    NewsFeedAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<News> news;
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        // set up the RecyclerView
        // mao ni ang pag add ug data boang ma dynamic ni siya if nana ang firebasae nimo hahahha
        news = new ArrayList<>();
        news.add(new News("OnePlus 6T Camera Review:","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.","6 july 1994",R.drawable.user));
        news.add(new News("I love Programming And Design","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,","6 july 1994",R.drawable.user));
        news.add(new News("My first trip to Thailand story ","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.","6 july 1994",R.drawable.user));
        news.add(new News("After Facebook Messenger, Viber now gets...","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,","6 july 1994",R.drawable.user));
        news.add(new News("Isometric Design Grid Concept","lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit","6 july 1994",R.drawable.user));
        news.add(new News("Android R Design Concept 4K","lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit ","6 july 1994",R.drawable.user));
        news.add(new News("OnePlus 6T Camera Review:","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.","6 july 1994",R.drawable.user));
        news.add(new News("I love Programming And Design","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,","6 july 1994",R.drawable.user));
        news.add(new News("My first trip to Thailand story ","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.","6 july 1994",R.drawable.user));
        news.add(new News("After Facebook Messenger, Viber now gets...","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,","6 july 1994",R.drawable.user));
        news.add(new News("Isometric Design Grid Concept","lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit","6 july 1994",R.drawable.user));
        news.add(new News("Android R Design Concept 4K","lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit ","6 july 1994",R.drawable.user));
        news.add(new News("OnePlus 6T Camera Review:","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.","6 july 1994",R.drawable.user));
        news.add(new News("I love Programming And Design","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,","6 july 1994",R.drawable.user));
        news.add(new News("My first trip to Thailand story ","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.","6 july 1994",R.drawable.user));
        news.add(new News("After Facebook Messenger, Viber now gets...","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,","6 july 1994",R.drawable.user));
        news.add(new News("Isometric Design Grid Concept","lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit","6 july 1994",R.drawable.user));
        news.add(new News("Android R Design Concept 4K","lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit lorem ipsum dolor sit ","6 july 1994",R.drawable.user));
      recyclerView = view.findViewById(R.id.news_rv);
      recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      adapter = new NewsFeedAdapter(getContext(),news);
      recyclerView.setAdapter(adapter);
        return view;
    }
}
