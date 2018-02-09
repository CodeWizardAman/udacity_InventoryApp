package com.example.android.inventoryapp;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by UFO_24 on 13-01-2018.
 */

public class InventoryDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, NumberPickerDialog.OnCompleteListener {

    private static final int DIALOG_RECIEVE_PRODUCTS_TYPE = 0;
    private static final int DIALOG_TRACK_SALE_TYPE = 1;
    private static final int DIALOG_ORDER_PRODUCTS_TYPE = 2;

    private Uri mUri;

    private String mProductName;
    private int mCurrentQuantity;
    private int mCurrentPrice;
    private int mCurrentSales;

    private static final int DETAIL_LOADER = 4;

    private TextView mProductNameTextView;

    private TextView mPriceTextView;

    private TextView mQuantityTextView;

    private TextView mTotalSalesTextView;

    private ImageView mProductImageView;

    private Button mRecieveProductBtn;

    private Button mTrackSaleBtn;

    private Button mOrderBtn;

    private NumberPicker mNumberPicker;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onComplete(int dialogId, int quantity) {

        switch (dialogId) {

            case DIALOG_RECIEVE_PRODUCTS_TYPE:
                recieveProducts(quantity);
                return;

            case DIALOG_TRACK_SALE_TYPE:
                trackSale(quantity);
                return;

            case DIALOG_ORDER_PRODUCTS_TYPE:
                orderProducts(quantity);
                return;

            default:
                Log.e("ProductDetail: ", "No method regristed with dialog id: " + dialogId);

        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setContentView(R.layout.activity_product_detail);

mProductNameTextView = (TextView) findViewById(R.id.name_text_view);

mPriceTextView = (TextView) findViewById(R.id.price_text_view);

        mQuantityTextView = (TextView) findViewById(R.id.quantity_text_view);

        mTotalSalesTextView = (TextView) findViewById(R.id.total_sales_text_view);

        mProductImageView = (ImageView)findViewById(R.id.product_image);

       mRecieveProductBtn = (Button)findViewById(R.id.shipment_btn);

       mTrackSaleBtn = (Button)findViewById(R.id.record_sale);

       mOrderBtn = (Button)findViewById(R.id.order_btn);

       mUri = getIntent().getData();
       if(mUri == null){
           finish();
       }

       setTitle("Product Details");

       mRecieveProductBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               NumberPickerDialog dialog = NumberPickerDialog.initDialog(DIALOG_RECIEVE_PRODUCTS_TYPE, "Enter the quanity recieved",
                       "Update Qunatity");
               dialog.show(getSupportFragmentManager(),"Recieve Dialog");
           }
       });

       mTrackSaleBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               NumberPickerDialog dialog = NumberPickerDialog.initDialog(DIALOG_TRACK_SALE_TYPE, "Enter quantities sold",
                       "Update Quantity");
               dialog.show(getSupportFragmentManager(), "Track Sale Dialog");
           }

       });

       mOrderBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               NumberPickerDialog dialog = NumberPickerDialog.initDialog(DIALOG_ORDER_PRODUCTS_TYPE, "Select number of quantites to order",
               "Update Quantity");
               dialog.show(getSupportFragmentManager(), "Order Dialog");
           }
       });
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
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

        switch (id){
            case DETAIL_LOADER:
                return new CursorLoader(
                        getApplicationContext(),
                        mUri,
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor != null && cursor.moveToNext()){
            mProductName = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_NAME));
            mCurrentQuantity =  cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_QUANTITY));
            mCurrentPrice = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLIMN_INVENTORY_ITEM_PRICE));

            String quantity = String.valueOf(mCurrentQuantity);
            String price = String.valueOf(mCurrentPrice);

            byte[] imageArray = cursor.getBlob(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_IMAGE));

            Bitmap bitmap = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);

            mProductNameTextView.setText(mProductName);
            mQuantityTextView.setText(quantity);
            mPriceTextView.setText(price);
            mProductImageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mProductNameTextView.setText("");
        mPriceTextView.setText("");
        mQuantityTextView.setText("");


    }

    private void recieveProducts(int quanity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_QUANTITY, quanity + mCurrentQuantity);

        int rowsAffected = getContentResolver().update(mUri, contentValues, null, null);

        if (rowsAffected > 0) {
            Toast.makeText(getApplicationContext(), "Product Quantity Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Product quantity could not be updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void trackSale(int quantityToSell){

        ContentValues contentValues = new ContentValues();

        if(mCurrentQuantity < quantityToSell){
            Toast.makeText(getApplicationContext(), "Quantity left not enough to fulfill the order", Toast.LENGTH_SHORT).show();
            return;
        }

        contentValues.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_ITEM_QUANTITY, mCurrentQuantity - quantityToSell);

        int rowsAffected = getContentResolver().update(mUri, contentValues, null, null);

        if(rowsAffected > 0){
            Toast.makeText(getApplicationContext(), "Sale Tracked", Toast.LENGTH_SHORT).show();
            getContentResolver().notifyChange(mUri, null);
        } else{
            Toast.makeText(getApplicationContext(), "Sale Not Tracked", Toast.LENGTH_SHORT).show();
        }
    }

    private void composeEmail(String[] emailAddress, String subject, String body){

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        try{
            startActivity(Intent.createChooser(intent, "Send mail.."));
        }catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(InventoryDetailActivity.this,
                    "There are no emails client installed", Toast.LENGTH_SHORT).show();
        }

    }

    private void orderProducts(int quantity){

        Toast.makeText(getApplicationContext(), "Clicked the positive button", Toast.LENGTH_SHORT).show();

        // Create Email Intent
        String[] emailAddress = new String[] {
                getString(R.string.order_email_address)
        };

        String emailBody = "Product Name: "+mProductName +"\nQuantity Required: "+quantity;

        composeEmail(emailAddress, getString(R.string.order_subject), emailBody);

    }
}
