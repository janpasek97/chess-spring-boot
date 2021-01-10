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

/**
 * Controller used for handling admin page requests
 */
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    /** Admin service instance used to change user information */
    private final AdminService adminService;

    /**
     * Change user details based on Rest API request formatted in JSON
     * @param jsString new user information in JSON format
     * @return instance of AdminServiceResults with information about action result
     */
    @PostMapping("/admin/api")
    AdminServiceResult changeUserDetails(@RequestBody String jsString) {
        // parse the input data
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

        // perform necessary changes
        result = adminService.changeUsername(email, username);
        if(!result.success) return result;
        result = adminService.changeRoles(email, roles);
        if(!result.success) return result;
        if(password.length() > 0) result = adminService.changePassword(email, password);
        return result;
    }

}
