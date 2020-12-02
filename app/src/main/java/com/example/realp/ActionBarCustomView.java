package com.example.realp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

//커스텀 액션바 클래스
public class ActionBarCustomView {

    private ImageView mBackImg = null;
    private TextView mTitleText = null;

    public ActionBarCustomView(AppCompatActivity activity, View.OnClickListener onClickListener) {
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater mInflater = LayoutInflater.from(activity);

        View customView = mInflater.inflate(R.layout.actionbar, null);

        mBackImg = customView.findViewById(R.id.back_img);
        mTitleText = customView.findViewById(R.id.title_text);

        if (onClickListener != null) {
            mBackImg.setOnClickListener(onClickListener);
        }

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        actionBar.setCustomView(customView, layoutParams);
    }




    public void setTitle(String strTitle) {
        mTitleText.setText(strTitle);
    }
}
