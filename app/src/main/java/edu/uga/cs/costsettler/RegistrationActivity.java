package edu.uga.cs.costsettler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private final String TAG = "RegistrationActivity.java";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registered user: " + email, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "user created");
                                //TODO create intent to go to activity for viewing items
                                Intent intent = new Intent(view.getContext(), ViewItemsActivity.class);
                                startActivity(intent);
                            } else {
                                Log.w(TAG, "user failed to create", task.getException());
                                Toast.makeText(RegistrationActivity.this, "An account with this email already exists. Please enter another email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            Log.d(TAG, "User email: " + email + " and password: " + password);
        });
    }
}