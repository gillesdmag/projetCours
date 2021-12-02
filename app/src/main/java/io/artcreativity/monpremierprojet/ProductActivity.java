package io.artcreativity.monpremierprojet;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.artcreativity.monpremierprojet.adapters.ProductAdapter;
import io.artcreativity.monpremierprojet.dao.DataBaseHelper;
import io.artcreativity.monpremierprojet.dao.DataBaseRoom;
import io.artcreativity.monpremierprojet.dao.ProductDao;
import io.artcreativity.monpremierprojet.dao.ProductRoomDao;
import io.artcreativity.monpremierprojet.databinding.ActivityProductBinding;
import io.artcreativity.monpremierprojet.entities.Product;

public class ProductActivity extends AppCompatActivity {

    private ActivityProductBinding binding;
    private List<Product> products = new ArrayList<>();
    private ProductAdapter productAdapter;
    final static int MAIN_CALL = 120;
    final static int MAIN_2CALL = 156;
    private ProductDao productDao;
    private ProductRoomDao productRoomDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        productDao = new ProductDao(this);
        productRoomDao = DataBaseRoom.getInstance(getApplicationContext()).productRoomDao();


        generateProducts();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProductActivity.this, MainActivity.class);
                startActivityIfNeeded(intent, MAIN_CALL);
            }
        });


        buildCustomAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MAIN_CALL) {
            if(resultCode== Activity.RESULT_OK) {
                    Log.e("TAG", "onActivityResult: " + data.getSerializableExtra("MY_PROD"));
                    // TODO: 18/11/2021 Ajout d'un nouveau produit dans la liste
                    Product product = (Product) data.getSerializableExtra("MY_PROD");
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            productRoomDao.insert(product);
                            products.add(product);
                            runOnUiThread(()->{
                                productAdapter.notifyDataSetChanged();
                            });

                        }
                    });
                    thread.start();
            }
        }

        if(requestCode==MAIN_2CALL) {
            if(resultCode== Activity.RESULT_OK) {
                    int productI = data.getIntExtra("MY_PROD",-1);
                    Product product = (Product) data.getSerializableExtra("MY_PROD1");
                    if (product!=null && productI!=0) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                products.set(productI,product);
                                runOnUiThread(() -> {
                                    productAdapter.notifyDataSetChanged();
                                });
                            }
                        });
                        thread.start();
                    }

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void buildCustomAdapter() {
        productAdapter = new ProductAdapter(this, products);
        binding.ourListView.setAdapter(productAdapter);
        binding.ourListView.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(),ProductDetailActivity.class);
            intent.putExtra("MY_PROD",products.get(position));
            intent.putExtra("position",position);
//            startActivity(intent);
            startActivityIfNeeded(intent, MAIN_2CALL);
        });
    }

    private void buildSimpleAdapterData() {
        List<Map<String, String>> mapList = new ArrayList<>();
        for (Product product :
                products) {
            Map<String, String> map = new HashMap<>();
            map.put("name", product.name);
            map.put("price", "XOF " + product.price);
            map.put("quantity",  product.quantityInStock + " disponible" +
                    (product.quantityInStock>1 ? "s" : ""));
            mapList.add(map);
        }
        binding.ourListView.setAdapter(new SimpleAdapter(this, mapList, R.layout.regular_product_item,
                new String[]{"name", "quantity", "price"}, new int[]{R.id.name, R.id.quantity_in_stock, R.id.price}));
    }

    private void generateProducts() {

        Thread thread = new Thread(new Runnable() {
            final List<Product> localProducts = new ArrayList<>();
            @Override
            public void run() {
                localProducts.addAll(productRoomDao.findAll());
                if(localProducts.isEmpty()) {
                    productRoomDao.insert(new Product("Galaxy S21", "Samsung Galaxy S21", 800000, 100, 10));
                    productRoomDao.insert(new Product("Galaxy Note 10", "Samsung Galaxy Note 10", 800000, 100, 10));
                    productRoomDao.insert(new Product("Redmi S11", "Xiaomi Redmi S11", 300000, 100, 10));
                    productRoomDao.insert(new Product("Galaxy S21", "Samsung Galaxy S21", 800000, 100, 10));
                    productRoomDao.insert(new Product("Galaxy S21", "Samsung Galaxy S21", 800000, 100, 10));
                    productRoomDao.insert(new Product("Galaxy S21", "Samsung Galaxy S21", 800000, 100, 10));
                    productRoomDao.insert(new Product("Galaxy S21", "Samsung Galaxy S21", 800000, 100, 10));

                    localProducts.addAll(productRoomDao.findAll());
                }
                runOnUiThread(()->{
                    products.addAll(localProducts);
                    productAdapter.notifyDataSetChanged();
                });
            }
        });
        thread.start();
    }
}