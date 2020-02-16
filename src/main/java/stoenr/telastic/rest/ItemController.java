package stoenr.telastic.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import stoenr.telastic.service.ItemService;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

}
