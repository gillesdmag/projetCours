package io.artcreativity.monpremierprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import io.artcreativity.monpremierprojet.dao.DataBaseHelper;
import io.artcreativity.monpremierprojet.dao.DataBaseRoom;
import io.artcreativity.monpremierprojet.dao.ProductDao;
import io.artcreativity.monpremierprojet.dao.ProductRoomDao;
import io.artcreativity.monpremierprojet.entities.Product;
import io.artcreativity.monpremierprojet.webservices.ProductWebService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.class.getCanonicalName();

    private TextInputEditText designationEditText;
    private TextInputEditText descriptionEditText;
    private TextInputEditText priceEditText;
    private TextInputEditText quantityInStockEditText;
    private TextInputEditText alertQuantityEditText;
    int position;
    Button add;

    private boolean isname;
    private boolean isdes;
    private boolean isprice;
    private boolean isquan;
    private boolean isaltq;
    Product product;
    private ProductRoomDao productRoomDao;
    private ProductDao productDao;

    DataBaseHelper dataBaseHelper;
    DataBaseRoom dataBaseRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productDao = new ProductDao(this);
        productRoomDao = DataBaseRoom.getInstance(getApplicationContext()).productRoomDao();

        designationEditText = findViewById(R.id.name);
        descriptionEditText = findViewById(R.id.description);
        priceEditText = findViewById(R.id.price);
        quantityInStockEditText = findViewById(R.id.quantity_in_stock);
        alertQuantityEditText = findViewById(R.id.alert_quantity);

        update();

        findViewById(R.id.my_btn).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    public void saveProduct(View view) {

        if (isvalue()){

            if(product!=null){
                product.setName(designationEditText.getText().toString());
                product.setDescription(descriptionEditText.getText().toString());
                product.setPrice(Double.parseDouble(priceEditText.getText().toString()));
                product.setQuantityInStock(Double.parseDouble(quantityInStockEditText.getText().toString()));
                product.setAlertQuantity(Double.parseDouble(alertQuantityEditText.getText().toString()));

                Intent intent = getIntent();
                intent.putExtra("MY_PROD", product);
                setResult(Activity.RESULT_OK, intent);

                finish();


            }

            else  {

                Log.d(TAG, "saveProduct: ");
                Product product = new Product();
                product.name = designationEditText.getText().toString();
                product.description = descriptionEditText.getText().toString();
                product.price = Double.parseDouble(priceEditText.getText().toString());
                product.quantityInStock = Double.parseDouble(quantityInStockEditText.getText().toString());
                product.alertQuantity = Double.parseDouble(alertQuantityEditText.getText().toString());
                Log.e(TAG, "saveProduct: " + product);
                Toast.makeText(getApplicationContext(), "J'ai clique", Toast.LENGTH_SHORT).show();

                new Thread(
                        ()->{
                            ProductWebService productWebService = new ProductWebService();
                            Product save = productWebService.createProduct(product);
                            System.out.println("save :: " + save);
                            runOnUiThread(()->{
                            });
                        }
                ).start();


                Intent intent = getIntent();
                intent.putExtra("MY_PROD", product);
                setResult(Activity.RESULT_OK, intent);
                finish();

            }

        }


    }

    public boolean isvalue() {
        if (designationEditText.getText().toString().isEmpty()) {
            designationEditText.setError("designation is required");
            Toast.makeText(getApplicationContext(), "Remplissez le champ", Toast.LENGTH_SHORT).show();
            isname = false;
        } else {
            isname = true;
        }

        if (descriptionEditText.getText().toString().isEmpty()) {
            descriptionEditText.setError("description is required");
            Toast.makeText(getApplicationContext(), "Remplissez le champ", Toast.LENGTH_SHORT).show();
            isdes = false;
        } else {
            isdes = true;
        }

        if (priceEditText.getText().toString().isEmpty()) {
            priceEditText.setError("price is required");
            Toast.makeText(getApplicationContext(), "Remplissez le champ", Toast.LENGTH_SHORT).show();
            isprice = false;
        } else {
            isprice = true;
        }

        if (quantityInStockEditText.getText().toString().isEmpty()) {
            quantityInStockEditText.setError("quantityInStock is required");
            Toast.makeText(getApplicationContext(), "Remplissez le champ", Toast.LENGTH_SHORT).show();
            isquan = false;
        } else {
            isquan = true;
        }

        if (alertQuantityEditText.getText().toString().isEmpty()) {
            alertQuantityEditText.setError("Alert quantity is required");
            Toast.makeText(getApplicationContext(), "Remplissez le champ", Toast.LENGTH_SHORT).show();
            isaltq = false;
        } else {
            isaltq = true;
        }

        if (isname == true && isaltq == true && isquan == true && isprice == true && isdes == true) {
            return true;
        } else {
            return false;
        }
    }



    @Override
    public void onClick(View view) {
        saveProduct(view);
    }

    public void update(){
        product = (Product) getIntent().getSerializableExtra("MY_PROD");
        if (product!=null){
            Bundle extras=getIntent().getExtras();
            if(extras!=null){
                position = extras.getInt("position");
            }
            designationEditText.setText(product.name);
            descriptionEditText.setText(product.description);
            priceEditText.setText(""+product.price);
            quantityInStockEditText.setText(""+product.quantityInStock);
            alertQuantityEditText.setText(""+product.alertQuantity);
        }
    }
}