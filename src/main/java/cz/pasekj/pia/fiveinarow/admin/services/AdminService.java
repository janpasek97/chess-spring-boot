package cz.pasekj.pia.fiveinarow.admin.services;

import cz.pasekj.pia.fiveinarow.admin.AdminServiceResult;

import java.util.List;

public interface AdminService {
    AdminServiceResult changePassword(String email, String password);
    AdminServiceResult changeUsername(String email, String username);
    AdminServiceResult changeRoles(String email, List<String> roles);
}
