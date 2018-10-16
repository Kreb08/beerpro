package ch.beerpro.presentation.explore.search;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.SearchesRepository;
import ch.beerpro.data.repositories.WishlistRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Search;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.zip;

public class SearchViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "SearchViewModel";
    private final MutableLiveData<String[]> searchTerms = new MutableLiveData<>();
    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();

    private final LiveData<List<Beer>> filteredBeers;
    private final BeersRepository beersRepository;
    private final WishlistRepository wishlistRepository;
    private final SearchesRepository searchesRepository;
    private final LiveData<List<Search>> myLatestSearches;

    public SearchViewModel() {
        beersRepository = new BeersRepository();
        wishlistRepository = new WishlistRepository();
        searchesRepository = new SearchesRepository();
        filteredBeers = map(zip(searchTerms, getAllBeers()), SearchViewModel::filter);
        myLatestSearches = switchMap(currentUserId, SearchesRepository::getLatestSearchesByUser);

        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<Beer>> getAllBeers() {
        return beersRepository.getAllBeers();
    }

    private static List<Beer> filter(Pair<String[], List<Beer>> input) {
        List<Beer> allBeers = input.second;
        if (allBeers == null) {
            return Collections.emptyList();
        }

        String searchTerm1 = input.first[0];
        String searchCategory = input.first[1];
        String searchManufacturer = input.first[2];
        if(input.first == null){
            return allBeers;
        }

        List<Beer> filtered = filterSearchTerm(searchTerm1, allBeers);
        filtered = filterCategory(searchCategory, filtered);
        filtered = filterManufacturer(searchManufacturer, filtered);
        return filtered;
    }

    private static List<Beer> filterSearchTerm(String name, List<Beer> beers){
        if (Strings.isNullOrEmpty(name)) {
            return beers;
        }
        if (beers == null) {
            return Collections.emptyList();
        }
        ArrayList<Beer> filtered = new ArrayList<>();
        for (Beer beer : beers) {
            if(beer.getName() != null) {
                if(beer.getName().toLowerCase().contains(name.toLowerCase())){
                    filtered.add(beer);
                }
            }
        }
        return filtered;
    }

    private static List<Beer> filterCategory(String category, List<Beer> beers) {
        if (Strings.isNullOrEmpty(category)) {
            return beers;
        }
        if (beers == null) {
            return Collections.emptyList();
        }
        ArrayList<Beer> filtered = new ArrayList<>();
        for (Beer beer : beers) {
            if(beer.getCategory() != null) {
                if(beer.getCategory().toLowerCase().contains(category.toLowerCase())){
                    filtered.add(beer);
                }
            }
        }
        return filtered;
    }

    private static List<Beer> filterManufacturer(String manufacturer, List<Beer> beers) {
        if (Strings.isNullOrEmpty(manufacturer)) {
            return beers;
        }
        if (beers == null) {
            return Collections.emptyList();
        }
        ArrayList<Beer> filtered = new ArrayList<>();
        for (Beer beer : beers) {
            if(beer.getManufacturer() != null) {
                if(beer.getManufacturer().toLowerCase().contains(manufacturer.toLowerCase())){
                    filtered.add(beer);
                }
            }
        }
        return filtered;
    }

    public LiveData<List<Search>> getMyLatestSearches() {
        return myLatestSearches;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerms.setValue(new String[]{searchTerm, null, null});
    }

    public void setSearchTerms(String[] searchTerms) {
        this.searchTerms.setValue(searchTerms);
    }

    public LiveData<List<Beer>> getFilteredBeers() {
        return filteredBeers;
    }


    public void toggleItemInWishlist(String beerId) {
        wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), beerId);
    }

    public void addToSearchHistory(String term) {
        searchesRepository.addSearchTerm(term);
    }
}