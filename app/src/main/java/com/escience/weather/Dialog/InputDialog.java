package com.escience.weather.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.escience.weather.Listener.InputDialogListener;
import com.escience.weather.R;

/**
 * Created by Stark on 2017/2/10.
 */
public class InputDialog extends Dialog {
    private EditText editText;
    private boolean isAllowClose = true;
    private InputDialogListener Listener;
    public InputDialog(Context context,InputDialogListener  listener) {
        this(context, R.style.NoFrameNoDim_Dialog, listener);
    }

    public InputDialog(Context context, int theme,InputDialogListener listener) {
        super(context, theme);
        Listener=listener;
        setContentView(R.layout.dialog_input);
        editText = (EditText) findViewById(R.id.edit_send);
        Button button=(Button)findViewById(R.id.button_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listener.finish(editText.getText().toString());
                InputDialog.this.cancel();
            }
        });
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity( Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width= WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
    }
    @Override
    public void cancel() {
        super.cancel();
        Listener.cancel();
    }
    public InputDialog setOnKeyDownClose(boolean isAllowClose) {
        this.isAllowClose = isAllowClose;
        return this;
    }
    public InputDialog setOnTouchClose(boolean temp)
    {
        setCanceledOnTouchOutside(temp);
        return this;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isAllowClose) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
