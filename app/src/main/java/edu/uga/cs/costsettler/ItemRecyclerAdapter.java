package edu.uga.cs.costsettler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemHolder> {
    public final String TAG = "ItemRecyclerAdapter.java";

    private List<Item> items;
    private Context context;
    private FirebaseDatabase db = null;
    private DatabaseReference ref = null;

    public ItemRecyclerAdapter(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView price;
        TextView item;
        TextView quantity;
        Button deleteButton;
        Button editButton;

        public ItemHolder(View view) {
            super(view);
            price = view.findViewById(R.id.price);
            item = view.findViewById(R.id.item);
            quantity = view.findViewById(R.id.quantity);
            deleteButton = view.findViewById(R.id.deleteButton);
            editButton = view.findViewById(R.id.editButton);
        }
    }

    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        if (db == null) {
            db = FirebaseDatabase.getInstance();
        }
        return new ItemHolder(view);
    }

    public void onBindViewHolder(ItemHolder holder, int pos) {
        Item item = items.get(pos);
        double price = item.getPrice();
        String itemName = item.getItemName();
        int quantity = item.getQuantity();

        holder.price.setText(Double.toString(price));
        holder.item.setText(itemName);
        holder.quantity.setText(Integer.toString(quantity));
        holder.deleteButton.setOnClickListener(view -> {
            Log.d(TAG, "Delete item " + item.getItemName() + " key " + item.getKey());
            items.remove(pos);
            this.notifyItemRemoved(pos);
            ref = db.getReference().child("items").child(item.getKey());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "deleted item " + item.getItemName() + " key " + item.getKey());
                            Toast.makeText(context.getApplicationContext(), "Deleted Item", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "Failed to delete item " + item.getItemName() + " key " + item.getKey());
                    Toast.makeText(context.getApplicationContext(), "failed to delete item", Toast.LENGTH_SHORT).show();
                }
            });

        });
        holder.editButton.setOnClickListener(view -> {
           Log.d(TAG, "Edit item " + item.getItemName() + " key " + item.getKey());
           Log.d(TAG, "size: " + items.size());
        });
    }

    public int getItemCount() {
        return items.size();
    }
}
