package ra.com.br.weather;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.text.WordUtils;

public class WeatherActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Weather> {

    public static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnectionAndGetLoader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Weather> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String location = sharedPreferences.getString(getString(R.string.settings_location_key), getString(R.string.settings_location_default_value));
        String unit = sharedPreferences.getString(getString(R.string.settings_unit_key), getString(R.string.settings_unit_default_value));
        return new WeatherLoader(this, Utils.createURL(this, location, unit));
    }

    @Override
    public void onLoadFinished(Loader<Weather> loader, Weather data) {
        View progressBarLoading = findViewById(R.id.progress_bar_loading);
        progressBarLoading.setVisibility(View.GONE);
        loadViews(data);
    }

    @Override
    public void onLoaderReset(Loader<Weather> loader) {
        loadViews(new Weather());
    }

    private void checkConnectionAndGetLoader() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
            if(!getLoaderManager().getLoader(LOADER_ID).isReset()) {
                getLoaderManager().restartLoader(LOADER_ID, null, this);
            }
        } else {
            TextView textViewEmpty = (TextView) findViewById(R.id.text_view_empty);
            textViewEmpty.setText(R.string.no_internet_connection);

            ImageView imageView = (ImageView) findViewById(R.id.image_view_weather);
            imageView.setVisibility(View.GONE);

            View progressBarLoading = findViewById(R.id.progress_bar_loading);
            progressBarLoading.setVisibility(View.GONE);
        }
    }

    private void loadViews(Weather currentWeather) {
        TextView textViewLocation = (TextView) findViewById(R.id.text_view_location);
        textViewLocation.setText(currentWeather.getLocation());

        TextView textViewLastUpdate = (TextView) findViewById(R.id.text_view_last_update);
        textViewLastUpdate.setText(Utils.getFormattedDateTime(currentWeather.getLastUpdate(), Utils.FORMAT_DATE_TIME));

        TextView textViewDescription = (TextView) findViewById(R.id.text_view_description);
        textViewDescription.setText(WordUtils.capitalize(currentWeather.getDescription()));

        TextView textViewTemperature = (TextView) findViewById(R.id.text_view_temperature);
        textViewTemperature.setText(String.valueOf(currentWeather.getTemperature()));

        TextView textViewUnit = (TextView) findViewById(R.id.text_view_unit);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String unit = sharedPreferences.getString(getString(R.string.settings_unit_key), getString(R.string.settings_unit_default_value));

        textViewUnit.setText(unit.equals(getString(R.string.settings_unit_celsius_value)) ? R.string.unity_celsius : R.string.unity_fahrenheit);

        TextView textViewHumidity = (TextView) findViewById(R.id.text_view_humidity);
        textViewHumidity.setText(getString(R.string.humidity)
                .concat(" ").concat(String.valueOf(currentWeather.getHumidity())
                        .concat(getString(R.string.percent))));

        ImageView imageView = (ImageView) findViewById(R.id.image_view_weather);

        if (currentWeather.getIcon() != null) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(Utils.getIconId(currentWeather.getIcon()));
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
}
