package com.example.yvetteworld.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        EditText editText = (EditText) findViewById(R.id.editText);
        String username = getIntent().getStringExtra("EditContent");
        editText.setText(username); // Set the editText textbox content as the edit item
    }

    public void onSubmit(View v) {
        // closes the activity and returns to first screen
        EditText editText = (EditText) findViewById(R.id.editText);
        Intent data = new Intent();
        data.putExtra("EditItem", editText.getText().toString()); // Pass the value back with the result in the MainActivity.java
        setResult(RESULT_OK, data);
        this.finish(); // Close the window
    }
}
