package com.lambdaschool.crudyrestaurants.controllers;

import com.lambdaschool.crudyrestaurants.models.Restaurant;
import com.lambdaschool.crudyrestaurants.services.RestaurantServices;
import com.lambdaschool.crudyrestaurants.views.MenuCounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantServices restaurantServices;

    // C => POST
    // R => GET
    // U =>
    // D => DELETE

    // READ operations (GET requests)

    // http://localhost:2019/restaurants/restaurants
    @GetMapping(value = "/restaurants", produces = "application/json")
    public ResponseEntity<?> listAllRestaurants() {
        List<Restaurant> myList = restaurantServices.findAllRestaurants();
        return new ResponseEntity<>(myList, HttpStatus.OK);
    }

    // http://localhost:2019/restaurants/restaurant/10
    @GetMapping(value = "/restaurant/{restaurantId}", produces = "application/json")
    public ResponseEntity<?> findRestaurantById(@PathVariable long restaurantId) {
        Restaurant r = restaurantServices.findRestaurantById(restaurantId);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    // http://localhost:2019/restaurants/name/Good%20Eats
    @GetMapping(value = "/name/{restaurantName}", produces = "application/json")
    public ResponseEntity<?> findRestaurantByName(@PathVariable String restaurantName) {
        Restaurant r = restaurantServices.findRestaurantByName(restaurantName);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    // http://localhost:2019/restaurants/likename/cafe
    @GetMapping(value = "/likename/{subname}", produces = "application/json")
    public ResponseEntity<?> findRestaurantByNameLike(@PathVariable String subname) {
        List<Restaurant> rtnList = restaurantServices.findByNameLike(subname);
        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

    // http://localhost:2019/restaurants/menucounts
    @GetMapping(value = "/menucounts", produces = "application/json")
    public ResponseEntity<?> countMenuCounts() {
        List<MenuCounts> rtnList = restaurantServices.countMenusByRestaurant();
        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

    // http://localhost:2019/restaurants/likedish/cheese
    @GetMapping(value = "/likedish/{dish}", produces = "application/json")
    public ResponseEntity<?> findRestaurantsByDish(@PathVariable String dish) {
        List<Restaurant> rtnList = restaurantServices.findRestaurantByDish(dish);
        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

    // DELETE operations (DELETE)

    // http://localhost:2019/restaurants/restaurant/{id}
    @DeleteMapping(value = "restaurant/{restaurantid}")
    public ResponseEntity<?> deleteRestaurantById(@PathVariable long restaurantid) {
        restaurantServices.delete(restaurantid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // CREATE operations (POST)

    // http://localhost:2019/restaurants/restaurant
    // Data => request body
    @PostMapping(value = "/restaurant", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addNewRestaurant(@Valid @RequestBody Restaurant newRestaurant) {
        newRestaurant = restaurantServices.save(newRestaurant);

        // Response Headers -> Location Header = url to the new restaurant
        // GET http://localhost:2019/restaurants/restaurant/{newId}
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newRestaurantURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{restaurantid}")
                .buildAndExpand(newRestaurant.getRestaurantid())
                .toUri();
        responseHeaders.setLocation(newRestaurantURI);
        return new ResponseEntity<>(newRestaurant, responseHeaders, HttpStatus.CREATED);
    }


}
