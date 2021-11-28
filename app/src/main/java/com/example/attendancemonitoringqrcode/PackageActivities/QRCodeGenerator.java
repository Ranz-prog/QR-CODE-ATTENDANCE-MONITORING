package com.example.attendancemonitoringqrcode.PackageActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.attendancemonitoringqrcode.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;

public class QRCodeGenerator extends AppCompatActivity {


    private String toolbarTitle;
    private String qrCodeString;

    private Intent intent;

    private Toolbar toolbar;

    private ImageView imgQRCode;

    private Button btnDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_generator);

        intent = getIntent();
        toolbarTitle = intent.getStringExtra("ToolbarTitle");
        qrCodeString = intent.getStringExtra("QRCodeString");

        setUpToolbar(toolbarTitle);

        updateViews();

        generateQRCode(qrCodeString);

    }


    private void setUpToolbar(String title) {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

    }


    private void updateViews() {

        btnDone = findViewById(R.id.btnDone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }


    private void generateQRCode(String qrCodeString) {

        imgQRCode = findViewById(R.id.imgQRCode);
        
        try {

            imgQRCode.setImageBitmap(getQRCodeImage(qrCodeString, 350, 350));

        } catch (WriterException e) {

            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());

        } catch (IOException e) {

            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());

        }

    }


    private Bitmap getQRCodeImage(String text, int width, int height) throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

        return barcodeEncoder.createBitmap(bitMatrix);

    }


}
