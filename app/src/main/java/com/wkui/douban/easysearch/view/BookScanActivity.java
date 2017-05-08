package com.wkui.douban.easysearch.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jaeger.library.StatusBarUtil;
import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.Utils.CommonUtil;
import com.wkui.douban.easysearch.Utils.LogUtil;
import com.wkui.douban.easysearch.Utils.ThemeUtil;
import com.wkui.douban.easysearch.base.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by wkui on 2017/3/5.
 */

public class BookScanActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks,QRCodeView.Delegate{
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private QRCodeView mQRCodeView;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_scan);
        StatusBarUtil.setColor(this,ThemeUtil.transparentColor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.scan_toolbar);
        toolbar.setTitle(getResources().getText(R.string.scan_search_title));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
    }

    @Override
    protected void handleThemeChange() {

    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        LogUtil.i(TAG, "scan result:" + result);
        CommonUtil.handleScanResult(this,result);
    }


    @Override
    public void onScanQRCodeOpenCameraError() {
        LogUtil.e(TAG, "打开相机出错");
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
        if ( decodePhotoTask != null ){
            decodePhotoTask.cancel(true ) ;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_show_album,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.show_album:
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                //Toast.makeText(this,getResources().getString(R.string.scan_search_title),Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    private static DecodePhotoTask decodePhotoTask ;
    public static String picturePath;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mQRCodeView.showScanRect();

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
            LogUtil.d(TAG,"scan picturePath"+picturePath);
            decodePhotoTask = new DecodePhotoTask(this);
            decodePhotoTask.execute();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }


    private static class DecodePhotoTask extends AsyncTask<Void,Void,String> {
        private WeakReference<Context> weakReference;

        public DecodePhotoTask(Context context){
            weakReference = new WeakReference<Context>(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            return QRCodeDecoder.syncDecodeQRCode(picturePath);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            CommonUtil.handleScanResult(weakReference.get(),result);
        }
    }

}


