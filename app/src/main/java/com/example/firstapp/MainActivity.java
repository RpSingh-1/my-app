package com.example.firstapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
     private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button uploadbtn=findViewById(R.id.uplaod);
        Button copybtm=findViewById(R.id.copy);
        mTextView=findViewById(R.id.text);
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,100);

            }
        });
        copybtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String copiedText = mTextView.getText().toString();
                ClipboardManager clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData data= ClipData.newPlainText("Image Copied ",copiedText);
                Toast.makeText(getApplicationContext(),"Text Copied",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK) {
            try{
           FirebaseVisionImage image = FirebaseVisionImage
                   .fromFilePath(MainActivity.this, data.getData());
           FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
           recognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
               @Override
               public void onSuccess(FirebaseVisionText firebaseVisionText) {
                   String text =firebaseVisionText.getText();
                   mTextView.setText("");
                   for(FirebaseVisionText.TextBlock block:firebaseVisionText.getTextBlocks())
                   {
                       mTextView.append("\n \n"+block.getText());
                   }
               }
           });
       } catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}