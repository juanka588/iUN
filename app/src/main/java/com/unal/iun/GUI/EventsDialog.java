package com.unal.iun.GUI;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.unal.iun.Interfaces.OnEventsButtonClick;
import com.unal.iun.R;

/**
 * Created by Camilo on 4/12/2017.
 */

public class EventsDialog extends Dialog {
    public static final int SELECTION_POSITIVE = 1, SELECTION_NEGATIVE = 2;


    private Button okButton;
    private Button registerButton;
    private Button webIVButton;
    private Button events;
    private EditText input;
    private int selection;


    public EventsDialog(final Context context, int themeId, final OnEventsButtonClick callback) {
        super(context, themeId);
        this.setContentView(R.layout.dialog_input_options);

        okButton = this.findViewById(R.id.button_ok);
        registerButton = this.findViewById(R.id.register_iv);
        webIVButton = this.findViewById(R.id.web_iv);
        events = this.findViewById(R.id.events_button);
        input = this.findViewById(R.id.input);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventsDialog.this.selection = SELECTION_POSITIVE;
                EventsDialog.this.dismiss();
            }
        });

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventsDialog.this.selection = SELECTION_POSITIVE;
                callback.onEventClick();
                EventsDialog.this.dismiss();
            }
        });

    }

    public String getInput() {
        return input.getText().toString();
    }

    public int getSelection() {
        return selection;
    }

    public EditText getInputView() {
        return input;
    }
}
