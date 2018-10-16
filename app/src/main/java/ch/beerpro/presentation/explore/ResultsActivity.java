package ch.beerpro.presentation.explore;

import android.app.ActivityOptions;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.presentation.details.DetailsActivity;
import ch.beerpro.presentation.explore.search.SearchViewModel;
import ch.beerpro.presentation.explore.search.ViewPagerAdapter;
import ch.beerpro.presentation.explore.search.beers.SearchResultFragment;
import ch.beerpro.presentation.explore.search.suggestions.SearchSuggestionsFragment;
import ch.beerpro.presentation.profile.mybeers.MyBeersViewModel;
import ch.beerpro.presentation.profile.mybeers.OnMyBeerItemInteractionListener;
import com.google.android.material.tabs.TabLayout;
import com.google.common.base.Strings;

public class ResultsActivity extends AppCompatActivity
        implements SearchResultFragment.OnItemSelectedListener, SearchSuggestionsFragment.OnItemSelectedListener,
        OnMyBeerItemInteractionListener {

    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private SearchViewModel searchViewModel;
    private MyBeersViewModel myBeersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ViewPager viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setSaveFromParentEnabled(false);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        myBeersViewModel = ViewModelProviders.of(this).get(MyBeersViewModel.class);
    }

    @Override
    public void onStart(){
        super.onStart();
        String category = getIntent().getExtras().getString("category");
        String manufacturer = getIntent().getExtras().getString("manufacturer");

        searchViewModel.setSearchTerms(new String[]{null, category, manufacturer});
        myBeersViewModel.setSearchTerms(new String[]{null, category, manufacturer});
        adapter.setShowSuggestions(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchResultListItemSelected(View animationSource, Beer item) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.ITEM_ID, item.getId());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, animationSource, "image");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onSearchSuggestionListItemSelected(String text) {

    }

    @Override
    public void onMoreClickedListener(ImageView photo, Beer item) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.ITEM_ID, item.getId());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, photo, "image");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onWishClickedListener(Beer item) {
        searchViewModel.toggleItemInWishlist(item.getId());
    }
}