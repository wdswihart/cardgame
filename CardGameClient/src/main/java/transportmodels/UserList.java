package transportmodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserList implements Serializable {
    private List<client.model.User> mUsers = new ArrayList<>();

    public List<client.model.User> getUsers() {
        return mUsers;
    }
}
