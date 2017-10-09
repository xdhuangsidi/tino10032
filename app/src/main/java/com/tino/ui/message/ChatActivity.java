package com.tino.ui.message;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
//预留定位功能，暂不开发。由于arr已经打包百度地图相关功能的jar so包，删包重新编译需要耗费一定时间
/*import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;*/

import com.tencent.TIMConversationType;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageDraft;
import com.tencent.TIMMessageStatus;
import com.tino.R;
import com.tino.adapter.ChatAdapter;
import com.tino.core.model.CustomMessage;
import com.tino.core.model.FileMessage;
import com.tino.core.model.ImageMessage;
import com.tino.core.model.Message;
import com.tino.core.model.MessageFactory;
import com.tino.core.model.TextMessage;
import com.tino.core.model.VideoMessage;
import com.tino.core.model.VoiceMessage;
import com.tino.im.event.RefreshEvent;
import com.tino.im.presenter.ChatPresenter;
import com.tino.im.utils.FileUtil;
import com.tino.im.utils.MediaUtil;
import com.tino.im.utils.RecorderUtil;
import com.tino.im.view.ChatInput;
import com.tino.im.view.ChatView;
import com.tino.im.view.TemplateTitle;
import com.tino.im.view.VoiceSendingView;
import com.tino.ui.home.UserDetialActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends FragmentActivity implements ChatView {

    private static final String TAG = "ChatActivity";

    private List<Message> messageList = new ArrayList<>();
    private ChatAdapter adapter;
    protected LinearLayoutManager layoutManager;
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout refreshLayout;
    private ChatPresenter presenter;
    private ChatInput input;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int IMAGE_STORE = 200;
    private static final int FILE_CODE = 300;
    private static final int IMAGE_PREVIEW = 400;
    private static final int VIDEO_RECORD = 500;
    private Uri fileUri;
    private VoiceSendingView voiceSendingView;
    private String identify;
    private RecorderUtil recorder = new RecorderUtil();
    private TIMConversationType type;
    private String titleStr;
    private Handler handler = new Handler();


    public static void navToChat(Context context, String identify, TIMConversationType type){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("identify", identify);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public static void navToChat(Context context, String identify, TIMConversationType type,
                                 String senderfaceUrl,String Nickname){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("identify", identify);
        intent.putExtra("type", type);
        intent.putExtra("Nickname", Nickname);
        intent.putExtra("faceUrl", senderfaceUrl);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        identify = getIntent().getStringExtra("identify");
        String faceUrl=getIntent().getStringExtra("faceUrl");
        type = (TIMConversationType) getIntent().getSerializableExtra("type");
        presenter = new ChatPresenter(this, identify, type);
        input = (ChatInput) findViewById(R.id.input_panel);
        input.setChatView(this);

        adapter = new ChatAdapter(this,faceUrl,messageList);
        recyclerView=(RecyclerView)findViewById(R.id.list);
        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.fragment_chat_srl_pullrefresh) ;
        this.refreshLayout.setEnabled(false);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setAdapter(this.adapter);

        TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
        switch (type) {
            case C2C:{
         /*       title.setMoreImg(R.drawable.btn_person);
                title.setMoreImgAction(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserDetialActivity.navToDetail(ChatActivity.this,);
                    }
                });*/
                    titleStr =  getIntent().getStringExtra("Nickname");
                     if(titleStr==null||titleStr.length()==0)titleStr=identify;
                     title.setTitleText(titleStr);//需要优化  intent加入聊天者昵称
                }
                break;


        }
        voiceSendingView = (VoiceSendingView) findViewById(R.id.voice_sending);
        presenter.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //退出聊天界面时输入框有内容，保存草稿
        if (input.getText().length() > 0){
            TextMessage message = new TextMessage(input.getText());
            presenter.saveDraft(message.getMessage());
        }else{
            presenter.saveDraft(null);
        }
        RefreshEvent.getInstance().onRefresh();
        presenter.readMessages();
        MediaUtil.getInstance().stop();
    }




    /**
     * 显示消息
     *
     * @param message
     */
    @Override
    public void showMessage(TIMMessage message) {
        if (message == null) {
            adapter.notifyDataSetChanged();
        } else {
            Message mMessage = MessageFactory.getMessage(message);
            if (mMessage != null) {
                if (mMessage instanceof CustomMessage){
                    CustomMessage.Type messageType = ((CustomMessage) mMessage).getType();
                    switch (messageType){
                        case TYPING:
                            TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
                            title.setTitleText("对方正在输入…");
                            handler.removeCallbacks(resetTitle);
                            handler.postDelayed(resetTitle,3000);
                            break;
                        default:
                            break;
                    }
                }else{
                    if (messageList.size()==0){
                        mMessage.setHasTime(null);
                    }else{
                        mMessage.setHasTime(messageList.get(messageList.size()-1).getMessage());
                    }
                    adapter.addMessafe(mMessage);
                    adapter.notifyDataSetChanged();
                    this.layoutManager.scrollToPositionWithOffset(this.adapter.getItemCount() - 1, 0);
                }

            }
        }

    }

    /**
     * 显示消息
     *
     * @param messages
     */
    @Override
    public void showMessage(List<TIMMessage> messages) {
        int newMsgNum = 0;
        for (int i = 0; i < messages.size(); ++i){
            Message mMessage = MessageFactory.getMessage(messages.get(i));
            if (mMessage == null || messages.get(i).status() == TIMMessageStatus.HasDeleted) continue;
            if (mMessage instanceof CustomMessage && (((CustomMessage) mMessage).getType() == CustomMessage.Type.TYPING ||
                    ((CustomMessage) mMessage).getType() == CustomMessage.Type.INVALID)) continue;
            ++newMsgNum;
            if (i != messages.size() - 1){
                mMessage.setHasTime(messages.get(i+1));
                messageList.add(0, mMessage);
            }else{
                mMessage.setHasTime(null);
                messageList.add(0, mMessage);
            }
        }
        adapter.setMessageList(messageList);
        adapter.notifyDataSetChanged();
        this.layoutManager.scrollToPositionWithOffset(newMsgNum, 0);
    }

    /**
     * 清除所有消息，等待刷新
     */
    @Override
    public void clearAllMessage() {
        messageList.clear();
    }

    /**
     * 发送消息成功
     *
     * @param message 返回的消息
     */
    @Override
    public void onSendMessageSuccess(TIMMessage message) {
        showMessage(message);
    }

    /**
     * 发送消息失败
     *
     * @param code 返回码
     * @param desc 返回描述
     */
    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {
        long id = message.getMsgUniqueId();
        for (Message msg : messageList){
            if (msg.getMessage().getMsgUniqueId() == id){
                switch (code){
                    case 80001:
                        //发送内容包含敏感词
                        adapter.notifyDataSetChanged();
                        break;
                    default:{
                        Toast.makeText(this, "errorcode"+code, Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        break;
                    }

                }
            }
        }

    }

    /**
     * 发送图片消息
     */
    @Override
    public void sendImage() {
        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
        intent_album.setType("image/*");
        startActivityForResult(intent_album, IMAGE_STORE);
    }

    /**
     * 发送照片消息
     */
    @Override
    public void sendPhoto() {
        Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent_photo.resolveActivity(getPackageManager()) != null) {
            File tempFile = FileUtil.getTempFile(FileUtil.FileType.IMG);
            if (tempFile != null) {
                fileUri = Uri.fromFile(tempFile);
            }
            intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent_photo, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    /**
     * 发送文本消息
     */
    @Override
    public void sendText() {
        Message message = new TextMessage(input.getText());

        presenter.sendMessage(message.getMessage());
        input.setText("");
    }

    /**
     * 发送文件
     */
    @Override
    public void sendFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_CODE);
    }


    /**
     * 开始发送语音消息
     */
    @Override
    public void startSendVoice() {
        voiceSendingView.setVisibility(View.VISIBLE);
        voiceSendingView.showRecording();
        recorder.startRecording();

    }

    /**
     * 结束发送语音消息
     */
    @Override
    public void endSendVoice() {
        voiceSendingView.release();
        voiceSendingView.setVisibility(View.GONE);
        recorder.stopRecording();
        if (recorder.getTimeInterval() < 1) {
            Toast.makeText(this, getResources().getString(R.string.chat_audio_too_short), Toast.LENGTH_SHORT).show();
        } else if (recorder.getTimeInterval() > 60) {
            Toast.makeText(this, getResources().getString(R.string.chat_audio_too_long), Toast.LENGTH_SHORT).show();
        } else {
            Message message = new VoiceMessage(recorder.getTimeInterval(), recorder.getFilePath());
            presenter.sendMessage(message.getMessage());
        }
    }

    /**
     * 发送小视频消息
     *
     * @param fileName 文件名
     */
    @Override
    public void sendVideo(String fileName) {
        Message message = new VideoMessage(fileName);
        presenter.sendMessage(message.getMessage());
    }


    /**
     * 结束发送语音消息
     */
    @Override
    public void cancelSendVoice() {

    }

    /**
     * 正在发送
     */
    @Override
    public void sending() {
        if (type == TIMConversationType.C2C){
            Message message = new CustomMessage(CustomMessage.Type.TYPING);
            presenter.sendOnlineMessage(message.getMessage());
        }
    }

    /**
     * 显示草稿
     */
    @Override
    public void showDraft(TIMMessageDraft draft) {
        input.getText().append(TextMessage.getString(draft.getElems(), this));
    }

    @Override
    public void videoAction() {
        /*Intent intent = new Intent(this, TCVideoRecordActivity.class);
        startActivityForResult(intent, VIDEO_RECORD);*/
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                   ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Message message = messageList.get(info.position);
        menu.add(0, 1, Menu.NONE, getString(R.string.chat_del));
        if (message.isSendFail()){
            menu.add(0, 2, Menu.NONE, getString(R.string.chat_resend));
        }
        if (message instanceof ImageMessage || message instanceof FileMessage){
            menu.add(0, 3, Menu.NONE, getString(R.string.chat_save));
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Message message = messageList.get(info.position);
        switch (item.getItemId()) {
            case 1:
                message.remove();
                messageList.remove(info.position);
                adapter.notifyDataSetChanged();
                break;
            case 2:
                messageList.remove(message);
                presenter.sendMessage(message.getMessage());
                break;
            case 3:
                message.save();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && fileUri != null) {
                showImagePreview(fileUri.getPath());
            }
        } else if (requestCode == IMAGE_STORE) {

            if (resultCode == RESULT_OK && data != null) {
                showImagePreview(FileUtil.getFilePath(this, data.getData()));
            }

        } else if (requestCode == FILE_CODE) {
            if (resultCode == RESULT_OK) {
                sendFile(FileUtil.getFilePath(this, data.getData()));
            }
        } else if (requestCode == IMAGE_PREVIEW){
            if (resultCode == RESULT_OK) {
                boolean isOri = data.getBooleanExtra("isOri",false);
                String path = data.getStringExtra("path");
                File file = new File(path);
                if (file.exists() && file.length() > 0){
                    if (file.length() > 1024 * 1024 * 10){
                        Toast.makeText(this, getString(R.string.chat_file_too_large),Toast.LENGTH_SHORT).show();
                    }else{
                        Message message = new ImageMessage(path,isOri);
                        presenter.sendMessage(message.getMessage());
                    }
                }else{
                    Toast.makeText(this, getString(R.string.chat_file_not_exist),Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == VIDEO_RECORD) {
            if (resultCode == RESULT_OK) {
                String videoPath = data.getStringExtra("videoPath");
                String coverPath = data.getStringExtra("coverPath");
                long duration = data.getLongExtra("duration", 0);
                Message message = new VideoMessage(videoPath, coverPath, duration);
                presenter.sendMessage(message.getMessage());
            }
        }

    }


    private void showImagePreview(String path){
        if (path == null) return;
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("path", path);
        startActivityForResult(intent, IMAGE_PREVIEW);
    }

    private void sendFile(String path){
        if (path == null) return;
        File file = new File(path);
        if (file.exists()){
            if (file.length() > 1024 * 1024 * 10){
                Toast.makeText(this, getString(R.string.chat_file_too_large),Toast.LENGTH_SHORT).show();
            }else{
                Message message = new FileMessage(path);
                presenter.sendMessage(message.getMessage());
            }
        }else{
            Toast.makeText(this, getString(R.string.chat_file_not_exist),Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 将标题设置为对象名称
     */
    private Runnable resetTitle = new Runnable() {
        @Override
        public void run() {
            TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
            title.setTitleText(titleStr);
        }
    };
    /*@Override
    public void openMap() {

        //Intent intent = new Intent(this ,com.hsd.ui.IndoorLocationActivity.class);
        //startActivityForResult(intent, this.LOCATION_CODE);
        //pr = (ProgressBar) findViewById(R.id.map_progress);
        //pr.setVisibility(View.VISIBLE);

        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new MyLocationListenner1());
        LocationClientOption option = new LocationClientOption();
        // 设置定位模式为高精度
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 返回的定位结果是百度经纬度,默认值gcj02
        option.setCoorType("bd09ll");
        // 设置发起定位请求的间隔时间为5000ms
        option.setScanSpan(0);
        // 返回的定位结果包含地址信息
        option.setIsNeedAddress(true);
        // 返回的定位结果包含手机机头的方向
        option.setNeedDeviceDirect(true);
        // 打开GPS
        option.setOpenGps(true);

        mLocClient.setLocOption(option);
        mLocClient.start();
        Toast.makeText(ChatActivity.this,"开始定位，请等待...",Toast.LENGTH_LONG).show();
        /*//*//*
    }

    //-------component of map---------------------
    LocationClient mLocClient;
    private GeoCoder mGeoCoder = GeoCoder.newInstance();
    double longitude,latitude;

    class MyLocationListenner1 implements BDLocationListener {



        @Override
        public void onReceiveLocation(BDLocation location) {
            //sendLocation(location.getLatitude(),location.getLongitude());
            //pr.setVisibility(View.GONE);
            LatLng mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            Toast.makeText(ChatActivity.this,"获取到经纬度\n"+longitude+"\n"+latitude,Toast.LENGTH_LONG).show();
            // 反地理编码请求参数对象
            ReverseGeoCodeOption mReverseGeoCodeOption = new ReverseGeoCodeOption();
            mReverseGeoCodeOption.location(mLatLng);
            // 发起反地理编码请求(经纬度->地址信息)
            mGeoCoder.reverseGeoCode(mReverseGeoCodeOption);
            mGeoCoder.setOnGetGeoCodeResultListener(mOnGetGeoCoderResultListener);
            //pr.setVisibility(View.GONE);

        }

        public void onConnectHotSpotMessage(String s, int i) {}
    }
    private OnGetGeoCoderResultListener mOnGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            // 反地理编码查询结果回调函数
            // 将地理位置信息载入到集合中
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(ChatActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                        .show();
                return;

            }


            Toast.makeText(ChatActivity.this,result.getSematicDescription() + "\n"
                    , Toast.LENGTH_SHORT).show();
            sendLocation(latitude,longitude,result.getSematicDescription());
        }

        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {

        }
    };
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stop();
       /* if(mLocClient!=null)
            mLocClient.stop();*/
    }


}
