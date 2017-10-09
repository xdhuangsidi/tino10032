package com.tino.ui.upload;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lling.photopicker.PhotoPickerActivity;
import com.tino.R;
import com.tino.core.ActionCallback;
import com.tino.core.net.UploadImp;
import com.tino.ui.upload.UpShareService;
import com.tino.views.BooDialog;
import com.tino.views.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

public class UpShareActivity extends AppCompatActivity {
    private GridAdapter adapter;
    EditText et_title;
    EditText et_text;
    NoScrollGridView gridView;
    private ArrayList<String> picPaths;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindview();
        init();
    }

    public void bindview()
{
    setContentView(setInflateId());
    gridView = (NoScrollGridView)findViewById( R.id.gridView);
    toolbar = (Toolbar) findViewById( R.id.toolbar);
    et_title=(EditText)findViewById(R.id.et_title);
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationIcon((int) R.drawable.btn_back1);
    toolbar.setNavigationOnClickListener(new OnClickListener() {
        public void onClick(View v) {
          finish();
        }
    });
    setTitle("上传帖子");
    et_text = (EditText) findViewById(  R.id.et_text);
    Button bt=(Button)findViewById(R.id.bt_send);
    bt.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            sendShare();
        }
    });
}
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<String> picList = new ArrayList();
        private int selectedPosition = 0;

        public class ViewHolder {
            public ImageButton bt_delete;
            public ImageView image;
        }

        public GridAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
            picList.clear();
        }

        public int getCount() {
            return this.picList.size() + 1;
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return (long) arg0;
        }

        public void setSelectedPosition(int position) {
            this.selectedPosition = position;
        }

        public int getSelectedPosition() {
            return this.selectedPosition;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
           /* if (convertView == null) {*/
                convertView = this.inflater.inflate(R.layout.item_publish_grid, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.iv);
                holder.bt_delete = (ImageButton) convertView.findViewById(R.id.bt_delete);
                convertView.setTag(holder);
          /*  } else {
                holder = (ViewHolder) convertView.getTag();
            }*/
            if (position == this.picList.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(UpShareActivity.this.getResources(), R.drawable.icon_addpic));
                holder.bt_delete.setVisibility(View.GONE);
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.bt_delete.setVisibility(View.VISIBLE);
                Glide.with(UpShareActivity.this).load((String) this.picList.get(position)).into(holder.image);
            }
            if(this.picList.size()==1&&position == 1){
                holder.image.setImageBitmap(BitmapFactory.decodeResource(UpShareActivity.this.getResources(), R.drawable.icon_addpic));
                holder.bt_delete.setVisibility(View.GONE);
            }
            holder.bt_delete.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    GridAdapter.this.picList.remove(position);
                    GridAdapter.this.notifyDataSetChanged();
                }
            });
            holder.image.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (position == GridAdapter.this.picList.size()) {
                        PhotoPickerActivity.showWithParams(UpShareActivity.this, true, 1, 9 - GridAdapter.this.picList.size());
                    }
                }
            });
            return convertView;
        }

        public void setPicList(List<String> picList1) {
            if (picList1 != null) {
                this.picList = picList1;
                notifyDataSetChanged();
            }
        }

        public void addPicList(List<String> picList1) {
            if (picList1 != null) {
                for (String a:picList1)
                this.picList.add(a);
                notifyDataSetChanged();
            }
        }

        public List<String> getPicList() {
            return this.picList;
        }
    }


    void sendShare() {
        BooDialog dialog = new BooDialog.BooBuilder(UpShareActivity.this).title("发布分享").progressMode().setMessage("请稍候...").show(UpShareActivity.this.getFragmentManager());
        String text = this.et_text.getText().toString().trim();
        String title=this.et_title.getText().toString().trim();
        if (TextUtils.isEmpty(text)||TextUtils.isEmpty(title)) {
         /*   showToast("写点东西再发布吧...");
            dialog.dismiss();*/

        }
         if (this.picPaths == null || this.picPaths.size() <= 0) {
            uploadShare(text, title,"[]", dialog);
        } else {
            Intent intent = new Intent(this, UpShareService.class);
            intent.putStringArrayListExtra("pics", this.picPaths);
            intent.putExtra("text", text);
            intent.putExtra("title", title);
            startService(intent);
            dialog.dismiss();
            finish();
            showToast("后台上传中...");

        }
    }
  public void  showToast(String s){
      Toast.makeText(this,s,Toast.LENGTH_SHORT).show();

  }

    private void uploadShare(String text, String title,String picJsonArray, final BooDialog dialog) {
        final String id=getSharedPreferences("login_data", 0).getString("uid", "");
        final String token=getSharedPreferences("login_data", 0).getString("token", "");
        UploadImp.uploadblog(token,id, title,text, picJsonArray, new ActionCallback<String>() {
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
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String string) {
                dialog.dismiss();
                showToast("恭喜,发布成功!^_^");
                Intent intent = new Intent();
                intent.setAction("tino.reflash_blog");
                sendBroadcast(intent);
                finish();

            }
        });

    }

    public int setInflateId() {
        return R.layout.fragment_up_share;
    }

    public void init() {
        this.adapter = new GridAdapter(this);

        this.gridView.setAdapter(this.adapter);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1) {
            this.picPaths = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
            for(String a:picPaths){
                System.out.println("image_path:"+a);
            }
            if (this.adapter != null) {
                this.adapter.addPicList(this.picPaths);
            }
        }
    }
}
