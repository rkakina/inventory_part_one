package com.example.android.inventoryone;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryone.data.ProductContract;
import com.example.android.inventoryone.data.ProductProvider;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of product data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = ProductProvider.class.getSimpleName();

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        Button saleButtonView = (Button) view.findViewById(R.id.saleButton);

        // Find the columns of product attributes that we're interested in
        int iDColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);

        // Read the product attributes from the Cursor for the current product
        final int iD = cursor.getInt(iDColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);

        //Puts labels in front of the price and inventory
        String priceFormat = "Price: $" + productPrice;
        String inventoryFormat = "Units available: " + productQuantity;

        //Changes the color of the inventory text. Orange if 10 or below, red if 5 or below.
        if (Integer.parseInt(productQuantity) <= 5) {
            quantityTextView.setTextColor(Color.RED);
        } else if (Integer.parseInt(productQuantity) <= 10) {
            quantityTextView.setTextColor(Color.parseColor("#FF8000"));
        } else {
            quantityTextView.setTextColor(Color.BLACK);
        }

        // Update the TextViews with the attributes for the current product
        nameTextView.setText(productName);
        priceTextView.setText(priceFormat);
        quantityTextView.setText(inventoryFormat);

        /**
         * The Sale button in the list item. If you click it, decreases inventory by 1. If inventory
         * is 0, throws a toast message saying you can't do that, son.
         */
        saleButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentInventory = Integer.parseInt(productQuantity);
                long id2 = iD;
                Log.v(LOG_TAG, "finna this id2=" + id2);
                Log.v(LOG_TAG, "finna this current inventory is:" + currentInventory);
                if (currentInventory == 0) {
                    Toast.makeText(view.getContext(), "This product is sold out.", Toast.LENGTH_SHORT).show();
                } else if (currentInventory >= 1) {
                    currentInventory -= 1;
                    ContentValues values = new ContentValues();
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, currentInventory);
                    Uri updatedUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id2);
                    Log.v(LOG_TAG, "finna URI updated with latest");
                    Log.v(LOG_TAG, "finna URI:" + updatedUri.toString());
                    context.getContentResolver().update(updatedUri, values, null, null);
                    Log.v(LOG_TAG, "finna updated");
                    quantityTextView.setText(String.valueOf(currentInventory));
                }
            }
        });//End of onClickListener

    }//end of BindView

}//end of life, man.
