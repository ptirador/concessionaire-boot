package com.ptirador.concessionaire.service;

import com.ptirador.concessionaire.model.Menu;
import com.ptirador.concessionaire.model.MenuWrapper;
import com.ptirador.concessionaire.repository.MenuRepository;
import com.ptirador.concessionaire.util.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ptirador
 */
@Service
public class MenuServiceImpl implements MenuService {

    /**
     * Data access object to menu.
     */
    private final MenuRepository menuRepository;

    /**
     * Constructor.
     *
     * @param menuRepository Data access object to menu.
     */
    public MenuServiceImpl(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * Obtains a list of menus by role id.
     *
     * @param roleId Role identificator.
     * @return Map, key is the menu type id, and value the list of associated menus.
     */
    @Override
    public List<MenuWrapper> getMenusList(int roleId) {
        List<MenuWrapper> menuWrappers = new ArrayList<>();

        MenuWrapper menuWrapperGeneral = getMenu(Constants.MENU_GENERAL_ID, roleId);
        menuWrappers.add(menuWrapperGeneral);

        MenuWrapper menuWrapperAdmin = getMenu(Constants.MENU_ADMIN_ID, roleId);
        menuWrappers.add(menuWrapperAdmin);

        return menuWrappers;
    }

    /**
     * Obtains a menu by its type and role id.
     *
     * @param typeId Menu type identifiator.
     * @param roleId Role identificator.
     * @return Menu wrapper.
     */
    private MenuWrapper getMenu(Integer typeId, Integer roleId) {
        String parentId = "0";
        List<Menu> menus = menuRepository.getMenusByParent(typeId, roleId, parentId);

        for (Menu menu : menus) {
            manageChildren(menu, typeId, roleId);
        }

        return new MenuWrapper(typeId, menus);
    }

    /**
     * @param menu
     * @param typeId
     * @param roleId
     */
    private void manageChildren(Menu menu, Integer typeId, Integer roleId) {
        List<Menu> children = menuRepository.getMenusByParent(typeId, roleId, menu.getId());

        for (Menu child : children) {
            manageChildren(child, typeId, roleId);
        }

        menu.setChildren(children);
    }
}
