package edu.uga.cs.costsettler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ViewItemsActivity extends AppCompatActivity {
    private final String TAG = "ViewItemsActivity.java";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        TextView text = findViewById(R.id.user);
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    Log.d(TAG, "User is logged in");
                    String email = currentUser.getEmail();
                    text.setText("User " + email);
                } else {
                    Log.d(TAG, "User should not have access to this.");
                    Intent intent = new Intent(ViewItemsActivity.this, MainActivity.class);
                    startActivity(intent);
                }


            }
        });
        logoutButton.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(ViewItemsActivity.this, MainActivity.class);
            Log.d(TAG, "User is logged out.");
        });
    }
}