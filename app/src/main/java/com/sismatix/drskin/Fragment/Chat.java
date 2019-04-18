package com.sismatix.drskin.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sismatix.drskin.Activity.Chat_messge;
import com.sismatix.drskin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment {
    LinearLayout lv_chat;
    public Chat() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_chat, container, false);
        lv_chat = (LinearLayout) v.findViewById(R.id.lv_chat);
        lv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Chat_messge.class);
                startActivity(intent);
            }
        });
        return v;
    }

}
