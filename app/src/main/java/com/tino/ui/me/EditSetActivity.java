package com.tino.ui.me;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lling.photopicker.PhotoPickerActivity;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversationType;
import com.tino.MainActivity;
import com.tino.R;
import com.tino.core.AccountImpl;
import com.tino.core.ActionCallback;
import com.tino.core.net.UploadImp;
import com.tino.im.utils.SDKmanager;
import com.tino.ui.message.ChatActivity;
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

public class EditSetActivity extends Activity {
    private CircleImageView circleImageView;
    String avatarUrl="";
    String grade="1";
    TextView tv_gender,tv_name,tv_sig;
    String school="0",institute="0",profession="0";
    static Activity preactivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_setting);
        init();
    }


    public static void navToActiivity(Activity activity,String gender,String avatar,String name,String sig){
        Intent intent = new Intent(activity, EditSetActivity.class);
        intent.putExtra("gender", gender);
        intent.putExtra("avatar", avatar);
        intent.putExtra("name",name);
        intent.putExtra("sig",sig);
        activity.startActivity(intent);
        preactivity=activity;
    }
    private void init(){
        avatarUrl=getIntent().getStringExtra("avatar");
        circleImageView=(CircleImageView)findViewById(R.id.avatar_set);
        Glide.with(this).load(avatarUrl)
               // .placeholder(R.mipmap.avatar_default).
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(circleImageView);
        Button button=(Button)findViewById(R.id.bt_contact_me);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(EditSetActivity.this, "", Toast.LENGTH_SHORT).show();
                ChatActivity.navToChat(EditSetActivity.this,"18821709653", TIMConversationType.C2C
                ,"http://tino-1254096761.picsh.myqcloud.com/avatar_18821709653_1502534972108.png","app管理员");
                finish();
            }
        });
        Spinner spinner1  = (Spinner) findViewById(R.id.spinner_school);
        // 建立数据源
        List<String> list = new ArrayList<>();
        list.add("陕西科技大学");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,

                R.layout.mspinner,list);
        spinner1.setAdapter(adapter);
        Spinner spinner_garde= (Spinner)findViewById(R.id.spinner_grade);
        adapter = new ArrayAdapter<>(this,

                R.layout.mspinner, Constants.garde);
        spinner_garde.setAdapter(adapter);
        spinner_garde.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                grade=i+"";
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });



        Spinner spinner2= (Spinner)findViewById(R.id.spinner_institute);
        adapter = new ArrayAdapter<>(this,

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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditSetActivity.this,   R.layout.mspinner, Constants.zhuanye[i]);
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

        tv_gender= ((TextView)findViewById(R.id.tv_gender));
        tv_gender.setText(getIntent().getStringExtra("gender"));

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPickerActivity.showWithParams(EditSetActivity.this, true, 0, 1);
            }
        });

        (findViewById(R.id.bt_exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtils.savetoken("",EditSetActivity.this);
                preactivity.finish();
                finish();

                SDKmanager.logout(new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(EditSetActivity.this, "退出帐号成功", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        (findViewById(R.id.bt_change_pwd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditSetActivity.this,ChangPwdActivity.class));

            }
        });
        tv_name=((TextView)findViewById(R.id.et_username));
                tv_name.setText(getIntent().getStringExtra("name"));
        tv_sig=((TextView)findViewById(R.id.et_signature));
        tv_sig.setText(getIntent().getStringExtra("sig"));
        (findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=tv_name.getText().toString();
                String signature= tv_sig.getText().toString();
                if(CommonUtils.isEmpty(name)){
                    Toast.makeText(EditSetActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(CommonUtils.isEmpty(signature)){
                    Toast.makeText(EditSetActivity.this, "签名不能为空", Toast.LENGTH_SHORT).show();

                }
                //if(avatarUrl==null&&avatarUrl.length()<2)avatarUrl=getIntent().getStringExtra("avatar");
                SDKmanager.setMyFaceUrl(avatarUrl.trim(), new TIMCallBack() {
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
                final BooDialog booDialog = new BooDialog.BooBuilder(EditSetActivity.this).title("正在修改").progressMode().setMessage("请稍候...").show(getFragmentManager());
                CommonUtils.saveAvatar(avatarUrl,EditSetActivity.this);
                AccountImpl.addinf(CommonUtils.gettoken(EditSetActivity.this),CommonUtils.getUid(EditSetActivity.this),avatarUrl,tv_gender.getText().toString(),name,
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
                                Intent intent = new Intent();
                                intent.setAction("tino.reflash_userinfo");
                                sendBroadcast(intent);
                                finish();
                            }
                        });
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
                        final String name="avatar_"+CommonUtils.getUid(EditSetActivity.this)+"_"+System.currentTimeMillis()+".png";
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
                                        Glide.with(EditSetActivity.this).load(avatarUrl).error(R.mipmap.avatar_default)
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
