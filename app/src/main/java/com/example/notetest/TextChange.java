package com.example.notetest;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by 王将 on 2018/5/21.
 */

public class TextChange implements TextWatcher {
    EditText editText;
    LinearLayout linearLayout;
    public TextChange(EditText editText,LinearLayout linearLayout){
        this.editText=editText;
        this.linearLayout=linearLayout;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (editText.getText().toString().isEmpty()){
            int id=(int)editText.getTag();
            int idList=NoteTool.getIdListEditext(NoteTool.getEditextView(id));
            if (idList!=0){
                View viewEditext=NoteTool.getEditextView(id);
                View viewImage=NoteTool.getImageView(id-1);

                linearLayout.removeView(viewEditext);
                linearLayout.removeView(viewImage);

                EditText editTextNext=NoteTool.getEdis().get(NoteTool.getIdEdi(editText)-1);
                editTextNext.setFocusable(true);//设置输入框可聚集
                editTextNext.setFocusableInTouchMode(true);//设置触摸聚焦
                editTextNext.requestFocus();//请求焦点
                editTextNext.findFocus();//获取焦点

                NoteTool.getEdis().remove((EditText) editText);
                NoteTool.getEditTexts().remove((View) viewEditext);
                NoteTool.getImages().remove((View) viewImage);

            }

        }
    }
}
