package edu.northeastern.a6_assignments.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.a6_assignments.R;
import edu.northeastern.a6_assignments.pojo.Users;

/**
 * SignIn is an Android activity that handles user sign-in functionality. It sets up the user
 * interface and applies edge-to-edge display settings.
 */
public class SignInActivity extends AppCompatActivity {

  private EditText username;
  private DatabaseReference usersRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_sign_in);
    username = findViewById(R.id.usernameField);

    usersRef = FirebaseDatabase.getInstance().getReference().child("users");
  }

  public void onSubmit(View view) {
    String usernameText = username.getText().toString().trim();

    if (usernameText.isEmpty()) {
      Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
      return;
    }

    usersRef.child(usernameText).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {
          Toast.makeText(SignInActivity.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(SignInActivity.this, StickerAppHomeActivity.class);

          Bundle bundle = new Bundle();
          bundle.putString("username", usernameText);
          intent.putExtras(bundle);

          startActivity(intent);
          finish();
        } else {
          Toast.makeText(SignInActivity.this, "User not found, Sign Up instead?", Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(SignInActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void onClickSignUp(View view){
    Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
    startActivity(intent);
    finish();
  }

}