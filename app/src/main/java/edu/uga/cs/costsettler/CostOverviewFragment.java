package edu.uga.cs.costsettler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
public class CostOverviewFragment extends Fragment {
    private final String TAG = "CostOverviewFragment";
    List<Purchase> purchases;
    List<String> users;
    public CostOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cost_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.infoText);
        String text = "Loading...";
        textView.setText(text);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("purchases");
        purchases = new ArrayList<Purchase>();

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
                if (purchases.isEmpty()) {
                    String text = "No purchases found";
                    textView.setText(text);
                } else {
                    users = new ArrayList<String>();
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("users");
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            users.clear();
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                String user = postSnapshot.getValue(String.class);
                                users.add(user);
                                Log.d(TAG, "ValueEventListener: added " + user);
                            }
                            textView.setText(getInfo());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "getting users failed");
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        Button button = view.findViewById(R.id.settleButton);

        button.setOnClickListener(v -> {
            ref.removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "purchases cleared from db");
                            Toast.makeText(getContext(), "Cost Settled", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "purchases failed to clear from db");
                            Toast.makeText(getContext(), "Cost unable to Settle", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private String getInfo() {
        List<Double> costs = new ArrayList<Double>();
        for (int i = 0; i < users.size(); i++) {
            costs.add(0.0);
        }

        for (int i = 0; i < purchases.size(); i++) {
            Purchase temp = purchases.get(i);
            for (int x = 0; x < users.size(); x++) {
                if (temp.getUser().equals(users.get(x))) {
                    double cost = costs.get(x);
                    costs.set(x, (cost += temp.getCost()));
                }
            }
        }
        double total = 0.0;

        for (int i = 0; i < costs.size(); i++) {
            total += costs.get(i);
        }

        String info = "Total Spent = " + total + "\n\n";
        for (int i = 0; i < users.size(); i++) {
            info = info + users.get(i) + " Spent = " + costs.get(i) + "\n";
        }
        info = info + "\nAverage Spent = " + (total/users.size()) + "\n\n";

        for (int i = 0; i < users.size(); i++) {
            double average = total/users.size();
            if (average <= costs.get(i)) {
                info = info + users.get(i) + " Spent " + (costs.get(i) - average) + " Above Average\n";
            } else {
                info = info + users.get(i) + " Spent " + (average - costs.get(i)) + " Below Average\n";
            }
        }

        return info;
    }

}