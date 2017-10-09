package com.tino.ui.me;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tino.R;
import com.tino.core.AccountImpl;
import com.tino.core.ActionCallback;
import com.tino.core.TinoApplication;
import com.tino.core.net.UploadImp;
import com.tino.utils.CommonUtils;
import com.tino.views.EText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 17-8-1.
 */

public class ChangPwdFragment extends Fragment{
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.rootView = inflater.inflate(R.layout.activity_register_pwd, container, false);
        init();
        return  rootView;

    }
    private void init(){
        (rootView.findViewById(R.id.bt_register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd0=((EText)rootView.findViewById(R.id.et_passwd0)).getText().toString().trim();
                String pwd1=((EText)rootView.findViewById(R.id.et_passwd1)).getText().toString().trim();
                if(pwd0.length()<7||pwd1.length()<7){
                    Toast.makeText(getActivity(), "密码至少要7位", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwd0.length()!=pwd1.length()){
                    Toast.makeText(getActivity(), "两个密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                for(int i=0;i<pwd0.length();i++){
                    if(pwd0.charAt(i)!=pwd1.charAt(i)){
                        Toast.makeText(getActivity(), "两个密码不一致", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                    AccountImpl.changePwd(CommonUtils.getUid(getActivity()), CommonUtils.gettoken(getActivity()), pwd1, new ActionCallback<String>() {
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
                                   Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                                   getActivity().finish();
                                }

                                else  if(code==101){
                                    Toast.makeText(getActivity(), "发生错误，请重新通过验证码验证", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


            }
        });

    }
}
