package edu.uga.cs.costsettler;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemHolder> {
    public final String TAG = "ItemRecyclerAdapter.java";

    private View topView;
    private List<Item> items;
    private Context context;
    private FirebaseDatabase db = null;
    private DatabaseReference ref = null;
    private String path;
    private List<Purchase> purchases;
    private String key;

    public ItemRecyclerAdapter(List<Item> items, Context context, String path, String key) {
        this.items = items;
        this.context = context;
        this.path = path;
        this.key = key;
    }

    public ItemRecyclerAdapter(List<Purchase> purchases, Context context) {
        this.purchases = purchases;
        this.context = context;
//        this.path = path;
        this.path = "purchases";
        key = null;
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView itemView;
        Button deleteButton;
        Button editButton;
        Button addButton;

        public ItemHolder(View view, String path) {
            super(view);
            if(path.equals("shoppingList")) {
                itemView = view.findViewById(R.id.item);
                deleteButton = view.findViewById(R.id.deleteButton);
                editButton = view.findViewById(R.id.editButton);
                addButton = view.findViewById(R.id.addCButton);
                topView = view;
            } else  {
                itemView = view.findViewById(R.id.textItem);
                deleteButton = view.findViewById(R.id.removeButton);
                editButton = view.findViewById(R.id.changeButton);
                topView = view;
            }
        }
    }

    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (path.equals("shoppingList")) {
             view = LayoutInflater.from(parent.getContext()).
                     inflate(R.layout.item, parent, false);
        } else if (path.equals("shoppingCart")) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_v2, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_v2, parent, false);
        }
        if (db == null) {
            db = FirebaseDatabase.getInstance();
        }
        return new ItemHolder(view, path);
    }

    public void onBindViewHolder(ItemHolder holder, int pos) {
//        Item item = new Item();
//        String itemString = "";
//        Purchase purchase;
//        String purchaseString = "";
//        if (items != null) {
//            item = items.get(pos);
//             itemString = item.toString();
//        }
//        if (purchases != null) {
//            purchase = purchases.get(pos);
//            purchaseString = purchase.toString();
//        }

        if (items != null) {
            Item item = items.get(pos);
            String itemString = item.toString();
            holder.itemView.setText(itemString);

            holder.deleteButton.setOnClickListener(view -> {
                Log.d(TAG, "Delete item " + item.getItemName() + " key " + item.getKey());
                ref = db.getReference().child(path).child(item.getKey()); //get reference to item being clicked
                if(path.equals("shoppingList")) { //action if clicked in shoppinglist recycler
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
                } else if (path.equals("shoppingCart")) {
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "deleted item " + item.getItemName() + " key " + item.getKey());
                                    item.setKey(null);
                                    ref = db.getReference("shoppingList");
                                    ref.push().setValue(item)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "Item added to Firebase database");
                                                    Toast.makeText(context.getApplicationContext(), "Item removed from the shopping cart", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "Item was not added to Firebase database");
                                                    Toast.makeText(context.getApplicationContext(), "Failed to remove item from the shopping cart.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "Failed to delete item " + item.getItemName() + " key " + item.getKey());
                        }
                    });
                } else {
                    Log.d(TAG, "Delete purchase");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Removed purchase");
                                    Toast.makeText(context, "Removed Purchase", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to remove purchase");
                                    Toast.makeText(context, "Could not remove purchase", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "error");
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            });
            //TODO fix bug: when clicking add item after pressing edit, it adds duplicate item to shoppinglist
            holder.editButton.setOnClickListener(view -> {
                Fragment fragment = new AddItemFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", item.getItemName());
                bundle.putString("key", item.getKey());
                bundle.putString("quantity", Integer.toString(item.getQuantity()));
                fragment.setArguments(bundle);
                Log.d(TAG, "Edit item " + item.getItemName() + " key " + item.getKey());
                Log.d(TAG, "size: " + items.size());
                Log.d(TAG, "TESTING TESTING TESTING");

                AppCompatActivity activity = (AppCompatActivity) topView.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager.beginTransaction().replace( R.id.fragmentContainerView, fragment).commit();
            });

            if (path.equals("shoppingList")) {
                holder.addButton.setOnClickListener(view -> {
                    Log.d(TAG, "Move item " + item.getItemName() + " key " + item.getKey());
                    ref = db.getReference().child(path).child(item.getKey());
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "deleted item " + item.getItemName() + " key " + item.getKey());
                                    item.setKey(null);
                                    ref = db.getReference("shoppingCart");
                                    ref.push().setValue(item)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "Item added to Firebase database");
                                                    Toast.makeText(context.getApplicationContext(), "Item added to the shopping cart", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "Item was not added to Firebase database");
                                                    Toast.makeText(context.getApplicationContext(), "Failed to add item to the shopping cart.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "Failed to delete item " + item.getItemName() + " key " + item.getKey());
                        }
                    });
                });
            }


        }
        if (purchases != null) {
            Log.d(TAG, "display purchases");
            Purchase purchase = purchases.get(pos);
            String purchaseString = purchase.toString();

            holder.itemView.setText(purchaseString);
            if (path.equals("purchases")) {
                Log.d(TAG, "set buttons to purchases actions");
                holder.deleteButton.setOnClickListener(view -> {
//                   Log.d(TAG, "Delete purchase with key " + purchase.getKey() + " with items " + purchases.get(pos).toString());
                    ref = db.getReference().child(path).child(purchase.getKey());
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Purchase removed successfully");
                                    Toast.makeText(context, "Purchase removed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "Purchase not removed");
                            Toast.makeText(context, "Could not remove the purchase", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                holder.editButton.setOnClickListener(view -> {
                   Fragment fragment = new ShoppingCartFragment();
                   Bundle bundle = new Bundle();
                   bundle.putString("purchaseKey", purchase.getKey());
                   Log.d(TAG, "edit purchase " + purchase.toString());
                   fragment.setArguments(bundle);
                   AppCompatActivity activity = (AppCompatActivity) topView.getContext();
                   FragmentManager fragmentManager = activity.getSupportFragmentManager();
                   fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
                });
            }
        }


//        holder.deleteButton.setOnClickListener(view -> {
//            Log.d(TAG, "Delete item " + item.getItemName() + " key " + item.getKey());
//            ref = db.getReference().child(path).child(item.getKey());
//            if(path.equals("shoppingList")) {
//
//
//                ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Log.d(TAG, "deleted item " + item.getItemName() + " key " + item.getKey());
//                                Toast.makeText(context.getApplicationContext(), "Deleted Item", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.d(TAG, "Failed to delete item " + item.getItemName() + " key " + item.getKey());
//                        Toast.makeText(context.getApplicationContext(), "failed to delete item", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else if (path.equals("shoppingCart")) {
//                ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Log.d(TAG, "deleted item " + item.getItemName() + " key " + item.getKey());
//                                item.setKey(null);
//                                ref = db.getReference("shoppingList");
//                                ref.push().setValue(item)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                Log.d(TAG, "Item added to Firebase database");
//                                                Toast.makeText(context.getApplicationContext(), "Item removed from the shopping cart", Toast.LENGTH_SHORT).show();
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.d(TAG, "Item was not added to Firebase database");
//                                                Toast.makeText(context.getApplicationContext(), "Failed to remove item from the shopping cart.", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                            }
//                        });
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.d(TAG, "Failed to delete item " + item.getItemName() + " key " + item.getKey());
//                    }
//                });
//            } else {
//                Log.d(TAG, "Delete purchase");
//                ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Log.d(TAG, "Removed purchase");
//                                Toast.makeText(context, "Removed Purchase", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "Failed to remove purchase");
//                                Toast.makeText(context, "Could not remove purchase", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.d(TAG, "error");
//                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//        });
//        holder.editButton.setOnClickListener(view -> {
//            Fragment fragment = new AddItemFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("name", item.getItemName());
//            bundle.putString("key", item.getKey());
//            bundle.putString("quantity", Integer.toString(item.getQuantity()));
//            fragment.setArguments(bundle);
//            Log.d(TAG, "Edit item " + item.getItemName() + " key " + item.getKey());
//            Log.d(TAG, "size: " + items.size());
//
//            AppCompatActivity activity = (AppCompatActivity) topView.getContext();
//            FragmentManager fragmentManager = activity.getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace( R.id.fragmentContainerView, fragment).commit();
//        });
//        if (path.equals("shoppingList")) {
//          holder.addButton.setOnClickListener(view -> {
//              Log.d(TAG, "Move item " + item.getItemName() + " key " + item.getKey());
//              ref = db.getReference().child(path).child(item.getKey());
//              ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                  @Override
//                  public void onDataChange(@NonNull DataSnapshot snapshot) {
//                      snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                          @Override
//                          public void onSuccess(Void unused) {
//                              Log.d(TAG, "deleted item " + item.getItemName() + " key " + item.getKey());
//                              item.setKey(null);
//                              ref = db.getReference("shoppingCart");
//                              ref.push().setValue(item)
//                                      .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                          @Override
//                                          public void onSuccess(Void unused) {
//                                              Log.d(TAG, "Item added to Firebase database");
//                                              Toast.makeText(context.getApplicationContext(), "Item added to the shopping cart", Toast.LENGTH_SHORT).show();
//                                          }
//                                      })
//                                      .addOnFailureListener(new OnFailureListener() {
//                                          @Override
//                                          public void onFailure(@NonNull Exception e) {
//                                              Log.d(TAG, "Item was not added to Firebase database");
//                                              Toast.makeText(context.getApplicationContext(), "Failed to add item to the shopping cart.", Toast.LENGTH_SHORT).show();
//                                          }
//                                      });
//                          }
//                      });
//                  }
//                  @Override
//                  public void onCancelled(@NonNull DatabaseError error) {
//                      Log.d(TAG, "Failed to delete item " + item.getItemName() + " key " + item.getKey());
//                  }
//              });
//          });
//        }
    }

    public int getItemCount() {
        if (items == null) {
            return purchases.size();
        }
        return items.size();
    }

    public String getPath() {return path;}
}
