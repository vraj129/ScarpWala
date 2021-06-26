package com.example.scrapwala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputLayout full_name,user_email,user_phn,user_pass;
    RadioGroup radioGroup;
    RadioButton user_type_radio;
    private TextView already_login;
    private Button reg_button;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private static final String TAG = "Registration Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        radioGroup = (RadioGroup)findViewById(R.id.radio);
       views();
       reg_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                register_user();
           }
       });
       already_login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
               finishAffinity();
           }
       });
      /*  firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("UserInfo");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                {
                    String user_email = email.getText().toString().trim();
                    String user_pass = password.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                database.getReference("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(username.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(RegistrationActivity.this,"Registered",Toast.LENGTH_LONG).show();
                                    }
                                });


                            }
                            else
                            {
                                Toast.makeText(RegistrationActivity.this,"Registeration Failed !!!",Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }
                else
                {
                    Toast.makeText(RegistrationActivity.this,"Please Fill all the Fields",Toast.LENGTH_LONG).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });
*/
    }
    private void views()
    {
        full_name = (TextInputLayout)findViewById(R.id.name);
        user_email = (TextInputLayout)findViewById(R.id.email);
        user_phn = (TextInputLayout)findViewById(R.id.phone_no);
        user_pass = (TextInputLayout)findViewById(R.id.password);
        reg_button = (Button)findViewById(R.id.reg_button);
        already_login = (TextView)findViewById(R.id.login_already);
    }
    public void checkButton(View v)
    {
        int radioid = radioGroup.getCheckedRadioButtonId();
        user_type_radio = findViewById(radioid);
        Toast.makeText(getBaseContext(),"Selected : "+user_type_radio.getText(),Toast.LENGTH_SHORT).show();
    }
    private Boolean validate_name()
    {
        String full_name1 = full_name.getEditText().getText().toString().trim();
        if(full_name1.isEmpty())
        {
            full_name.setError("Field Cannot Empty");
            return false;
        }
        else
        {
            full_name.setError(null);
            full_name.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validate_email()
    {
        String email = user_email.getEditText().getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(email.isEmpty())
        {
            user_email.setError("Field Cannot Empty");
            return false;
        }
        else if(!email.matches(emailPattern))
        {
            user_email.setError("Invalid email address");
            return false;
        }
        else
        {
            user_email.setError(null);
            user_email.setErrorEnabled(false);
            return true;
        }
    }

    private  Boolean validate_phn()
    {
        String val = user_phn.getEditText().getText().toString();

        if (val.isEmpty()) {
            user_phn.setError("Field cannot be empty");
            return false;
        }
        else if (val.length() != 10)
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
        String val = user_pass.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$";

        if (val.isEmpty()) {
            user_pass.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            user_pass.setError("Password is too weak");
            user_pass.setHelperText("It should contain 1 special character and its length at least 8");
            return false;
        } else {
            user_pass.setError(null);
            user_pass.setErrorEnabled(false);
            user_pass.setHelperTextEnabled(false);
            user_pass.setHelperText(null);
            return true;
        }
    }

    private void register_user()
    {
        if(!validate_name() | !validate_email() | !validate_phn() | !validate_pass())
        {
            return;
        }

        int radioid = radioGroup.getCheckedRadioButtonId();
        user_type_radio = findViewById(radioid);
        database = FirebaseDatabase.getInstance();
        final String full_name1 = full_name.getEditText().getText().toString().trim();
        final String email = user_email.getEditText().getText().toString().trim();
        final String phn = user_phn.getEditText().getText().toString().trim();
        final String pass = user_pass.getEditText().getText().toString().trim();
        final String type = user_type_radio.getText().toString();
        Query phnQuery;
        databaseReference = database.getReference("Normal_Users");
        phnQuery = FirebaseDatabase.getInstance().getReference("Normal_Users").orderByChild("user_phn").equalTo(phn);
        phnQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG,"Snap shot : "+snapshot.getChildrenCount());
                if(snapshot.getChildrenCount()>0)
                {
                    //Toast.makeText(RegistrationActivity.this,"Phone Number Exists ",Toast.LENGTH_SHORT).show();
                    user_phn.setError("Phone Number Already Exists");
                }
                else
                {
                    user_phn.setError(null);
                    user_phn.setErrorEnabled(false);
                    UserHelperClass helperClass = new UserHelperClass(full_name1,email,phn,pass,type);
                    databaseReference.child(phn).setValue(helperClass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}