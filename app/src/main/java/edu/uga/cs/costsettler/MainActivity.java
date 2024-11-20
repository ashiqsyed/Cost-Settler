package edu.uga.cs.costsettler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity.java";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button registrationButton = findViewById(R.id.registrationButton);
        Button signInButton = findViewById(R.id.signIn);
        mAuth = FirebaseAuth.getInstance();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) { //user is logged in
                    Log.d(TAG, "User should be logged in");
                    Intent intent = new Intent(MainActivity.this, ViewItemsActivity.class);
                    startActivity(intent);
                }
            }
        });
        registrationButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
            startActivity(intent);
        });

        signInButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), SignInActivity.class);
            startActivity(intent);
        });
    }
}