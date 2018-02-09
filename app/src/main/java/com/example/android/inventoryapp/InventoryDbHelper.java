package com.example.android.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by UFO_24 on 23-12-2017.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "products";

    private static final int DATABASE_VERSION = 1;

    public InventoryDbHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // CREATE TABLE tablename (_Id INTEGER PRIMARY KEY AUTOINCREMENT, _productName  TEXT NOT NULL,
        // _price INTEGER NOT NULL,  _quantity INTEGER NOT NULL DEFAULT 0, _sujppliername TEXT NOT NULL, SupplierEmail TEXT NOT NULL
        // , suppliercontact TEXT NOT NULL)

        String SQL_CREATE_INVENTORY_TABLE ="CREATE TABLE "
                + InventoryContract.InventoryEntry.TABLE_NAME + "("
                + InventoryContract.InventoryEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_NAME+" TEXT NOT NULL, "
                + InventoryContract.InventoryEntry.COLIMN_INVENTORY_ITEM_PRICE+" INTEGER NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_QUANTITY+" INTEGER NOT NULL DEFAULT 0, "
                + InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME+" TEXT NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_SUPPLIER_CONTACT+" TEXT NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL+" TEXT NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_IMAGE+" TEXT NOT NULL"
                +");";

        sqLiteDatabase.execSQL(SQL_CREATE_INVENTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
