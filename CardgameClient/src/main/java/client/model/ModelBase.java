package client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Observable;

public abstract class ModelBase extends Observable implements Serializable {
    // METHODS:

    @JsonIgnore
    public abstract boolean isDefault();
}
