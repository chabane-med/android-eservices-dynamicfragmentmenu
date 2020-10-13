package android.eservices.dynamicfragmentmenu;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationInterface {

    private final static String FRAGMENT_NUMBER_KEY = "Fragment_Number";
    private final static String FRAGMENT_STORED_KEY = "Fragment_Stored";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private SelectableNavigationView navigationView;
    private SparseArray<Fragment> fragmentArray;
    private Fragment currentFragment;
    private Fragment favoriteFragment;
    private Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigationElements();




        //TODO Restore instance state
        //If available :
        //1° - Retrieve the stored fragment from the saved bundle (see SO link in indications below, bottom of the class)
        //2° - Use the replace fragment to display the retrieved fragment
        //3° - Add the restored fragment to the cache so it is not recreated when selected the menu item again
        //If the bundle is null, then display the default fragment using navigationView.setSelectedItem();
        //Reminder, to get a menu item, use navigationView.getMenu().getItem(idex)

        //Let's imagine we retrieve the stored counter state, before creating the favorite Fragment
        //and then be able to update and manage its state.
        updateFavoriteCounter(3);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setupNavigationElements() {
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        fragmentArray = new SparseArray<>(3);
        selectedFragment = SelectedFragment.newInstance();
        fragmentArray.put(0,selectedFragment);
        favoriteFragment = FavoritesFragment.newInstance();
        fragmentArray.put(1,favoriteFragment);

        navigationView = findViewById(R.id.navigation);
        navigationView.inflateHeaderView(R.layout.navigation_header);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId()){
                    case R.id.list:{

                      /*  if(fragmentArray.get(0).equals(null)){
                            selectedFragment = SelectedFragment.newInstance();
                            currentFragment = selectedFragment;
                            fragmentArray.put(0,selectedFragment);

                        }else{*/

                            currentFragment = fragmentArray.get(0);
                       // }

                        break; }
                    case R.id.favorites:{
                       /* if(fragmentArray.get(1).equals(null)){
                            favoriteFragment = SelectedFragment.newInstance();
                            currentFragment = favoriteFragment;
                            fragmentArray.put(1,favoriteFragment);

                        }else{*/
                            currentFragment = fragmentArray.get(1);
                      //  }


                        break; }
                    case R.id.logoff:{
                        logoff();
                    }
                    break;



                }
                replaceFragment(currentFragment);

                //check in the doc what this boolean means and use it the right way ...
                return false;
            }
        });
    }


    private void replaceFragment(Fragment newFragment) {

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();


        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .commit();

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void logoff() {
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void updateFavoriteCounter(int counter) {
        String counterContent = counter > 9 ? getString(R.string.counter_full) : Integer.toString(counter);
        View counterView = navigationView.getMenu().getItem(1).getActionView();
        counterView.setVisibility(counter > 0 ? View.VISIBLE : View.GONE);
        ((TextView) counterView.findViewById(R.id.counter_view)).setText(counterContent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

       // savedInstanceState.putInt(FRAGMENT_NUMBER_KEY,navigationView.getCheckedItem().getOrder());
       // getSupportFragmentManager().putFragment(savedInstanceState,"myFragment",fragmentArray.get(1));
    }

    //TODO saveInstanceState to handle
    //TODO first save the currently displayed fragment index using the key FRAGMENT_NUMBER_KEY, and getOrder() on the menu item
    //Reminder, to get the selected item in the menu, we can use myNavView.getCheckedItem()
    //TODO then save the current state of the fragment, you may read https://stackoverflow.com/questions/15313598/once-for-all-how-to-correctly-save-instance-state-of-fragments-in-back-stack

}

