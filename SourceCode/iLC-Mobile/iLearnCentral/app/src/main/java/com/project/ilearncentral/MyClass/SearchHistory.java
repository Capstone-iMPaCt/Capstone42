package com.project.ilearncentral.MyClass;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class SearchHistory extends Observable {
    private static final String TAG = "SearchHistory";
    private static CollectionReference ref;
    private List<String> queryList;
    private String document;

    public SearchHistory() {
        this.queryList = new ArrayList<>();
        this.ref = FirebaseFirestore.getInstance().collection("SearchHistory");
    }

    public static SearchHistory getInstance() {
        return new SearchHistory();
    }

    public SearchHistory queryList(List<String> queryList) {
        this.queryList = queryList;
        return this;
    }

    public List<String> getQueryList() {
        return queryList;
    }

    public SearchHistory document(String document) {
        this.document = document;
        return this;
    }

    public void save() {
        if (queryList.size() == 0 || document == null) {
            Log.d(TAG, "Error saving queries.");
            return;
        }
        Map<String, Object> queries = new HashMap<>();
        queries.put("queries", queryList);
        ref.document(document).set(queries);
    }

    public void get() {
        ref.document(document)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {
                            List<String> queries = (List<String>) value.get("queries");
                            queryList.clear();
                            queryList.addAll(queries);
                            setChanged();
                            notifyObservers();
                        }
                    }
                });
    }
}
