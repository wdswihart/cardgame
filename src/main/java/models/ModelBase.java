package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Observable;

public abstract class ModelBase implements Serializable {
    // METHODS:

    public ModelBase() {
    }

    @JsonIgnore
    public abstract boolean isDefault();
}
