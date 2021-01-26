package com.lambdaschool.crudyrestaurants.services;

import com.lambdaschool.crudyrestaurants.models.Menu;
import com.lambdaschool.crudyrestaurants.models.Payment;
import com.lambdaschool.crudyrestaurants.models.Restaurant;
import com.lambdaschool.crudyrestaurants.repositories.PaymentRepository;
import com.lambdaschool.crudyrestaurants.repositories.RestaurantRepository;
import com.lambdaschool.crudyrestaurants.views.MenuCounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "restaurantServices")
public class RestaurantServicesImpl implements RestaurantServices {

    @Autowired
    private RestaurantRepository restrepos;

    @Autowired
    private PaymentRepository paymentrepos;

    @Override
    public List<Restaurant> findAllRestaurants() {
        List<Restaurant> list = new ArrayList<>();
        restrepos.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Restaurant findRestaurantById(long id) {
        return restrepos.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant " + id + " not found."));
    }

    @Override
    public Restaurant findRestaurantByName(String name) {
        Restaurant restaurant = restrepos.findByName(name);

        if (restaurant == null) {
            throw new EntityNotFoundException("Restaurant " + name + " not found.");
        }
        return restaurant;
    }

    @Override
    public List<Restaurant> findByNameLike(String thename) {
        List<Restaurant> list = restrepos.findByNameContainingIgnoringCase(thename);
        return list;
    }

    @Override
    public List<MenuCounts> countMenusByRestaurant() {
        List<MenuCounts> list = restrepos.findMenuCounts();
        return list;
    }

    @Override
    public List<Restaurant> findRestaurantByDish(String thedish) {
        List<Restaurant> list = restrepos.findByMenus_dishContainingIgnoringCase(thedish);
        return list;
    }

    // Valid data
    @Transactional
    @Override
    public Restaurant save(Restaurant restaurant) {
        Restaurant newRestaurant = new Restaurant();

        if (restaurant.getRestaurantid() != 0) {
            findRestaurantById(restaurant.getRestaurantid());
            newRestaurant.setRestaurantid(restaurant.getRestaurantid());
        }
        // single value fields
        newRestaurant.setName(restaurant.getName());
        newRestaurant.setAddress(restaurant.getAddress());
        newRestaurant.setCity(restaurant.getCity());
        newRestaurant.setState(restaurant.getState());
        newRestaurant.setTelephone(restaurant.getTelephone());
        newRestaurant.setSeatcapacity(restaurant.getSeatcapacity());

        // collections
        // payment must already exist
        newRestaurant.getPayments().clear();
        for (Payment p : restaurant.getPayments()) {
            Payment newPayment = paymentrepos.findById(p.getPaymentid())
                    .orElseThrow(() -> new EntityNotFoundException("Payment " + p.getPaymentid() + " not found."));
            newRestaurant.getPayments().add(newPayment);
        }

        newRestaurant.getMenus().clear();
        for (Menu m : restaurant.getMenus()) {
            Menu newMenu = new Menu(m.getDish(), m.getPrice(), newRestaurant);
            newRestaurant.getMenus().add(newMenu);
        }

        // primary key, id = 0 ?, it does an Add
        //                != 0 ?, it does an Update (delete then add)
        return restrepos.save(newRestaurant);
    }

    @Transactional
    @Override
    public Restaurant update(Restaurant restaurant, long restid) {
        Restaurant updateRestaurant = findRestaurantById(restid);

        // single value fields

        if(restaurant.getName() != null) {
            updateRestaurant.setName(restaurant.getName());
        }
        if (restaurant.getAddress() != null) {
            updateRestaurant.setAddress(restaurant.getAddress());
        }
        if (restaurant.getCity() != null) {
            updateRestaurant.setCity(restaurant.getCity());
        }
        if (restaurant.getState() != null) {
            updateRestaurant.setState(restaurant.getState());
        }
        if (restaurant.getTelephone() != null) {
            updateRestaurant.setTelephone(restaurant.getTelephone());
        }
        if (restaurant.hasvalueforseatcapacity) {
            updateRestaurant.setSeatcapacity(restaurant.getSeatcapacity());
        }

        // collections
        // payment must already exist
        if (restaurant.getPayments().size() > 0) {
            updateRestaurant.getPayments().clear();
            for (Payment p : restaurant.getPayments()) {
                Payment newPayment = paymentrepos.findById(p.getPaymentid())
                        .orElseThrow(() -> new EntityNotFoundException("Payment " + p.getPaymentid() + " not found."));
                updateRestaurant.getPayments().add(newPayment);
            }
        }

        if (restaurant.getMenus().size() > 0) {
            updateRestaurant.getMenus().clear();
            for (Menu m : restaurant.getMenus()) {
                Menu newMenu = new Menu(m.getDish(), m.getPrice(), updateRestaurant);
                updateRestaurant.getMenus().add(newMenu);
            }
        }

        return restrepos.save(updateRestaurant);
    }

    @Transactional
    @Override
    public void deleteAllRestaurants() {
        restrepos.deleteAll();
    }

    @Transactional
    @Override
    public void delete(long restaurantid) {
//        if (restrepos.findById(restaurantid).isPresent()) {
//            restrepos.deleteById(restaurantid);
        Restaurant r = findRestaurantById(restaurantid);
        if (r != null) {
            restrepos.deleteById(restaurantid);
        } else {
            throw new EntityNotFoundException("Restaurant " + restaurantid + " not found.");
        }
    }
}
