package asu.eng.gofund.view;

import asu.eng.gofund.controller.AddressController;
import asu.eng.gofund.controller.UserController;
import asu.eng.gofund.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/address")
public class AddressView {

    @Autowired
    private AddressController addressController;


    public AddressView() {
    }

    @PostMapping
    public String createAddress(@RequestBody Address address) {
        int result = addressController.createAddress(address);
        if (result == 1) {
            return "Address created successfully";
        } else {
            return "Address creation failed";
        }
    }
}
