package com.ptirador.concessionaire.model;

import com.ptirador.concessionaire.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Wraps a list of menus organized by type.
 *
 * @author ptirador
 */
@AllArgsConstructor
@Getter
@Setter
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
}
