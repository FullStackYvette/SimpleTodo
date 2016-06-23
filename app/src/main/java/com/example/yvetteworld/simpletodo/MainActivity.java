package com.example.yvetteworld.simpletodo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items; // Create an Array List
    ArrayAdapter<String> itemsAdapter; // Create an Array Adapter
    ListView lvItems;
    int index;
    private final int REQUEST_CODE = 20;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems(); // Read content from io file
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter); // Attach Adapter to List View
        setupListViewListener();// Invoke listener from onCreate()
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // adds input item to the list
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText); // Add etNewItem content
        etNewItem.setText(""); // Set the content of the etNewItem content as empty
        writeItems();
    }

    //Create method for setting up the listener
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View item, int pos, long id) {
                items.remove(pos);// Remove the item from the list
                itemsAdapter.notifyDataSetChanged(); // Refresh the Adapter
                writeItems(); // Update the arrays in the file after deletion
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int pos, long id) {
                String value = items.get(pos).toString();
                launchComposeView(value); //Pass value to the child form
                index = pos; // Record which position of item has been changed
            }
        });
    }

    // Read file content to items
    private void readItems()
    {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try
        {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }
        catch (IOException e)
        {
            items = new ArrayList<String>();
        }
    }

    // Write items in array to file
    private void writeItems()
    {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try
        {
            FileUtils.writeLines(todoFile, items);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        startActivityForResult(i, REQUEST_CODE); // Expect the child window pass the data to the parent, and then onActivityResult function will be triggered
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String editContent = data.getExtras().getString("EditItem");
            items.set(index, editContent);
            itemsAdapter.notifyDataSetChanged();// Notify the list is updated
            writeItems(); // Save the result
        }
    }

    public void launchComposeView(String val) {
        // first parameter is the parent window (Main Activity), second is the pop up window (Edit Item Window) to launch
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        i.putExtra("EditContent", val); // Pass the "EditContent" to edit form
        startActivityForResult(i,REQUEST_CODE); // Brings up the second activity
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.yvetteworld.simpletodo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.yvetteworld.simpletodo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


}
