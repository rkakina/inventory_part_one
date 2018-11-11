package com.example.android.inventoryone;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
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

    /** Tag for the log messages */
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
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView supplierNameTextView = (TextView) view.findViewById(R.id.supplierName);
        TextView supplierNumberTextView = (TextView) view.findViewById(R.id.supplierNumber);
        Button saleButtonView = (Button) view.findViewById(R.id.saleButton);

        // Find the columns of product attributes that we're interested in
        int iDColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int supplierNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
        int supplierNumberColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE_NUMBER);

        // Read the product attributes from the Cursor for the current product
        final int iD = cursor.getInt(iDColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex); //Integer.toString()?
        final String productQuantity = cursor.getString(quantityColumnIndex); //Integer.toString()?
        String productSupplierName = cursor.getString(supplierNameColumnIndex);
        String productSupplierNumber = cursor.getString(supplierNumberColumnIndex);


        // If the product supplier or number is unknown, then use some default text
        // that says "Unknown supplier/number", so the TextView isn't blank.
        //Might need to revisit price or quantity if the blanks are unaesthetic.
        if (TextUtils.isEmpty(productSupplierName)) {
            productSupplierName = context.getString(R.string.supplier_unknown);
        }
        if (TextUtils.isEmpty(productSupplierNumber)) {
            productSupplierNumber = context.getString(R.string.number_unknown);
        }

        // Update the TextViews with the attributes for the current product
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);
        supplierNameTextView.setText(productSupplierName);
        supplierNumberTextView.setText(productSupplierNumber);

        saleButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentInventory = Integer.parseInt(productQuantity);
                Log.v(LOG_TAG,"finna this current inventory is:"+currentInventory);
                if(currentInventory==0) {
                    Toast.makeText(view.getContext(), "This product is sold out.",Toast.LENGTH_SHORT).show();
                } else if(currentInventory >=1) {
                    currentInventory-=1;
                    ContentValues values = new ContentValues();
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, currentInventory);
                    Uri updatedUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, iD);
                    Log.v(LOG_TAG,"finna URI updated with latest");
                    Log.v(LOG_TAG,"finna URI:"+updatedUri.toString());
                    context.getContentResolver().update(updatedUri, values, null, null);
                    Log.v(LOG_TAG,"finna updated");
                    quantityTextView.setText(currentInventory);
                }
            }
        });

    }
}
