package com.ptirador.concessionaire.service;

import com.ptirador.concessionaire.model.MenuWrapper;

import java.util.List;

/**
 * @author ptirador
 */
public interface MenuService {

    /**
     * Obtains a list of menus by role id.
     * @param roleId Role identificator.
     * @return Map, key is the menu type id, and value the list of associated menus.
     */
    List<MenuWrapper> getMenusList(int roleId);
}
