package cz.pasekj.pia.fiveinarow.admin;

import lombok.Getter;

/**
 * Simple crate class representing result of the AdminService
 * The class is serialized to JSON as an output of REST API
 */
@Getter
public class AdminServiceResult {

    /** Boolean flag indicating success of an operation */
    public boolean success;
    /** Error message in case success == false */
    public String errorMessage;

}
