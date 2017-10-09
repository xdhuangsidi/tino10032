package com.tino.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.gson.Gson;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tino.MainActivity;
import com.tino.R;
import com.tino.core.AccountImpl;
import com.tino.core.ActionCallback;
import com.tino.core.model.User;
import com.tino.core.net.UploadImp;
import com.tino.utils.CommonUtils;
import com.tino.views.EText;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by root on 17-7-31.
 */

public class PwdLoginFragment extends Fragment {
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            this.rootView = inflater.inflate(R.layout.activity_login, container, false);
            init();
            return  rootView;

    }
    private void init(){
        Button button = (Button) rootView.findViewById(R.id.bt_mobileAuth);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                AuthLoginFragment fragment=new AuthLoginFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.fragment_container, fragment);
                transaction.addToBackStack("auth_fragment");
                transaction.commit();

            }
        });
        button=(Button)rootView.findViewById(R.id.bt_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "请先通过短信验证", Toast.LENGTH_SHORT).show();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                AuthLoginFragment fragment=new AuthLoginFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.fragment_container, fragment);
                transaction.addToBackStack("auth_fragment");
                transaction.commit();
            }
        });



        button=(Button)rootView.findViewById(R.id.bt_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password=((EText)rootView.findViewById(R.id.et_passwd)).getText().toString();
                final  String mobile=((AppCompatEditText)rootView.findViewById(R.id.et_account)).getText().toString().trim();
                if(!CommonUtils.isMobileNO(mobile)){
                    Toast.makeText(getActivity(), "手机号码格式有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<6){
                    Toast.makeText(getActivity(), "密码太短", Toast.LENGTH_SHORT).show();
                    return;
                }
                rootView.findViewById(R.id.bt_login).setClickable(false);
                AccountImpl.loginWithPwd(mobile, password, new ActionCallback<String>() {
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
                        CommonUtils.saveUid(mobile,getActivity());
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            code=jsonObject.getInt("code");
                            if(code==100){
                               User user= new Gson().fromJson(jsonObject.getString("obj"),User.class);
                                MainActivity.NavtoMain(getActivity(),user);

                            }

                            else{
                                rootView.findViewById(R.id.bt_login).setClickable(true);
                                Toast.makeText(getActivity(), "用户不存在或密码错误", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            rootView.findViewById(R.id.bt_login).setClickable(true);
                            Toast.makeText(getActivity(), "服务器错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


    }

}
