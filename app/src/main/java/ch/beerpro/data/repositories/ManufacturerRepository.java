package ch.beerpro.data.repositories;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import ch.beerpro.domain.models.Manufacturer;
import ch.beerpro.domain.utils.FirestoreQueryLiveData;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.zip;

public class ManufacturerRepository {

    private final FirestoreQueryLiveDataArray<Manufacturer> allManufacturers =
            new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(Manufacturer.COLLECTION), Manufacturer.class);

    private static LiveData<Manufacturer> getManufacturerById(String manufacturerId) {
        return new FirestoreQueryLiveData<>(
                FirebaseFirestore.getInstance().collection(Manufacturer.COLLECTION).document(manufacturerId), Manufacturer.class);
    }

    public LiveData<List<Manufacturer>> getAllManufacturers() {
        return allManufacturers;
    }

    public LiveData<Manufacturer> getManufacturer(LiveData<String> manufacturerId) {
        return switchMap(manufacturerId, ManufacturerRepository::getManufacturerById);
    }

    public LiveData<Manufacturer> getManufacturerByName(String name) {
        return map(allManufacturers, manufacturers -> {
            for(Manufacturer manufacturer : manufacturers){
                if(manufacturer.getName() != null && manufacturer.getName().equals(name)){
                    return manufacturer;
                }
            }
            return null;
        });
    }

}
