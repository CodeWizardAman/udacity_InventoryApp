package com.example.android.inventoryapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static java.lang.Integer.parseInt;

/**
 * Created by UFO_24 on 24-12-2017.
 */

public class InventoryEditorActvity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mUri;
    private static final int SINGLE_PRODUCT_LOADER = 3;

    private EditText mEditProductName;

    private EditText mEditProductQuantity;

    private EditText mEditProductPrice;

    private byte[] mImageByteArray;

    private Button mImageButton;

    private ImageView mProductImageView;

    private static final int IMAGE_PICKER_CODE = 7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_product);

        mUri = getIntent().getData();

        mEditProductName = (EditText) findViewById(R.id.edit_text_addProduct_name);
        mEditProductPrice = (EditText) findViewById(R.id.edit_text_addProduct_price);
        mEditProductQuantity = findViewById(R.id.edit_text_addProduct_quantity);

        mProductImageView = findViewById(R.id.product_image);

        mProductImageView.setVisibility(View.GONE);


        if (mUri != null) {
            setTitle("Product Info");
            getLoaderManager().initLoader(SINGLE_PRODUCT_LOADER, null, this);
        } else {
            setTitle("Add a new product");
        }

        mImageButton = (Button) findViewById(R.id.image_upload_button);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Intent chooserIntent = Intent.createChooser(intent, "Select an image");
                startActivityForResult(intent, IMAGE_PICKER_CODE);
            }
        });

    }

    private void addInventoryProduct(String name, int price, int quantity, byte[] imageByteArray) {

        ContentValues values = new ContentValues();

        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_NAME, name);
        values.put(InventoryContract.InventoryEntry.COLIMN_INVENTORY_ITEM_PRICE, price);
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_QUANTITY, quantity);

        if (imageByteArray != null) {
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_IMAGE, imageByteArray);
        }

        Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);

        if (newUri == null) {

            Toast.makeText(this, "Error with saving inventory product", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "Product saved in inventory", Toast.LENGTH_SHORT).show();
        }

        this.finish();

    }

    private void editInventoryProduct(String name, int price, int quantity, byte[] imageArray) {

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_NAME, name);
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_QUANTITY, quantity);
        values.put(InventoryContract.InventoryEntry.COLIMN_INVENTORY_ITEM_PRICE, price);

        if (imageArray != null) {
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_IMAGE, imageArray);
        }

        int rowsAffected = getContentResolver().update(mUri, values, null, null);

        if (rowsAffected == 0) {
            Toast.makeText(this, "No Changes were saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Product has been updated", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {


        String[] projection = new String[]{
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_NAME,
                InventoryContract.InventoryEntry.COLIMN_INVENTORY_ITEM_PRICE,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_IMAGE
        };


        switch (id) {
            case SINGLE_PRODUCT_LOADER:
                return new CursorLoader(this,
                        InventoryContract.InventoryEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                );

            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursorData) {

        if (cursorData != null && cursorData.moveToNext()) {

            String productName = cursorData.getString(cursorData.getColumnIndex
                    (InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_NAME));

            int priceInex = cursorData.getColumnIndex(InventoryContract.InventoryEntry.COLIMN_INVENTORY_ITEM_PRICE);
            String productPrice = String.valueOf(cursorData.getInt(priceInex));

            int quantityIndex = cursorData.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_QUANTITY);
            String productQuantity = String.valueOf(quantityIndex);

            int imageIndex = cursorData.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_IMAGE);
            mImageByteArray = cursorData.getBlob(imageIndex);

            Bitmap inventoryProductImage = BitmapFactory.decodeByteArray(mImageByteArray, 0, mImageByteArray.length);

            if (inventoryProductImage != null) {
                mProductImageView.setImageBitmap(inventoryProductImage);
                mProductImageView.setVisibility(View.VISIBLE);
            } else {
                mProductImageView.setVisibility(View.GONE);
            }

            mEditProductName.setText(productName);
            mEditProductQuantity.setText(productQuantity);
            mEditProductPrice.setText(productPrice);

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mEditProductName.setText("");
        mEditProductPrice.setText("");
        mEditProductQuantity.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }

            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);

                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                mProductImageView.setImageBitmap(selectedImage);

                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    mImageByteArray = outputStream.toByteArray();

                } catch (Exception e) {
                    Log.e("InventoryEditorActivity", "convertBitMapToByteArray exception " + e);
                }

            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
                Toast.makeText(InventoryEditorActvity.this, "No Image Picked", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveInventoryProduct();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveInventoryProduct() {

        String name = mEditProductName.getText().toString();

        int price = parseInt(mEditProductPrice.getText().toString());

        int quantity = parseInt(mEditProductQuantity.getText().toString());

        if (mUri == null) {
            addInventoryProduct(name, price, quantity, mImageByteArray);
        } else {
            editInventoryProduct(name, price, quantity, mImageByteArray);
        }
    }


}
