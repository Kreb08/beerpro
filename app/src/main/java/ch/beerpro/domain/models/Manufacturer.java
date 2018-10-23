package ch.beerpro.domain.models;

import com.google.firebase.firestore.Exclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manufacturer implements Entity, Serializable {

    public static final String COLLECTION = "manufacturer";
    public static final String FIELD_NAME = "name";

    @Exclude
    private String id;
    private String name;
    private String photo;

}
