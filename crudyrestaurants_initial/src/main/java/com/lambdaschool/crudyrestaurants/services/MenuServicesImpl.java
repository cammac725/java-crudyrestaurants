package com.lambdaschool.crudyrestaurants.services;

import com.lambdaschool.crudyrestaurants.models.Menu;
import com.lambdaschool.crudyrestaurants.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "menuServices")
public class MenuServicesImpl implements MenuServices{

    @Autowired
    private MenuRepository menurepos;

    @Override
    public List<Menu> findAllMenus() {
        List<Menu> myMenus = new ArrayList<>();
        menurepos.findAll().iterator().forEachRemaining(myMenus::add);
        return myMenus;
    }

    @Transactional
    @Override
    public Menu save(Menu menu) {
        return menurepos.save(menu);
    }
}
