package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simpletodo.adapters.ItemAdapter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // represents our model class i.e list of strings can also act as a 'database' where info is stored
    ArrayList items;

    // create member variables for our views here so we can get a handle/ref of them anytime in other methods
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;

    ItemAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate views here so we can reference them else where
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        // fill our model with mock data
        loadItems();
        // items = new ArrayList<>();
        // items.add("Buy coffee");
        // items.add("Do laundry");
        // items.add("YOLO");


        ItemAdapter.OnLongClickListener onLongClickListener = new ItemAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // position represents the exact position the user has long pressed

                // Delete the appropriate item from model
                items.remove(position);

                // Notify the adapter at which position item was deleted from the model
                itemsAdapter.notifyItemRemoved(position);

                Toast.makeText(getApplicationContext(), "Item was removed!", Toast.LENGTH_SHORT).show();

                saveItems();
            }
        };

        // use the adapter we created by linking it to the RV we instantiated above
        itemsAdapter = new ItemAdapter(items, onLongClickListener);  // construct the adapter with some model data
        rvItems.setAdapter(itemsAdapter); // set the adapter on to our RV so that we can talk with it

        // next we need to set a layout for our RV
        // below sets the most basic layout manager as our layout manager, linear layout manager (which by default put things on the UI in a vertical way)
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // implement ADD logic on button press
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when button is clicked, sends the text from the editText into the RV on the screen
                // using the adapter which helps us refresh the RV to reflect new data
                String todoItem = etItem.getText().toString();

                // add todoItem to the model
                items.add(todoItem);

                // notify the adapter we added new data to the model (the adapter will then refresh the RV for us)
                itemsAdapter.notifyItemInserted(items.size() - 1);  // also specify the position where the item is inserted (last position of our model)

                etItem.setText("");

                Toast.makeText(getApplicationContext(), "Item was added!", Toast.LENGTH_SHORT).show();

                saveItems();
            }
        });
    }

    // helper function we created to give us the file
    private File getDataFile() {
        // getFilesDir() gets the directory of this app
        // data.txt is the name of the file which will store our data
        return new File(getFilesDir(), "data.txt");
    }

    // helper function will load items by reading every line of the data file
    private void loadItems() {
        // basically help us read items from the file and load it into an array
        // WE ONLY LOAD ITEMS ONCE - WHEN THE APP STARTS (ie in onCreate)
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items from file :/", e);
            items = new ArrayList<>();
        }
    }

    // helper function saves items by writing them into the data file
    private void saveItems() {
        // basically helps us write the items from array into the file
        // WE SAVE ITEMS EVERYTIME WE ADD A NEW ITEM OR REMOVE AN ITEM
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error saving items to file :/", e);
        }
    }
}