package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by UFO_24 on 11-01-2018.
 */

public class InventoryProductCursorAdapter extends CursorAdapter {
    public InventoryProductCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView productNameTextView = (TextView)view.findViewById(R.id.product_name_text);

        TextView priceTextView = (TextView)view.findViewById(R.id.product_price_text);

        TextView quantityTextView = (TextView)view.findViewById(R.id.product_quantity_text);

        int id = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);

        int nameIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_NAME);

        int priceIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLIMN_INVENTORY_ITEM_PRICE);

        int quantityIndex =  cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_QUANTITY);

        String name = cursor.getString(nameIndex);

        int price = cursor.getInt(priceIndex);

        int quantity = cursor.getInt(quantityIndex);

        String formattedPrice = String.valueOf(price);
        String formattedQuantity = String.valueOf(quantity);


        productNameTextView.setText(name);
        priceTextView.setText(formattedPrice);
        quantityTextView.setText(formattedQuantity);


    }
}
