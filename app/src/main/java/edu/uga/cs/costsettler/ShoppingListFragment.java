package edu.uga.cs.costsettler;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment {
    private final String TAG = "ShoppingListFragment.java";

    private RecyclerView recycler;
    private ItemRecyclerAdapter itemRecyclerAdapter;
    private List<Item> items;

    private FirebaseDatabase db;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = view.findViewById(R.id.list);
        items = new ArrayList<Item>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(view.getContext());
        recycler.setLayoutManager(manager);

        itemRecyclerAdapter = new ItemRecyclerAdapter(items, view.getContext(), "shoppingList", null);
        recycler.setAdapter(itemRecyclerAdapter);
        db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("shoppingList");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    item.setKey(postSnapshot.getKey());
                    items.add(item);
                    Log.d(TAG, "ValueEventListener: added " + item.toString());
                    Log.d(TAG, "ValueEventListener: key " + item.getKey());
                }
                itemRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "ValueEventListener: failed to read from Firebase" );
            }
        });


    }

}