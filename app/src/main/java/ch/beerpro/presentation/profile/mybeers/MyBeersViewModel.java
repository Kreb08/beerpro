package ch.beerpro.presentation.profile.mybeers;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.beerpro.data.repositories.*;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;
import ch.beerpro.domain.models.MyBeer;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static androidx.lifecycle.Transformations.map;
import static ch.beerpro.domain.utils.LiveDataExtensions.zip;

public class MyBeersViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "MyBeersViewModel";
    private final MutableLiveData<String[]> searchTerms = new MutableLiveData<>();

    private final WishlistRepository wishlistRepository;
    private final LiveData<List<MyBeer>> myFilteredBeers;

    public MyBeersViewModel() {

        wishlistRepository = new WishlistRepository();
        BeersRepository beersRepository = new BeersRepository();
        MyBeersRepository myBeersRepository = new MyBeersRepository();
        RatingsRepository ratingsRepository = new RatingsRepository();

        LiveData<List<Beer>> allBeers = beersRepository.getAllBeers();
        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        LiveData<List<Wish>> myWishlist = wishlistRepository.getMyWishlist(currentUserId);
        LiveData<List<Rating>> myRatings = ratingsRepository.getMyRatings(currentUserId);

        LiveData<List<MyBeer>> myBeers = myBeersRepository.getMyBeers(allBeers, myWishlist, myRatings);

        myFilteredBeers = map(zip(searchTerms, myBeers), MyBeersViewModel::filter);

        currentUserId.setValue(getCurrentUser().getUid());
    }

    private static List<MyBeer> filter(Pair<String[], List<MyBeer>> input) {
        List<MyBeer> myBeers = input.second;
        if (myBeers == null) {
            return Collections.emptyList();
        }

        if(input.first == null){
            return myBeers;
        }
        String searchTerm1 = input.first[0];
        String searchCatetegory = input.first[1];
        String searchManufacturer = input.first[2];

        List<MyBeer> filtered = filterSearchTerm(searchTerm1, myBeers);
        filtered = filterCategory(searchCatetegory, filtered);
        filtered = filterManufacturer(searchManufacturer,filtered);

        return filtered;
    }

    private static List<MyBeer> filterSearchTerm(String name, List<MyBeer> beers){
        if (Strings.isNullOrEmpty(name)) {
            return beers;
        }
        if (beers == null) {
            return Collections.emptyList();
        }
        ArrayList<MyBeer> filtered = new ArrayList<>();
        for (MyBeer beer : beers) {
            if(beer.getBeer().getName() != null) {
                if(beer.getBeer().getName().toLowerCase().contains(name.toLowerCase())){
                    filtered.add(beer);
                }
            }
        }
        return filtered;
    }

    private static List<MyBeer> filterCategory(String category, List<MyBeer> beers){
        if (Strings.isNullOrEmpty(category)) {
            return beers;
        }
        if (beers == null) {
            return Collections.emptyList();
        }
        ArrayList<MyBeer> filtered = new ArrayList<>();
        for (MyBeer beer : beers) {
            if(beer.getBeer().getCategory() != null) {
                if(beer.getBeer().getCategory().toLowerCase().contains(category.toLowerCase())){
                    filtered.add(beer);
                }
            }
        }
        return filtered;
    }

    private static List<MyBeer> filterManufacturer(String manufacturer, List<MyBeer> beers){
        if (Strings.isNullOrEmpty(manufacturer)) {
            return beers;
        }
        if (beers == null) {
            return Collections.emptyList();
        }
        ArrayList<MyBeer> filtered = new ArrayList<>();
        for (MyBeer beer : beers) {
            if(beer.getBeer().getManufacturer() != null) {
                if(beer.getBeer().getManufacturer().toLowerCase().contains(manufacturer.toLowerCase())){
                    filtered.add(beer);
                }
            }
        }
        return filtered;
    }

    public LiveData<List<MyBeer>> getMyFilteredBeers() {
        return myFilteredBeers;
    }

    public void toggleItemInWishlist(String beerId) {
        wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), beerId);
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerms.setValue(new String[]{searchTerm, null, null});
    }

    public void setSearchTerms(String[] searchTerms){
        this.searchTerms.setValue(searchTerms);
    }
}