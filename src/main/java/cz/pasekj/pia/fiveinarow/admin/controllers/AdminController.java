package cz.pasekj.pia.fiveinarow.admin.controllers;

import cz.pasekj.pia.fiveinarow.admin.AdminServiceResult;
import cz.pasekj.pia.fiveinarow.admin.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/admin/api")
    AdminServiceResult changeUserDetails(@RequestBody String jsString) {
        JSONObject jsonObject = new JSONObject(jsString);
        String username = jsonObject.getString("username");
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        JSONArray rolesArr = jsonObject.getJSONArray("roles");

        List<String> roles = new ArrayList<>();

        for (int i = 0; i < rolesArr.length(); i++) {
            roles.add(rolesArr.getString(i));
        }

        AdminServiceResult result;

        result = adminService.changeUsername(email, username);
        if(!result.success) return result;
        result = adminService.changeRoles(email, roles);
        if(!result.success) return result;
        if(password.length() > 0) result = adminService.changePassword(email, password);
        return result;
    }

}
