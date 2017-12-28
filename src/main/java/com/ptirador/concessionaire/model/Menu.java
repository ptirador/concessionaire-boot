package com.ptirador.concessionaire.model;

import com.ptirador.concessionaire.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * Bean that represents a menu.
 *
 * @author ptirador
 */
@Document(collection = "menus")
@Getter
@Setter
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique id.
     */
    @Id
    private String id;

    /**
     * Parent id.
     */
    private String parentId;

    /**
     * Position number.
     */
    private Integer order;

    /**
     * Access URL.
     */
    private String url;

    /**
     * Alternative text.
     */
    private String alt;

    /**
     * Description text.
     */
    private String text;

    /**
     * Font awesome icon.
     */
    private String icon;

    /**
     * Menu type:
     * General menu: {@link Constants#MENU_GENERAL_ID}
     * Admin menu: {@link Constants#MENU_ADMIN_ID}
     */
    private Integer typeId;

    /**
     * Menu children.
     */
    @DBRef
    private transient List<Menu> children;
}
