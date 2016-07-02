package org.lrhsd.storm.pit.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    int count = 1;
    String mCurrentPhotoPath;
    private final static int REQUEST_TAKE_PHOTO = 6969;
    Uri outputFileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .into((ImageView) findViewById(R.id.img));

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory(), "Bot Images");
        ;
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    public void takePicture(View v) {

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Bot Images/";
        File newdir = new File(dir);
        newdir.mkdirs();

        count++;
        String file = dir + "work.jpg";
        File newfile = new File(file);

        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
    }
    public void generateQR(View v){

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode != RESULT_CANCELED) {
            Log.d("I hate this", data.getData().getPath());
            Bitmap bm = (Bitmap)data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            Log.d("base64", encodedImage);
            EventBus.getDefault().postSticky(encodedImage);
            Intent intent = new Intent(this, QrActivity.class);
            startActivity(intent);
        }

            else {
            Log.d("resultCode", String.valueOf(resultCode));
            Log.d("requestCode", String.valueOf(requestCode));
        }

    }
}
