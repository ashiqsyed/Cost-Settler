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

public class SignInActivity extends AppCompatActivity {
    private final String TAG = "SignInActivity.java";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText emailInput2 = findViewById(R.id.emailInput2);
        EditText passwordInput2 = findViewById(R.id.passwordInput2);
        Button signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(view -> {
            String email = emailInput2.getText().toString();
            String password = passwordInput2.getText().toString();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Sign in successful, email: " + email);
                                Intent intent = new Intent(SignInActivity.this, NavigationHostActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d(TAG, "Sign in failed");
                                Toast.makeText(SignInActivity.this, "An account with this email does not exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
}