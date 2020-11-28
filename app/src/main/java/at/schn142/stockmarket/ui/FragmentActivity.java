package at.schn142.stockmarket.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import at.schn142.stockmarket.R;

/**
 * This class represents FragmentActivity
 * Activity for the Fragments(stock,compare,settings)
 *
 * @author michaelschneider
 * @version 1.0
 */
public class FragmentActivity extends AppCompatActivity {

    public static final int NEW_STOCK_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_stock, R.id.navigation_compare, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_STOCK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(FragmentActivity.this,
                    getString(R.string.add_stock_preamble) + " " +data.getStringExtra(SearchActivity.EXTRA_REPLY), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

}