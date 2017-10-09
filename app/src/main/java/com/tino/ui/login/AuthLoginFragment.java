package com.tino.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mob.MobSDK;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tino.MainActivity;
import com.tino.R;
import com.tino.core.AccountImpl;
import com.tino.core.ActionCallback;
import com.tino.core.TinoApplication;
import com.tino.core.model.User;
import com.tino.core.net.UploadImp;
import com.tino.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * Created by root on 17-8-1.
 */

public class AuthLoginFragment extends Fragment {
    private View rootView;
    TextView bt_auth;
    AppCompatEditText et_password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.rootView == null) {
            this.rootView = inflater.inflate(R.layout.activity_login_autocode, container, false);
            init();


        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return this.rootView;
    }
    private void init(){
      /*  SMSSDK.initSDK(getActivity(), "1fb6d62928273", "f88fb2f8be82eb8ca1a355baf4e7e101");*/

       final EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.setAskPermisionOnReadContact(true);
   /*     SMSSDK.registerEventHandler(eventHandler);*/
        et_password=(AppCompatEditText)rootView.findViewById(R.id.et_code);
        bt_auth=(TextView)rootView.findViewById(R.id.bt_auth);
        bt_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum=((AppCompatEditText)rootView.findViewById(R.id.et_phone)).getText().toString();
                if(!CommonUtils.isMobileNO(phoneNum)){
                    Toast.makeText(getActivity(), "手机号码格式有误", Toast.LENGTH_SHORT).show();
                    return ;
                }
                MobSDK.init(TinoApplication.getContext());
                SMSSDK.registerEventHandler(eventHandler);
                SMSSDK.getVerificationCode("86", phoneNum);
                et_password.setHint("输入验证码");
                bt_auth.setClickable(false);
                bt_auth.setText("(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {  break;}
                            try {Thread.sleep(1000);
                            } catch (InterruptedException e) {e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
            }
        });
        Button commit=(Button)rootView.findViewById(R.id.bt_login_im);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneNum=((AppCompatEditText)rootView.findViewById(R.id.et_phone)).getText().toString().trim();
                if(!CommonUtils.isMobileNO(phoneNum)){
                    Toast.makeText(getActivity(), "手机号码格式有误", Toast.LENGTH_SHORT).show();
                    return ;
                }

                  String code=((AppCompatEditText)rootView.findViewById(R.id.et_code)).getText().toString().trim();
                  if(code.length()==4){
                      (rootView.findViewById(R.id.bt_login_im)).setClickable(false);
                      Toast.makeText(getActivity(), "验证中，请稍后", Toast.LENGTH_SHORT).show();
                      AccountImpl.loginWithAuth(phoneNum, code, new ActionCallback<String>() {
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
                              int code;
                              try {
                                  JSONObject jsonObject = new JSONObject(s);
                                  code=jsonObject.getInt("code");
                                  if(code==100){
                                      User user= new Gson().fromJson(jsonObject.getString("obj"),User.class);
                                      MainActivity.NavtoMain(getActivity(),user);
                                  }
                                  else if(code==105||code==102){
                                      CommonUtils.savetoken(jsonObject.getString("token"),getActivity());
                                      CommonUtils.saveUid(phoneNum,getActivity());

                                      FragmentManager manager = getActivity().getSupportFragmentManager();
                                      FragmentTransaction transaction = manager.beginTransaction();
                                      PwdInputFragment fragment=new PwdInputFragment();
                                      transaction.add(R.id.fragment_container, fragment, "Inputpwd");
                                      transaction.commit();
                                  }
                                  else  if(code==101){
                                      (rootView.findViewById(R.id.bt_login_im)).setClickable(true);
                                      Toast.makeText(getActivity(), "验证码错误", Toast.LENGTH_SHORT).show();
                                  }

                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
                      });
                  }
                else Toast.makeText(getActivity(), "无效验证码", Toast.LENGTH_SHORT).show();
            }
        });
    }

    int i = 60;
    Handler handler = new Handler() {
        public void handleMessage(Message msg)
        {
            if (msg.what == -9) {
                bt_auth.setText("(" + i + ")");
            }
            else if (msg.what == -8) {
                bt_auth.setText("获取");
                bt_auth.setClickable(true);
                i = 60;
            }
            else {
                int event = msg.arg1;
                int result = msg.arg2;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        et_password.setHint("发送成功，请输入");
                    }
                }
            }
        }
    };


}
