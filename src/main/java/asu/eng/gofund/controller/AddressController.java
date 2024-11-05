package asu.eng.gofund.controller;

import asu.eng.gofund.model.Address;
import org.springframework.stereotype.Controller;

@Controller
public class AddressController {


    public AddressController() {
    }

    public int createAddress(Address address) {
        return address.saveAddress();
    }
}
