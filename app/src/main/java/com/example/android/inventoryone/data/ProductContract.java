package com.example.android.inventoryone.data;

import android.provider.BaseColumns;

public final class ProductContract {

    /**
     * Just here so that it can't be initialized by anything else
     */
    private ProductContract() {
    }

    public static final class ProductEntry implements BaseColumns {

        /**
         * Name of database table for products
         */
        public final static String TABLE_NAME = "products";

        /**
         * Unique ID number for the product (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the product.
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "name";

        /**
         * Price of the product.
         * Type: INTEGER
         */
        public final static String COLUMN_PRODUCT_PRICE = "price";

        /**
         * Quantity of the product.
         * Type: INTEGER
         */
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";

        /**
         * Supplier's name for the product.
         * Type: String
         */
        public final static String COLUMN_PRODUCT_SUPPLIER_NAME = "supplier_name";

        /**
         * Supplier's phone number
         * Type: STRING (INTEGER is not enough for phone numbers, also doesn't play well with - or () )
         */
        public final static String COLUMN_PRODUCT_PHONE_NUMBER = "supplier_number";

    }

}
