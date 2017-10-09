package com.tino.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lling.photopicker.PhotoPickerActivity;
import com.tencent.TIMCallBack;
import com.tino.MainActivity;
import com.tino.R;
import com.tino.core.AccountImpl;
import com.tino.core.ActionCallback;
import com.tino.core.model.User;
import com.tino.core.net.UploadImp;
import com.tino.im.utils.SDKmanager;
import com.tino.ui.login.WelComeActivity;
import com.tino.utils.CommonUtils;
import com.tino.utils.Constants;
import com.tino.views.BooDialog;
import com.tino.views.CircleImageView;
import com.tino.views.BooDialog;
import com.tino.views.BooDialog.BooBuilder;
import com.tino.views.BooDialog.BooBuilder.SingleCheckBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-8-1.
 */

public class EditMeActivity extends Activity {
    private CircleImageView circleImageView;
    private String avatarUrl="http://tino-1254096761.picsh.myqcloud.com/avatar_18821709653_1502534972108.png";
    TextView tv_gender;
    String grade="1";
    String school="0",institute="0",profession="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_edit0);
        init();
    }
    private void init(){
        circleImageView=(CircleImageView)findViewById(R.id.avatar_set);
        Glide.with(this)
                .load(avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(circleImageView);
        Spinner spinner1  = (Spinner) findViewById(R.id.spinner_school);
        // 建立数据源
        List<String> list = new ArrayList<>();
        list.add("陕西科技大学");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,

                R.layout.mspinner,list);
        spinner1.setAdapter(adapter);


        Spinner spinner_grade  = (Spinner) findViewById(R.id.spinner_grade);
        // 建立数据源
        adapter = new ArrayAdapter<>(this,

                R.layout.mspinner,Constants.garde);
        spinner_grade.setAdapter(adapter);
        spinner_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               grade=i+"";
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

            });

        Spinner spinner2= (Spinner)findViewById(R.id.spinner_institute);
        adapter = new ArrayAdapter<String>(this,

                R.layout.mspinner, Constants.xueyuan);
        spinner2.setAdapter(adapter);

        spinner2.setSelection(0,true);

        final Spinner spinner3=(Spinner)findViewById(R.id.spinner_profession);
        adapter = new ArrayAdapter<>(this,

                R.layout.mspinner, Constants.zhuanye[0]);
        spinner3.setAdapter(adapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                institute=""+i;
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditMeActivity.this,   R.layout.mspinner, Constants.zhuanye[i]);
                spinner3.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               profession=""+i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tv_gender=(TextView)findViewById(R.id.tv_gender);

        findViewById(R.id.avatar_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPickerActivity.showWithParams(EditMeActivity.this, true, 0, 1);
            }
        });
        findViewById(R.id.bt_select_gender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGender();
            }
        });
        (findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BooDialog booDialog = new BooDialog.BooBuilder(EditMeActivity.this).title("正在修改").progressMode().setMessage("请稍候...").show(getFragmentManager());
                final String name=((TextView)findViewById(R.id.et_username)).getText().toString();
                String signature= ((TextView)findViewById(R.id.et_signature)).getText().toString();
                final String token=CommonUtils.gettoken(EditMeActivity.this);
                final String uid=CommonUtils.getUid(EditMeActivity.this);
                AccountImpl.addinf(token,uid,avatarUrl,tv_gender.getText().toString(),name,
                        signature,school,institute,grade,profession,new ActionCallback<String>(){

                            @Override
                            public void inProgress(float f) {

                            }

                            @Override
                            public void onAfter() {

                            }

                            @Override
                            public void onBefore() {

                            }

                            @Override
                            public void onError() {

                            }

                            @Override
                            public void onFailure(String str, String str2) {

                            }

                            @Override
                            public void onSuccess(String s) {
                                SDKmanager.setMyFaceUrl(avatarUrl, new TIMCallBack() {
                                    @Override
                                    public void onError(int i, String s) {

                                    }

                                    @Override
                                    public void onSuccess() {

                                    }
                                });
                                SDKmanager.setMyNick(name, new TIMCallBack() {
                                    @Override
                                    public void onError(int i, String s) {

                                    }

                                    @Override
                                    public void onSuccess() {

                                    }
                                });
                                AccountImpl.getinfo(uid, token, uid, new ActionCallback<User>() {
                                    @Override
                                    public void inProgress(float f) {

                                    }

                                    @Override
                                    public void onAfter() {

                                    }

                                    @Override
                                    public void onBefore() {

                                    }

                                    @Override
                                    public void onError() {

                                    }

                                    @Override
                                    public void onFailure(String str, String str2) {

                                    }

                                    @Override
                                    public void onSuccess(User user) {
                                        com.tino.core.api.Constants.user=user;
                                        MainActivity.NavtoMain(EditMeActivity.this, user);
                                        finish();
                                    }
                                });
                            }
                        });
            }
        });

    }



    void selectGender() {
        BooDialog.BooBuilder.SingleCheckBuilder builder = new BooBuilder(this).title("选择性别").singleCheckMode();
        RadioGroup group = builder.group;
        final BooDialog dialog = builder.show(getFragmentManager());
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_boy) {
                    EditMeActivity.this.tv_gender.setText("男");
                } else {
                    EditMeActivity.this.tv_gender.setText("女");
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == -1) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                if (result != null && result.size() > 0) {
                    cropImageUri(Uri.parse("file://" + (result.get(0))), 200, 200, 100);
                }
            }
        }
        else if (requestCode == 100) {
            System.out.print("try to get the data");
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (bmp != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                final byte[] byteArray = baos.toByteArray();
                UploadImp.getcosSigFromServer(new ActionCallback<String>() {
                    @Override
                    public void inProgress(float f) {
                    }
                    @Override
                    public void onAfter() {
                    }
                    @Override
                    public void onBefore() {
                    }
                    @Override
                    public void onError() {
                    }
                    @Override
                    public void onFailure(String str, String str2) {
                    }
                    @Override
                    public void onSuccess(String s) {
                        String tempsig="";
                        String tempurl="";
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            tempsig=jsonObject.getString("sig");
                            tempurl=jsonObject.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final String name="avatar_"+CommonUtils.getUid(EditMeActivity.this)+"_"+System.currentTimeMillis()+".png";
                        UploadImp.SaveImageInCosServer(tempsig, byteArray, tempurl + name, new ActionCallback<String>() {


                            @Override
                            public void inProgress(float f) {

                            }

                            @Override
                            public void onAfter() {

                            }

                            @Override
                            public void onBefore() {

                            }

                            @Override
                            public void onError() {

                            }

                            @Override
                            public void onFailure(String str, String str2) {

                            }

                            @Override
                            public void onSuccess(String s) {
                            try {
                                    JSONObject   jsonObj = new JSONObject(s);
                                    JSONObject config = jsonObj.getJSONObject("data");
                                    avatarUrl=config.getString("access_url").replace(".file.",".picsh.");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.with(EditMeActivity.this).load(avatarUrl).error(R.mipmap.avatar_default)
                                                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(circleImageView);
                                    }
                                });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        }
    }
    Uri uritempFile;
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP",null);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("output", uri);
        uritempFile  = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, requestCode);
    }

}
