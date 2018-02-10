package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int INVENTORY_PRODUCT_LOADER = 2;
    private InventoryProductCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_product);

       final FloatingActionButton floatingBtn = (FloatingActionButton)findViewById(R.id.new_product_btn);
if(floatingBtn != null) {
    floatingBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, InventoryEditorActvity.class);
            startActivity(intent);
        }
    });
}
       ListView productListItems = (ListView) findViewById(R.id.product_list);

       productListItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int poisition, long id) {
               Intent intent = new Intent(MainActivity.this, InventoryDetailActivity.class);
               Uri uri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);
               intent.setData(uri);
               startActivity(intent);
           }
       });

       mAdapter = new InventoryProductCursorAdapter(this, null);
       productListItems.setAdapter(mAdapter);

       getLoaderManager().initLoader(INVENTORY_PRODUCT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        switch (id) {
            case INVENTORY_PRODUCT_LOADER:
                String[] projection = new String[]{

                        InventoryContract.InventoryEntry._ID,
                        InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_NAME,
                        InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_QUANTITY,
                        InventoryContract.InventoryEntry.COLIMN_INVENTORY_ITEM_PRICE,
                        InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_IMAGE
                };

                return new CursorLoader(this, InventoryContract.InventoryEntry.CONTENT_URI, projection,
                        null, null, null);

                default:
                    return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


}
