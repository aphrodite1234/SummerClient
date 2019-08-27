package com.example.z1229.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.summerclient.R;

public class CommentDialog extends DialogFragment implements TextWatcher,View.OnClickListener {
    //点击发表，内容不为空时的回调
    public SendListener sendListener;
    private TextView tv_send;
    private String hintText;

    private Dialog dialog;
    private EditText et_content;
    private ImageView iv_emoji;

    public CommentDialog() {
    }


    @SuppressLint("ValidFragment")
    public CommentDialog(String hintText, SendListener sendBackListener) {//提示文字
        this.hintText = hintText;
        this.sendListener = sendBackListener;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog = new Dialog(getActivity(), R.style.Comment_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        View contentView = View.inflate(getActivity(), R.layout.dialog_comment_input, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.alpha = 1;
        lp.dimAmount = 0.0f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

//        iv_emoji = (ImageView) contentView.findViewById(R.id.iv_emoji);
        et_content = (EditText) contentView.findViewById(R.id.dialog_com_input);
        et_content.setHint(hintText);
        tv_send = (TextView) contentView.findViewById(R.id.dialog_com_send);

        et_content.addTextChangedListener(this);
        tv_send.setOnClickListener(this);
//        iv_emoji.setOnClickListener(this);
        et_content.setFocusable(true);
        et_content.setFocusableInTouchMode(true);
        et_content.requestFocus();

//        final Handler handler = new Handler();
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        CommonUtils.hideSoftKeyBoard(getActivity());
//                    }
//                }, 200);
//
//            }
//        });
        return dialog;
    }


    public void cleanText() {
        et_content.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            tv_send.setEnabled(true);
            tv_send.setTextColor(Color.BLACK);
        } else {
            tv_send.setEnabled(false);
            tv_send.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_com_send:
                checkContent();
                break;
        }
    }

    private void checkContent() {
        String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "评论内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        sendListener.sendComment(content);
        dismiss();
    }

    public interface SendListener {
        void sendComment(String inputText);
    }
}
