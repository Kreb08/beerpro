package ch.beerpro.presentation.explore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.GlideApp;
import ch.beerpro.R;
import ch.beerpro.domain.models.Manufacturer;
import ch.beerpro.presentation.utils.ManufacturerDiffItemCallback;


/**
 * This class is really similar to {@link BeerCategoriesRecyclerViewAdapter} see the documentation there.
 */
public class BeerManufacturersRecyclerViewAdapter
        extends ListAdapter<Manufacturer, BeerManufacturersRecyclerViewAdapter.ViewHolder> {

    private final BeerManufacturersFragment.OnItemSelectedListener listener;

    public BeerManufacturersRecyclerViewAdapter(BeerManufacturersFragment.OnItemSelectedListener listener) {
        super(new ManufacturerDiffItemCallback<>());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_explore_beer_manufacturers_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bind(getItem(position), position, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content)
        TextView content;

        @BindView(R.id.imageView)
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(Manufacturer manufacturer, int position, BeerManufacturersFragment.OnItemSelectedListener listener) {
            content.setText(manufacturer.getName());

            GlideApp.with(itemView.getContext()).load(manufacturer.getPhoto()).into(imageView);

            if (listener != null) {
                itemView.setOnClickListener(v -> listener.onBeerManufacturerSelected(manufacturer.getName()));
            }
        }
    }
}
