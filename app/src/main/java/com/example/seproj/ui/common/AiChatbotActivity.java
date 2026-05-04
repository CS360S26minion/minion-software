package com.example.seproj.ui.common;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;
import com.example.seproj.service.AiChatbotService;

/**
 * Student-facing chat screen for AI counseling support.
 * Displays conversation bubbles and sends student messages to the AI chatbot service.
 *
 * Outstanding issues:
 * - Chat messages are not saved after leaving the screen.
 */
public class AiChatbotActivity extends AppCompatActivity {
    private LinearLayout layoutMessages;
    private ScrollView scrollChat;
    private EditText etChatMessage;
    private Button btnSendMessage;
    private ProgressBar progressChat;
    private AiChatbotService chatbotService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chatbot);

        Button btnBack = findViewById(R.id.btnBack);
        layoutMessages = findViewById(R.id.layoutMessages);
        scrollChat = findViewById(R.id.scrollChat);
        etChatMessage = findViewById(R.id.etChatMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        progressChat = findViewById(R.id.progressChat);
        chatbotService = new AiChatbotService();
        String studentId = getIntent().getStringExtra("studentId");
        String studentName = getIntent().getStringExtra("studentName");
        BottomTaskbar.attachStudent(this, studentId, studentName);

        btnBack.setOnClickListener(v -> finish());
        btnSendMessage.setOnClickListener(v -> sendMessage());

        String greeting = "Hi" + (studentName == null || studentName.trim().isEmpty() ? "" : ", " + studentName.trim())
                + ". I am here to listen. What is on your mind today?";
        addMessage(greeting, false);
    }

    private void sendMessage() {
        String message = etChatMessage.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(this, "Type a message first", Toast.LENGTH_SHORT).show();
            return;
        }

        addMessage(message, true);
        etChatMessage.setText("");
        hideKeyboard();
        setSending(true);

        chatbotService.sendMessage(message, new AiChatbotService.ChatCallback() {
            @Override
            public void onSuccess(String reply) {
                setSending(false);
                addMessage(reply, false);
            }

            @Override
            public void onFailure(Exception e) {
                setSending(false);
                addMessage("I could not reply right now. " + e.getMessage(), false);
            }
        });
    }

    private void addMessage(String message, boolean fromUser) {
        TextView bubble = new TextView(this);
        bubble.setText(message);
        bubble.setTextSize(15);
        bubble.setLineSpacing(4, 1);
        bubble.setTextColor(getColor(fromUser ? R.color.white : R.color.text_primary));
        bubble.setBackgroundResource(fromUser ? R.drawable.bg_chat_bubble_user : R.drawable.bg_chat_bubble_bot);
        bubble.setPadding(dp(14), dp(10), dp(14), dp(10));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dp(8), dp(6), dp(8), dp(6));
        params.gravity = fromUser ? Gravity.END : Gravity.START;
        params.width = getResources().getDisplayMetrics().widthPixels - dp(88);
        bubble.setLayoutParams(params);

        if (!fromUser && layoutMessages.getChildCount() == 0) {
            bubble.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
        }

        layoutMessages.addView(bubble);
        scrollChat.post(() -> scrollChat.fullScroll(View.FOCUS_DOWN));
    }

    private void setSending(boolean sending) {
        progressChat.setVisibility(sending ? View.VISIBLE : View.GONE);
        btnSendMessage.setEnabled(!sending);
        etChatMessage.setEnabled(!sending);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etChatMessage.getWindowToken(), 0);
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}



