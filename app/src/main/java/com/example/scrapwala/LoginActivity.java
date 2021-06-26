package com.example.scrapwala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scrapwala.normal_user.HomeActivity_normal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView logoText,sloganText;
    private TextInputLayout user_phn,password;
    private static final String TAG = "Login Activity";
    private LinearLayout progressbar,main_view;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private Button newuser,login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pages_view();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                    startActivity(intent);
            }
        });
       // view();
      /*  firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
       if(user != null)
        {
            Toast.makeText(this,"Already Logged In",Toast.LENGTH_LONG).show();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(email.getText().toString().trim(),user_pass.getText().toString().trim());
            }
        });
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
        */
    }
    private void pages_view()
    {
        newuser=(Button)findViewById(R.id.NewUser);
        logo =(ImageView)findViewById(R.id.logo_icon);
        logoText=(TextView)findViewById(R.id.logo_name);
        sloganText=(TextView)findViewById(R.id.logan_name);
        user_phn=(TextInputLayout)findViewById(R.id.user_name);
        password=(TextInputLayout)findViewById(R.id.password);
        login_button=(Button)findViewById(R.id.login);
        progressbar=findViewById(R.id.progressbar);
        main_view=findViewById(R.id.main_view);
    }
    private Boolean validate_phone()
    {
        String val = user_phn.getEditText().getText().toString();

        if (val.isEmpty()) {
            user_phn.setError("Field cannot be empty");
            return false;
        }
        else if (val.length() !=10)
        {
            user_phn.setError("Phone Number Invalid");
            return false;
        }
        else
            {
            user_phn.setError(null);
            user_phn.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validate_pass() {
        String val = password.getEditText().getText().toString();

        if (val.isEmpty())
        {
            password.setError("Field cannot be empty");
            return false;

        }
        else
        {
            password.setError(null);
            password.setErrorEnabled(false);

            return true;
        }
    }
    private void loginUser()
    {
        if(!validate_pass() | !validate_phone())
        {
            return;
        }
        else
        {

            isUser();
        }
    }

    private void isUser()
    {
        login_button.setClickable(false);
        newuser.setClickable(false);
        user_phn.setClickable(false);
        password.setClickable(false);
        progressbar.setVisibility(View.VISIBLE);

        final String phn = user_phn.getEditText().getText().toString().trim();
        final String pass = password.getEditText().getText().toString().trim();
        Query phnQuery;
        Log.d(TAG,"PHN : "+phn);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Normal_Users");
        phnQuery = databaseReference.orderByChild("user_phn").equalTo(phn);
        phnQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG,"Snap shot : "+snapshot.getChildrenCount());
                if(snapshot.exists())
                {
                    user_phn.setError(null);
                    user_phn.setErrorEnabled(false);

                    String pass_db = snapshot.child(phn).child("user_pass").getValue(String.class);
                    if(pass_db.equals(pass))
                    {
                        String fullname = snapshot.child(phn).child("full_name").getValue(String.class);
                        String email = snapshot.child(phn).child("user_email").getValue(String.class);
                        String user_type = snapshot.child(phn).child("user_type").getValue(String.class);
                        password.setError(null);
                        password.setErrorEnabled(false);
                        if(user_type.equals("User"))
                        {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity_normal.class);
                            intent.putExtra("fullname",fullname);
                            intent.putExtra("email",email);
                            intent.putExtra("phn",phn);
                            intent.putExtra("password",pass_db);
                            startActivity(intent);
                            finishAffinity();
                        }
                        else if(user_type.equals("Ragman"))
                        {
                            Toast.makeText(getApplicationContext(),"Ragman is the Type",Toast.LENGTH_SHORT).show();
                        }
                        else if(user_type.equals("Admin"))
                        {
                            Toast.makeText(getApplicationContext(),"Welcome Lone Wolf",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        login_button.setClickable(true);
                        newuser.setClickable(true);
                        user_phn.setClickable(true);
                        password.setClickable(true);
                        progressbar.setVisibility(View.GONE);
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                }
                else
                {
                    login_button.setClickable(true);
                    newuser.setClickable(true);
                    user_phn.setClickable(true);
                    password.setClickable(true);
                    progressbar.setVisibility(View.GONE);
                    user_phn.setError("No such Phone No. Exist");
                    user_phn.requestFocus();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                login_button.setClickable(true);
                newuser.setClickable(true);
                user_phn.setClickable(true);
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    /*
    private void validate(String mail,String pass) {
        progressDialog.setMessage("Wait For Few Minutes !!!");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/
}
