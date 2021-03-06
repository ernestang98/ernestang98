package com.mdp34.message;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import androidx.fragment.app.Fragment;
import com.mdp34.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdp34.bluetooth.BluetoothService;

import java.nio.charset.Charset;

public class MessageFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "MessageFragment";

    private PageViewModel pageViewModel;

    // Declaration Variable
    // Shared Preferences
    SharedPreferences sharedPreferences;

    FloatingActionButton send;
    private static TextView messageReceivedTextView;
    private EditText typeBoxEditText;

    public static MessageFragment newInstance(int index) {
        MessageFragment fragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_message, container, false);

        send = (FloatingActionButton) root.findViewById(R.id.messageButton);

        // Message Box
        messageReceivedTextView = (TextView) root.findViewById(R.id.messageReceivedTextView);
        messageReceivedTextView.setMovementMethod(new ScrollingMovementMethod());
        typeBoxEditText = (EditText) root.findViewById(R.id.typeBoxEditText);

        // get shared preferences
        sharedPreferences = getActivity().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLog("Clicked sendTextBtn");
                String sentText = "" + typeBoxEditText.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("message", sharedPreferences.getString("message", "") + '\n'  + "ME:" + sentText);
                editor.commit();
                messageReceivedTextView.setText(sharedPreferences.getString("message", ""));
                typeBoxEditText.setText("");

                if (BluetoothService.connStatusFlag == true) {
                    byte[] bytes = sentText.getBytes(Charset.defaultCharset());
                    BluetoothService.write(bytes);
                }
                showLog("Exiting sendTextBtn");
            }
        });

        return root;
    }

    private static void showLog(String message) {
        Log.d(TAG, message);
    }

    public static TextView getMessageReceivedTextView() {
        return messageReceivedTextView;
    }
}
