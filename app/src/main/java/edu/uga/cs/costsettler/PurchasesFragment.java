package edu.uga.cs.costsettler;

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
 * Use the {@link PurchasesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchasesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String TAG = "PurchaseFragment.java";

    private RecyclerView recycler;
    private ItemRecyclerAdapter itemRecyclerAdapter;
    private List<Purchase> purchases;

    private FirebaseDatabase db;

    public PurchasesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PurchasesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PurchasesFragment newInstance(String param1, String param2) {
        PurchasesFragment fragment = new PurchasesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchases, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = view.findViewById(R.id.purchases);
        purchases = new ArrayList<Purchase>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(view.getContext());
        recycler.setLayoutManager(manager);

        itemRecyclerAdapter = new ItemRecyclerAdapter(purchases, view.getContext());
        recycler.setAdapter(itemRecyclerAdapter);

        db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("purchases");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                purchases.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Purchase purchase = postSnapshot.getValue(Purchase.class);
                    purchase.setKey(postSnapshot.getKey());
                    purchases.add(purchase);
                    Log.d(TAG, "ValueEventListener: added " + purchase.toString());
                    Log.d(TAG, "ValueEventListener: key " + purchase.getKey());
                }
                itemRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "ValueEventListener: failed to read from firebase");
            }
        });

        Button settleCostButton = view.findViewById(R.id.settleCostButton);
        settleCostButton.setOnClickListener(view2 -> {
           Log.d(TAG, "purchases: " + purchases.toString());
        });
    }
}