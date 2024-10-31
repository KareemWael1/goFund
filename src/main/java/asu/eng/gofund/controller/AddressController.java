package asu.eng.gofund.controller;

import asu.eng.gofund.model.Address;
import asu.eng.gofund.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AddressController {


    public AddressController() {
    }

    public int createAddress(Address address) {
        return address.saveAddress();
    }
}
