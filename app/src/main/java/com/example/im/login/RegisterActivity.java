package com.example.im.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.im.Constant;
import com.example.im.R;
import com.example.im.utils.imageloaderutil.ImageLoaderFactory;
import com.example.im.utils.URIUtils;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.PicassoAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.List;

import static com.example.im.Constant.REQUEST_CAMERA;

public class RegisterActivity extends AppCompatActivity {
    private ImageView mIvAvatar;
    private EditText mEtUserName;
    private EditText mEtPassWord;
    File picFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        mIvAvatar = findViewById(R.id.iv_avatar);
        mEtUserName = findViewById(R.id.et_username);
        mEtPassWord = findViewById(R.id.et_password);
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicChooseDialog();
            }
        });
    }

    private void showPicChooseDialog() {
        final String[] items = {"从相册选择", "拍照选择", "取消"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(RegisterActivity.this);
        listDialog.setTitle("选择图片方式");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Toast.makeText(RegisterActivity.this, "相册", Toast.LENGTH_SHORT).show();
                    FishBun.with(RegisterActivity.this).setImageAdapter(new PicassoAdapter()).setMaxCount(1).setCamera(true).startAlbum();

                } else if (which == 1) {
                    Toast.makeText(RegisterActivity.this, "拍照", Toast.LENGTH_SHORT).show();
                    applyWritePermission();

                } else {
                    return;
                }
            }
        });
        listDialog.show();
    }


    public void applyWritePermission() {

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= 23) {
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                useCamera();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.REQUEST_CAMERA);
            }
        } else {
            useCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            useCamera();
        } else if (requestCode == Constant.REQUEST_CAMERA) {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }

    private void useCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        picFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/image/" + System.currentTimeMillis() + ".jpg");
        picFile.getParentFile().mkdirs();

        //改变Uri  和xml中的一致
        Uri uri = FileProvider.getUriForFile(RegisterActivity.this, "com.example.im.fileprovider", picFile);
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    List<Uri> path = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                    String realPath = URIUtils.getRealPathFromUri(RegisterActivity.this, path.get(0));
                    Log.d("TAG", realPath);
                    ImageLoaderFactory.getLoader().loadAvatorFromLocal(mIvAvatar, realPath, R.mipmap.head);
                    break;
                }
            case Constant.REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    Uri uri = FileProvider.getUriForFile(this, "com.example.im.fileprovider", picFile);
                    Log.d("TAG", picFile.getPath());
                    ImageLoaderFactory.getLoader().loadAvatorFromLocal(mIvAvatar, picFile.getPath(), R.mipmap.head);
                    break;
                }
        }
    }

}
