package com.advantal.mt.picker.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.mt.picker.Adapter.GridViewSupplyAdapter;
import com.advantal.mt.picker.Adapter.VideoGridViewSupplyAdapter;
import com.advantal.mt.picker.R;

import java.util.ArrayList;
import java.util.Objects;

public class FilePickerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int IMAGE_REQ_CODE = 1, VIDEO_REQ_CODE = 2, PERMISSION_READ_EXTERNAL_STORAGE = 100;
    private ArrayList<Uri> mImageUri = new ArrayList<>();
    private ArrayList<Uri> mVideoUri = new ArrayList<>();
    private RecyclerView mShowPickedImgView, mShowPickedVideoView;
    private GridViewSupplyAdapter supplyAdapter;
    private AlertDialog.Builder builder;
    private Button btnClick, btnClearAll;
    private VideoGridViewSupplyAdapter videoGridViewSupplyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);

        initView();

        askStoragePermission();
    }

    private void askStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select Image from Gallery",
                "Select Video from Gallery"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                selectImage();
                                break;
                            case 1:
                                selectVideo();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void initView() {
        Objects.requireNonNull(getSupportActionBar()).hide();
        builder = new AlertDialog.Builder(this);

        btnClick = (Button) findViewById(R.id.btnClick);
        btnClearAll = (Button) findViewById(R.id.btnClearAll);
        btnClick.setOnClickListener(this::onClick);
        btnClearAll.setOnClickListener(this::onClick);


        mShowPickedImgView = (RecyclerView) findViewById(R.id.rv_showPickedImg);
        mShowPickedVideoView = (RecyclerView) findViewById(R.id.rv_showPickedVideo);

        int numberOfColumns = 3;
        supplyAdapter = new GridViewSupplyAdapter(this, mImageUri);
        mShowPickedImgView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mShowPickedImgView.setAdapter(supplyAdapter);

        videoGridViewSupplyAdapter = new VideoGridViewSupplyAdapter(this, mVideoUri);
        mShowPickedVideoView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mShowPickedVideoView.setAdapter(videoGridViewSupplyAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                btnClick.setVisibility(View.GONE);
                mShowPickedVideoView.setVisibility(View.GONE);
                mShowPickedImgView.setVisibility(View.VISIBLE);
                assert data != null;
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int myNum = 0; myNum < count; myNum++) {
                        mImageUri.add(data.getClipData().getItemAt(myNum).getUri());
                    }
                    supplyAdapter.notifyDataSetChanged();
                } else {
                    Uri imgURI = data.getData();
                    mImageUri.add(imgURI);
                    supplyAdapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == VIDEO_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mShowPickedVideoView.setVisibility(View.VISIBLE);
                btnClick.setVisibility(View.GONE);
                mShowPickedImgView.setVisibility(View.GONE);
                assert data != null;
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int myNum = 0; myNum < count; myNum++) {
                        mVideoUri.add(data.getClipData().getItemAt(myNum).getUri());
                    }
                    videoGridViewSupplyAdapter.notifyDataSetChanged();
                } else {
                    Uri imgUri = data.getData();
                    mVideoUri.add(imgUri);
                    videoGridViewSupplyAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures: "), IMAGE_REQ_CODE);
    }

    public void selectVideo() {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("video/mp4");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select videos"), VIDEO_REQ_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("video/mp4");
            startActivityForResult(intent, VIDEO_REQ_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        builder.setMessage("Do you want to close this application?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Alert");
        alert.show();
    }

    private void handleTask() {
        mImageUri.clear();
        mVideoUri.clear();
        supplyAdapter.notifyDataSetChanged();
        videoGridViewSupplyAdapter.notifyDataSetChanged();
        btnClick.setVisibility(View.VISIBLE);
        mShowPickedVideoView.setVisibility(View.GONE);
        mShowPickedImgView.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClick:
                showPictureDialog();
                break;

            case R.id.btnClearAll:
                handleTask();
                break;

        }
    }
}