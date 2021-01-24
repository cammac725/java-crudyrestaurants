package com.lambdaschool.crudyrestaurants.controllers;

import com.lambdaschool.crudyrestaurants.models.Menu;
import com.lambdaschool.crudyrestaurants.services.MenuServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuServices menuServices;

    // http://localhost:2019/menus/menus
    @GetMapping(value = "/menus", produces = "application/json")
    public ResponseEntity<?> listAllMenus() {
        List<Menu> myMenus = menuServices.findAllMenus();
        return new ResponseEntity<>(myMenus, HttpStatus.OK);
    }

}
