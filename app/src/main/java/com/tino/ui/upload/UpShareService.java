package com.tino.ui.upload;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;

import com.tino.core.ActionCallback;
import com.tino.core.net.UploadImp;
import com.tino.utils.CommonUtils;
import com.tino.utils.PhotoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class UpShareService extends Service {
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        final String id= CommonUtils.getUid(this);
        final String token= CommonUtils.gettoken(this);
        final List<String> picPaths = intent.getStringArrayListExtra("pics");
        final String text = intent.getStringExtra("text");
        final String title=intent.getStringExtra("title");
        new Thread(new Runnable() {

            public void run() {
                for (String pic : picPaths) {
                    System.out.println(pic);
                    try {
                        PhotoUtil.compressImage(pic, new File(pic).getName());
                    } catch (FileNotFoundException e) {
                        UpShareService.this.stopSelf();
                        Toast.makeText(UpShareService.this, "文件不存在", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
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
                        final String sig=tempsig;
                        final SparseArray<String> keyMap = new SparseArray();
                        for (int i = 0; i < picPaths.size(); i++) {
                            final String name = "share_" + id + "_" + System.currentTimeMillis() + ".png";
                            final int position = i;
                            UploadImp.SaveImageInCosServer(sig, PhotoUtil.bitmapTobyte(picPaths.get(position)), tempurl + name, new ActionCallback<String>() {
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

                                    String accessurl="";
                                    try {
                                        JSONObject   jsonObj = new JSONObject(s);
                                        JSONObject config = jsonObj.getJSONObject("data");
                                        accessurl=config.getString("access_url").replace(".file.",".picsh.");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    keyMap.put(position, accessurl);
                                    if (keyMap.size() >= picPaths.size()) {
                                        JSONArray jsonArray = new JSONArray();
                                        for (int i = 0; i < keyMap.size(); i++) {
                                            jsonArray.add(keyMap.valueAt(i));
                                        }
                                        final String token = getSharedPreferences("login_data", 0).getString("token", "");
                                        /*图片上传完成   开始帖子文字和相关图片的url*/
                                        UploadImp.uploadblog(token, id, title, text, jsonArray.toJSONString(), new ActionCallback<String>() {
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
                                                Toast.makeText(UpShareService.this, "恭喜,发布成功!^_^", Toast.LENGTH_SHORT).show();
                                                UpShareService.this.stopSelf();
                                                Intent intent = new Intent();
                                                intent.setAction("tino.reflash_blog");
                                                sendBroadcast(intent);
                                            }
                                        });
                                    }
                                }
                            });

                        }
                    }
                });



        }}).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
