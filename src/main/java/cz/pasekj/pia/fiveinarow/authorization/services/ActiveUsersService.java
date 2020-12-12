package cz.pasekj.pia.fiveinarow.authorization.services;

import java.util.List;

public interface ActiveUsersService {
    List<String> getLoggedInUsers();
}
