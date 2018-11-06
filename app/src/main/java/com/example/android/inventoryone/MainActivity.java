package com.example.android.inventoryone;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.inventoryone.data.ProductContract;
import com.example.android.inventoryone.data.ProductDbHelper;

public class MainActivity extends AppCompatActivity {

    /**
     * Database helper that will provide us access to the database
     */
    private ProductDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiating and passing the context, which is this
        mDbHelper = new ProductDbHelper(this);
    }

    /**
     * Displays the data that was put in to the app
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Defines a projection that specifies which columns will be used from the database
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE_NUMBER};

        // Performs query on inventory table
        Cursor cursor = db.query(
                ProductContract.ProductEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                          // The columns for the WHERE clause
                null,                      // The values for the WHERE clause
                null,                           // Don't group the rows
                null,                            // Don't filter by row groups
                null);                          // The sort order

        TextView displayView = (TextView) findViewById(R.id.text_view_product);

        try {
            // Creates a header to display above the contents of the database
            // Cursor goes through each of the rows and displays contents as specified below
            displayView.setText("The product table contains " + cursor.getCount() + " products.\n\n");
            displayView.append(ProductContract.ProductEntry._ID + " - " +
                    ProductContract.ProductEntry.COLUMN_PRODUCT_NAME + " - " +
                    ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE + " - " +
                    ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY + " - " +
                    ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " - " +
                    ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE_NUMBER + "\n");

            // index of each column
            int idColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierNumberColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE_NUMBER);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Get all the values of the product currently under the cursor
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierNumber = cursor.getString(supplierNumberColumnIndex);

                // Display the values as so:
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierNumber));
            }
        } finally {
            // Closing the cursor; we're done with it for now.
            cursor.close();
        }
    }

    /**
     * Just for putting dummy data in the app
     */
    private void insertProduct() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Creates ContentValues with dummy data for new product
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "Playstation 4 Pro");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, 399);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, 20);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Sony");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE_NUMBER, "808-799-4234");

        // puts the content values in the database
        long newRowId = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu, adds it to the bar
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Switches for different items in the list that are selected
        switch (item.getItemId()) {
            // Adds dummy data
            case R.id.action_insert_dummy_data:
                insertProduct();
                displayDatabaseInfo();
                return true;
            // Deletes all data, doesn't do anything atm
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
