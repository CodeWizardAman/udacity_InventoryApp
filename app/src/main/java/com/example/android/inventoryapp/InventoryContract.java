package com.example.android.inventoryapp;

import android.net.Uri;
import android.provider.BaseColumns;
import android.content.ContentResolver;
import android.net.Uri;

import java.net.URI;

/**
 * Created by UFO_24 on 23-12-2017.
 */

public class InventoryContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PRODUCTS_PATH = "inventory";

    private InventoryContract(){}


    public static final class InventoryEntry implements BaseColumns{

        public final static String TABLE_NAME = "inventory";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PRODUCTS_PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PRODUCTS_PATH;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PRODUCTS_PATH);

        public static String _ID = BaseColumns._ID;

        public final static String COLUMN_INVENTORY_ITEM_NAME= "name";
        public final static String COLIMN_INVENTORY_ITEM_PRICE = "price";
        public final static String COLUMN_INVENTORY_ITEM_QUANTITY ="quantity";
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_SUPPLIER_CONTACT ="supplier_contact";
        public final static String COLUMN_SUPPLIER_EMAIL ="supplier_email";
        public final static String COLUMN_INVENTORY_ITEM_IMAGE ="image";

    }
}
