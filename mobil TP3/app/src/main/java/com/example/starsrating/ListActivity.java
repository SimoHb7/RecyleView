package com.example.starsrating;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starsrating.Beans.Star;
import com.example.starsrating.Service.StarService;
import com.example.starsrating.Adapter.StarAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private List<Star> stars;
    private RecyclerView recyclerView;
    private StarAdapter starAdapter = null;
    private StarService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        stars = new ArrayList<>();
        service = StarService.getInstance();
        init();
        recyclerView = findViewById(R.id.recycle_view);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        //insérer le code
        starAdapter = new StarAdapter(this, service.findAll());
        recyclerView.setAdapter(starAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Rechercher...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                starAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share_message) {
            Intent smsIntent = new Intent(Intent.ACTION_SEND);
            smsIntent.setType("text/plain");
            smsIntent.putExtra(Intent.EXTRA_TEXT, "Découvrez ces stars !");
            startActivity(Intent.createChooser(smsIntent, "Partager via"));
            return true;

        } else if (id == R.id.action_share_bluetooth) {
            Intent bluetoothIntent = new Intent(Intent.ACTION_SEND);
            bluetoothIntent.setType("text/plain");
            bluetoothIntent.putExtra(Intent.EXTRA_TEXT, "Découvrez ces stars !");
            bluetoothIntent.setPackage("com.android.bluetooth");
            startActivity(Intent.createChooser(bluetoothIntent, "Partager via"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void init(){
        service.create(new Star("kate bosworth", "https://images.pexels.com/photos/19305958/pexels-photo-19305958/free-photo-of-rhume-froid-mode-gens.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 3.5f));
        service.create(new Star("george clooney", "https://images.pexels.com/photos/7005629/pexels-photo-7005629.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 3));
        service.create(new Star("kit harington",
                "https://images.pexels.com/photos/31208515/pexels-photo-31208515/free-photo-of-ator-roteiro.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 5));
        service.create(new Star("cillian murphy", "https://images.pexels.com/photos/6899935/pexels-photo-6899935.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 1));
        service.create(new Star("Maisie williams", "https://images.pexels.com/photos/18977034/pexels-photo-18977034/free-photo-of-robes-occidentales-prise-de-vue-par-dhanno.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 5));
        service.create(new Star("sophie turner", "https://images.pexels.com/photos/19292779/pexels-photo-19292779/free-photo-of-robe-occidentale-2024-shoot-par-dhanno-mayra-jaffri.jpeg", 1));
    }
}
