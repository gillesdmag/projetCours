package io.artcreativity.monpremierprojet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.artcreativity.monpremierprojet.dao.DataBaseRoom;
import io.artcreativity.monpremierprojet.dao.ProductDao;
import io.artcreativity.monpremierprojet.dao.ProductRoomDao;
import io.artcreativity.monpremierprojet.entities.Product;

public class ProductDetailActivity extends AppCompatActivity {

    TextView name,description,price,quantityInStock,alertQuantity;
    int position;
    Product p = new Product();
    HashMap<String,String> maplist;
//    private List<Product> products = new ArrayList<>();
    final static int MAIN_2CALL = 156;
    private ProductDao productDao;
    boolean ismod=false;
    private ProductRoomDao productRoomDao;
    Product product;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MAIN_2CALL) {
            if(resultCode== Activity.RESULT_OK) {
                Log.e("TAG", "onActivityResult: " + data.getSerializableExtra("MY_PROD"));

                 product = (Product) data.getSerializableExtra("MY_PROD");

                name.setText(product.name);
                description.setText(product.description);
                price.setText(""+product.price);
                quantityInStock.setText(""+product.quantityInStock+" disponible"+(product.quantityInStock>1
                        ? "s" : ""));
                alertQuantity.setText(""+product.alertQuantity);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        productRoomDao.update(product);
                    }
                });
                thread.start();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productDao = new ProductDao(this);
        productRoomDao = DataBaseRoom.getInstance(getApplicationContext()).productRoomDao();


        product = (Product) getIntent().getSerializableExtra("MY_PROD");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                productRoomDao.update(product);
                    ismod = true;
            }
        });
        thread.start();

        Log.e("TAG", "onCreate: " + product);

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        quantityInStock = findViewById(R.id.quantityInStock);
        alertQuantity = findViewById(R.id.alertQuantity);



        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            position = extras.getInt("position");
        }

        name.setText(product.name);
        description.setText(product.description);
        price.setText(""+product.price);
        quantityInStock.setText(""+product.quantityInStock+" disponible"+(product.quantityInStock>1
         ? "s" : ""));
        alertQuantity.setText(""+product.alertQuantity);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = new MenuInflater(this);
        getMenuInflater().inflate(R.menu.menu2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.create_menu_item){


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("MY_PROD",getIntent().getSerializableExtra("MY_PROD"));
            intent.putExtra("position",position);
            startActivityIfNeeded(intent, MAIN_2CALL);

            return true;
        }else if(id == R.id.delete_menu_item)
        {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG","avant");
                    productRoomDao.delete(product);
                    Log.e("TAG","apres");
                }
            });
            thread.start();
            Intent intent= new Intent(getApplicationContext(),ProductActivity.class);
//            intent.putExtra("MY_PROD", product);
            intent.putExtra("MY_PROD",position);
            intent.putExtra("ismod",ismod);
            startActivityIfNeeded(intent, MAIN_2CALL);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (ismod){
            Intent intent= new Intent();
            intent.putExtra("MY_PROD1", product);
            intent.putExtra("MY_PROD",position);
            setResult(Activity.RESULT_OK, intent);
        }
        super.onBackPressed();
    }
}