package com.example.scrapwala.normal_user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.StateSet;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scrapwala.R;
import com.example.scrapwala.ScrapInfo;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static android.util.Log.d;
import static android.util.Log.w;

public class UploadActivity extends AppCompatActivity {
    private Button cam_button,select_loc,post_scrap;
    private TextInputLayout scrap,weight,address,pin;
    private ImageView imageView;
    private CheckBox checking;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final String TAG = "Upload Activity";
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private String user_phn,latitude="",longitude="";
    private Uri mImageUri;
    private StorageReference mStorageRef;

    //private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mStorageRef = FirebaseStorage.getInstance().getReference("scrap_uploads");
        page_views();
        Intent intent = getIntent();
        user_phn = intent.getStringExtra("user_phn");

        post_scrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_scrap();
            }
        });
    }

    private void upload_scrap() {
        if(!checkPic() | !checkScrapName() | !checkWeight() | !checkAddress() | !checkPincode() | !checkLoaction())
        {
            return;
        }
        final String scrapName = scrap.getEditText().getText().toString().trim();
        final String scrapWeight = weight.getEditText().getText().toString().trim();
        final String scrapAddress = address.getEditText().getText().toString().trim();
        final String scrapPincode = pin.getEditText().getText().toString().trim();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("ScrapInfo");
        final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
        +"."+getFileExtension(mImageUri));
        fileReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri>then(@NonNull Task<UploadTask.TaskSnapshot> task)throws Exception
            {
                if(!task.isSuccessful())
                {
                    throw task.getException();

                }
                else
                {
                    return  fileReference.getDownloadUrl();
                }
            }
        })
        .addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    Uri downloaduri = task.getResult();
                    Log.d(TAG,"Uri : "+downloaduri.toString());
                    ScrapInfo scrap = new ScrapInfo(scrapName,downloaduri.toString(),scrapWeight,user_phn,scrapAddress,scrapPincode,latitude,longitude);
                     databaseReference.child(user_phn).child(databaseReference.push().getKey()).setValue(scrap);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity_normal.class);
                    intent.putExtra("phn",user_phn);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });
       // ScrapInfo scrap = new ScrapInfo(scrapName,"123",scrapWeight,user_phn,scrapAddress,scrapPincode,latitude,longitude);
       // databaseReference.child(user_phn).setValue(scrap);

    }
    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void takePicture(View view)
    {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(imageTakeIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(imageTakeIntent,REQUEST_IMAGE_CAPTURE);
        }
    }
    public void selecLocation(View view)
    {
        PlacePicker.IntentBuilder  builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(UploadActivity.this)
                    ,PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            mImageUri = getImageUri(getBaseContext(),bitmap);
            imageView.setImageBitmap(bitmap);
        }
        else if(requestCode == PLACE_PICKER_REQUEST )
        {
            if( resultCode == RESULT_OK)
            {
                Place place = PlacePicker.getPlace(data, UploadActivity.this);
                StringBuilder stringBuilder = new StringBuilder();
                 latitude = String.valueOf(place.getLatLng().latitude);
                 longitude = String.valueOf(place.getLatLng().longitude);
                d(TAG,"Latitude : " + latitude);
                d(TAG,"Longitude : " + longitude);
                if(!latitude.isEmpty() && !longitude.isEmpty())
                {
                    checking.setChecked(true);
                }
            }
        }

    }
    private Boolean checkPic()
    {
        if(imageView.getDrawable() == null)
        {
            Toast.makeText(UploadActivity.this,"Please upload Image !!!!",Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            return true;
        }
    }
    private Boolean checkScrapName()
    {
        String val = scrap.getEditText().getText().toString().trim();
        if(val.isEmpty())
        {
            scrap.setError("Field Cannot be empty");
            return false;
        }
        else if(val.length()>15)
        {
            scrap.setError(null);
            scrap.setError("Name too Long");
            return false;
        }
        else
        {
            scrap.setError(null);
            scrap.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean checkWeight()
    {
        String val = weight.getEditText().getText().toString().trim();

        if(val.isEmpty())
        {
            weight.setError(null);
            weight.setError("Field Cannot be empty");
            return false;
        }
        else if(Integer.parseInt(val)<0)
        {
            weight.setError(null);
            weight.setError("Please Enter a Valid Weight");
            return false;
        }
        else if(Integer.parseInt(val)> 100)
        {
            weight.setError(null);
            weight.setError("Please Enter weight less than 100 Kg");
            return false;
        }
        else
        {
            weight.setError(null);
            weight.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean checkAddress()
    {
        String val = address.getEditText().getText().toString().trim();
        if(val.isEmpty()) {
            address.setError(null);
            address.setError("Field Cannot be empty");
            return false;
        }
        else if(val.length()>30)
        {
            address.setError(null);
            address.setError("Address too Long");
            return false;
        }
        else
        {
            address.setError(null);
            address.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean checkPincode()
    {
        String val = pin.getEditText().getText().toString().trim();
        if(val.isEmpty())
        {
            pin.setError(null);
            pin.setError("Field Cannot be empty");
            return false;
        }

        else if(val.length() != 6)
        {
            pin.setError(null);
            pin.setError("Enter a valid Pincode");
            return false;
        }
        else
        {
            pin.setError(null);
            pin.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean checkLoaction()
    {
        if(latitude.isEmpty() && longitude.isEmpty())
        {
            Toast.makeText(UploadActivity.this,"Please Select Location !!!",Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            return true;
        }
    }

    private void page_views()
    {
        cam_button = (Button)findViewById(R.id.open_cam);
        imageView = (ImageView)findViewById(R.id.imageview);
        select_loc = (Button)findViewById(R.id.loc_button);
        checking = (CheckBox)findViewById(R.id.checking);
        post_scrap = (Button)findViewById(R.id.post_scrap);
        scrap = (TextInputLayout)findViewById(R.id.name_scrap);
        weight = (TextInputLayout)findViewById(R.id.weight);
        address = (TextInputLayout)findViewById(R.id.scrap_address);
        pin = (TextInputLayout)findViewById(R.id.pincode);
    }
}