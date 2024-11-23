package edu.uga.cs.costsettler;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItemFragment extends Fragment {
    private final String TAG = "AddItemFragment.java";
    private Bundle bundle;
    private View topView;

    public AddItemFragment() {
        //empty constructor
    }

    public static AddItemFragment newInstance() {
        AddItemFragment fragment = new AddItemFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText itemNameInput = view.findViewById(R.id.itemNameInput);
        EditText quantityInput = view.findViewById(R.id.quantityInput);
        Button addButton = view.findViewById(R.id.addItemButton);

        bundle = this.getArguments();
        topView = view;

        if(bundle != null) {
            itemNameInput.setText(bundle.getString("name"));
            quantityInput.setText(bundle.getString("quantity"));
        }

        addButton.setOnClickListener(view2 -> {
            String itemName = itemNameInput.getText().toString();
            int quantity = Integer.parseInt(quantityInput.getText().toString());
            Item item = new Item(itemName, quantity);
            Log.d(TAG, item.toString());
            addItem(item);
        });


    }

    public void addItem(Item item) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("shoppingList");
        if (bundle != null) {
            ref.child(bundle.getString("key")).setValue(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Item Edited");

                            AppCompatActivity activity = (AppCompatActivity) topView.getContext();
                            FragmentManager fragmentManager = activity.getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace( R.id.fragmentContainerView, new ShoppingListFragment()).commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Item was not added to Firebase database");

                            Toast.makeText(getContext(), "Item was not added to the shopping list.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            ref.push().setValue(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Item added to Firebase database");

                            Toast.makeText(getContext(), "Item added to the shopping list", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Item was not added to Firebase database");

                            Toast.makeText(getContext(), "Item was not added to the shopping list.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } //else


    }
}
