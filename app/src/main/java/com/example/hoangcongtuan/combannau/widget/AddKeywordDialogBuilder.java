package com.example.hoangcongtuan.combannau.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.hoangcongtuan.combannau.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by hoangcongtuan on 10/6/18.
 */
public class AddKeywordDialogBuilder extends AlertDialog.Builder {
    @BindView(R.id.edtKeyword)
    EditText edtKeyword;
    View rootView;

    public AddKeywordDialogBuilder(Context context) {
        super(context);
        init();
        initWidget();
        this.setView(rootView);
        ButterKnife.bind(this, rootView);
    }

    private void init() {
        setTitle(R.string.add_key_word);
    }

    private void initWidget() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        rootView = inflater.inflate(R.layout.layout_add_keyword_dialog, null);
    }

    public String getKeyWords() {
        return edtKeyword.getText().toString();
    }

}
