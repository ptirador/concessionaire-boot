package com.ptirador.concessionaire.model;

import com.ptirador.concessionaire.util.Constants;

import java.io.Serializable;
import java.util.List;

/**
 * Wraps a list of menus organized by type.
 *
 * @author ptirador
 */
public class MenuWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Menu type:
     * General menu: {@link Constants#MENU_GENERAL_ID}
     * Admin menu: {@link Constants#MENU_ADMIN_ID}
     */
    private Integer typeId;

    /**
     * Menus list.
     */
    private List<Menu> menusList;

    /**
     * Constructor.
     *
     * @param typeId
     * @param menusList
     */
    public MenuWrapper(Integer typeId, List<Menu> menusList) {
        this.typeId = typeId;
        this.menusList = menusList;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public List<Menu> getMenusList() {
        return menusList;
    }

    public void setMenusList(List<Menu> menusList) {
        this.menusList = menusList;
    }
}
