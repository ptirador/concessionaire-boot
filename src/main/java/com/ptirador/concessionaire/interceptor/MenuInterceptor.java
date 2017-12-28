package com.ptirador.concessionaire.interceptor;

import com.ptirador.concessionaire.model.MenuWrapper;
import com.ptirador.concessionaire.model.User;
import com.ptirador.concessionaire.service.MenuService;
import com.ptirador.concessionaire.util.Constants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.ptirador.concessionaire.util.Constants.MENU_ADMIN_ID;
import static com.ptirador.concessionaire.util.Constants.MENU_GENERAL_ID;


/**
 * Set the necessary values to get the menu.
 *
 * @author ptirador
 */
@Component
public class MenuInterceptor extends HandlerInterceptorAdapter {

    /**
     * Session attribute for a list of menus.
     */
    private static final String S_MENUS_LIST = "menusList";

    /**
     * Session attribute for a menu.
     */
    private static final String S_MENU = "menu";
    /**
     * Administration URL.
     */
    private static final String ADMIN_URL = "/administration";
    /**
     * Service for menu management.
     */
    private final MenuService menuService;

    /**
     * Constructor.
     *
     * @param menuService Service for menu management.
     */
    public MenuInterceptor(final MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * It executes before the controller and gets the menu.
     *
     * @param request  HTTP request.
     * @param response HTTP response.
     * @param handler  Handler object.
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        HttpSession session = request.getSession();
        if (null != session) {
            User sessionUser = (User) session.getAttribute(Constants.USER_LOGIN);
            if (null != sessionUser) {
                @SuppressWarnings("unchecked")
                List<MenuWrapper> menusList = (List<MenuWrapper>) session.getAttribute(S_MENUS_LIST);

                // If it is the first time I access, I get the Map with the 2 possible types.
                if (menusList == null) {
                    int roleId = sessionUser.getRole();
                    menusList = menuService.getMenusList(roleId);
                    session.setAttribute(S_MENUS_LIST, menusList);
                }

                String path = request.getRequestURL().toString();
                Integer menuTypeId = path.contains(ADMIN_URL) ? MENU_ADMIN_ID : MENU_GENERAL_ID;

                session.setAttribute(S_MENU, getMenusByType(menusList, menuTypeId));

            }
        }
        return true;
    }

    /**
     * @param menuWrapperList
     * @param typeId
     * @return
     */
    private MenuWrapper getMenusByType(List<MenuWrapper> menuWrapperList, Integer typeId) {
        for (MenuWrapper menuWrapper : menuWrapperList) {
            if (typeId.equals(menuWrapper.getTypeId())) {
                return menuWrapper;
            }
        }

        return null;
    }

}
