package edu.northeastern.a6_assignments.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import edu.northeastern.a6_assignments.R;
import edu.northeastern.a6_assignments.pojo.Users;

public class SignUpActivity extends AppCompatActivity {

    private EditText username;

    private EditText firstname;

    private EditText lastname;

    private DatabaseReference usersRef;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.unique_name);
        firstname = findViewById(R.id.first_name);
        lastname = findViewById(R.id.last_name);

        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public void onSubmit(View view) {
        String usernameText = username.getText().toString().trim();
        String firstnameText = firstname.getText().toString().trim();
        String lastnameText = lastname.getText().toString().trim();

        if (usernameText.isEmpty() || firstnameText.isEmpty() || lastnameText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        usersRef.child(usernameText).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (currentData.getValue()!=null) {
                    return Transaction.abort();
                } else {
                    Users newUser = new Users(firstnameText, lastnameText);
                    currentData.setValue(newUser);
                    return Transaction.success(currentData);
                }
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    Toast.makeText(SignUpActivity.this,
                            "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (committed) {
                    Toast.makeText(SignUpActivity.this,
                            "User created successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, StickerAppHomeActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("username", usernameText);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this,
                            "Username already taken. Please choose another.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClickSignIn(View view){
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
    private void clearFields() {
        username.setText("");
        firstname.setText("");
        lastname.setText("");
    }
}
