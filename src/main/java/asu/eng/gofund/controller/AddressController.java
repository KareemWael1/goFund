package asu.eng.gofund.controller;

import asu.eng.gofund.model.Address;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/address")
public class AddressController {


    public AddressController() {
    }

    @PostMapping
    public String createAddress(@RequestBody Address address) {
        int result = address.saveAddress();
        if (result == 1) {
            return "Address created successfully";
        } else {
            return "Address creation failed";
        }
    }

}
