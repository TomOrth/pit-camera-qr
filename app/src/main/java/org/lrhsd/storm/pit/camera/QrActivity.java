package org.lrhsd.storm.pit.camera;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;

import de.greenrobot.event.EventBus;

/**
 * <p>Generates QR Code.  Copy and paste each year as there usually is no change</p>
 */
public class QrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        //Gets string
        String output = EventBus.getDefault().removeStickyEvent(String.class);
        //Assests for qr code
        Display disp = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        ByteArrayOutputStream code = QRCode.from(output).withSize(size.x-10,size.x-10).stream();
        byte[] byteArray = code.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView myImage = (ImageView) findViewById(R.id.qrImg);

        myImage.setMinimumHeight(size.y-10);
        myImage.setMinimumWidth(size.x-10);
        myImage.setMaxHeight(size.y-10);
        myImage.setMaxWidth(size.x-10);
        myImage.setImageBitmap(bmp);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Action to return to main page
     * @param v - View to pass to method
     */
    public void deleteAndExit(View v){
        new AlertDialog.Builder(this)
                .setTitle("Return to Main screen")
                .setMessage("Are you sure? Doing this will clear the QR code unique contents and this action cannot be undone")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent back = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(back);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}