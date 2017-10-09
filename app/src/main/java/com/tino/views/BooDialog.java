package com.tino.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tino.R;


public class BooDialog extends DialogFragment {
    private BooBuilder builder;

    public static class BooBuilder {
        private BooDialog booDialog;
        private Button bt_cancel;
        private Button bt_sure;
        private Context context;
        private Dialog dialog;
        private LinearLayout ll_bottom;
        private LinearLayout ll_content;
        private int mTheme;
        private TextView tv_title;

        public interface ButtonClick {
            void onSure(String str);
        }

        public class EditBuilder {
            private AppCompatEditText editView;

            public EditBuilder(AppCompatEditText editText) {
                this.editView = editText;
            }

            public EditBuilder editText(String text) {
                this.editView.setText(text);
                return this;
            }

            public EditBuilder onSure(final ButtonClick click) {
                BooBuilder.this.bt_sure.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (click != null) {
                            click.onSure(EditBuilder.this.editView.getText().toString().trim());
                        }
                        BooBuilder.this.onDismiss();
                    }
                });
                return this;
            }

            public BooDialog show(FragmentManager manager) {
                return BooBuilder.this.onShow(manager);
            }
        }

        public class ProgressBuilder {
            private TextView tv_msg;
            private ProgressWheel progressWheel;
            public ProgressBuilder(TextView tv) {
                this.tv_msg = tv;
            }
            public ProgressBuilder(TextView tv,ProgressWheel progressWheel) {
                this.tv_msg = tv;
                this.progressWheel=progressWheel;
            }
            public ProgressBuilder setMessage(String message) {
                this.tv_msg.setText(message);
                return this;
            }
            public  ProgressBuilder setProgressWheelDisVisiable(){
                this.progressWheel.setVisibility(View.GONE);
                return this;
            }
            public BooDialog show(FragmentManager manager) {
                return BooBuilder.this.onShow(manager);
            }
        }

        public class SingleCheckBuilder {
            public RadioGroup group;

            public SingleCheckBuilder(RadioGroup radioGroup) {
                this.group = radioGroup;
            }

            public BooDialog show(FragmentManager manager) {
                return BooBuilder.this.onShow(manager);
            }
        }

        public BooBuilder(Context context) {
            this(context, 0);
        }

        public BooBuilder(Context context, int theme) {
            this.context = context;
            this.mTheme = theme;
            if (this.mTheme == 0) {
                this.mTheme = R.style.CustomDialog;
            }
            init();
        }

        private void init() {
            this.dialog = new Dialog(this.context, this.mTheme);
            this.dialog.requestWindowFeature(1);
            this.dialog.setContentView(R.layout.dialog_common);
            this.dialog.setCanceledOnTouchOutside(true);
            Window window = this.dialog.getWindow();
            LayoutParams wlp = window.getAttributes();
            wlp.gravity = 17;
            wlp.width = -1;
            window.setAttributes(wlp);
            initView(this.dialog);
        }

        public BooBuilder title(String title) {
            this.tv_title.setText(title);
            return this;
        }

        public EditBuilder editMode() {
            View editView = View.inflate(this.context, R.layout.dialog_edit, null);
            this.ll_content.addView(editView);
            return new EditBuilder((AppCompatEditText) editView.findViewById(R.id.et_content));
        }

        public ProgressBuilder progressMode() {
            View progressView = View.inflate(this.context, R.layout.dialog_progress, null);
            TextView tv_msg = (TextView) progressView.findViewById(R.id.tv_msg);
            ProgressWheel progressWheel=(ProgressWheel) progressView.findViewById(R.id.progress);
            this.ll_content.addView(progressView);
            disableBottom();
            return new ProgressBuilder(tv_msg,progressWheel);
        }


        public BooBuilder customViewMode(View view) {
            this.dialog.setContentView(view);
            return this;
        }

        public SingleCheckBuilder singleCheckMode() {
            disableBottom();
            View checkView = View.inflate(this.context, R.layout.dialog_single_check, null);
            this.ll_content.addView(checkView);
            return new SingleCheckBuilder((RadioGroup) checkView.findViewById(R.id.radio_root));
        }

        private void initView(Dialog dialog) {
            this.ll_content = (LinearLayout) dialog.findViewById(R.id.ll_content);
            this.ll_bottom = (LinearLayout) dialog.findViewById(R.id.ll_bottom);
            this.tv_title = (TextView) dialog.findViewById(R.id.tv_title);
            this.bt_cancel = (Button) dialog.findViewById(R.id.bt_cancel);
            this.bt_sure = (Button) dialog.findViewById(R.id.bt_sure);
            this.bt_cancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    BooBuilder.this.onDismiss();
                }
            });
        }

        private void disableBottom() {
            this.ll_bottom.setVisibility(View.GONE);
        }

        public Dialog getDialog() {
            return this.dialog;
        }

        public void onDismiss() {
            if (this.booDialog != null) {
                this.booDialog.dismiss();
            }
        }

        public BooDialog onShow(FragmentManager manager) {
            this.booDialog = new BooDialog();
            this.booDialog.setBuilder(this);
            this.booDialog.show(manager, "");
            return this.booDialog;
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return this.builder.getDialog();
    }

    public void setBuilder(BooBuilder builder) {
        this.builder = builder;
    }
}
