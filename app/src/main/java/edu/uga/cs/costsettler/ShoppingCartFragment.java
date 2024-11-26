package edu.uga.cs.costsettler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCartFragment extends Fragment {
    private final String TAG = "ShoppingCartFragment.java";

    private RecyclerView recycler;
    private ItemRecyclerAdapter itemRecyclerAdapter;
    private List<Item> items;
    private Bundle bundle;
    private String key;
    private View topView;

    private FirebaseDatabase db;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bundle = this.getArguments();
        db = FirebaseDatabase.getInstance();
        recycler = view.findViewById(R.id.cart);
        items = new ArrayList<Item>();
        topView = view;

        RecyclerView.LayoutManager manager = new LinearLayoutManager(view.getContext());
        recycler.setLayoutManager(manager);

        Button purchaseButton = view.findViewById(R.id.purchaseButton);
        EditText costInput = view.findViewById(R.id.costInput);


        DatabaseReference ref = db.getReference("shoppingCart");
        if (bundle != null) {
            key = bundle.getString("key");
            Log.d(TAG, "currently editing a purchase");
            Log.d(TAG, "editing purchase with key " + key);
            DatabaseReference ref2 = db.getReference("purchases").child(key).child("itemsPurchased");
            itemRecyclerAdapter = new ItemRecyclerAdapter(items, view.getContext(), "shoppingCart", key);

            recycler.setAdapter(itemRecyclerAdapter);
            ref2.addValueEventListener(new ValueEventListener() {
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
                }
            });
            recycler.setAdapter(itemRecyclerAdapter);

            costInput.setText(Double.toString(bundle.getDouble("cost")));
        } else {
            itemRecyclerAdapter = new ItemRecyclerAdapter(items, view.getContext(), "shoppingCart", null);

            recycler.setAdapter(itemRecyclerAdapter);
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



        purchaseButton.setOnClickListener(view2 -> {
            DatabaseReference ref2;
            Purchase purchase;
            double cost = Double.parseDouble(costInput.getText().toString());
            if (bundle == null) {
                ref2 = db.getReference("purchases");
                purchase = new Purchase(items, cost, "test", new Date().toString());
                ref2.push().setValue(purchase)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "purchase added to database");
                                Toast.makeText(getContext(), "Purchase made", Toast.LENGTH_SHORT).show();
                                items.clear(); //clear list
                                itemRecyclerAdapter.notifyDataSetChanged();
                                ref.removeValue(); //removes it from firebase "shoppingcart"
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Purchase not added to database");
                                Toast.makeText(getContext(), "Purchase failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                ref2 = db.getReference("purchases").child(key);
                purchase = new Purchase(items, cost, bundle.getString("user"), bundle.getString("date"));
                ref2.setValue(purchase)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "purchase edited in database");
                                Toast.makeText(getContext(), "Purchase edited", Toast.LENGTH_SHORT).show();
                                AppCompatActivity activity = (AppCompatActivity) topView.getContext();
                                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                fragmentManager.popBackStack();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Purchase not edited in database");
                                Toast.makeText(getContext(), "Purchase edit failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    } //onViewCreated

}