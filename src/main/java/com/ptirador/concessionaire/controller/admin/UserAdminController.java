package com.ptirador.concessionaire.controller.admin;

import com.ptirador.concessionaire.exception.ResourceNotFoundException;
import com.ptirador.concessionaire.model.User;
import com.ptirador.concessionaire.service.UserService;
import com.ptirador.concessionaire.util.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/administration/users")
public class UserAdminController {

    /**
     * Users JSON list URL.
     */
    private static final String URL_USERS_JSON_LIST = "/list/json";
    /**
     * User detail URL.
     */
    private static final String URL_USER_DETAIL = "/detail/{id}";
    /**
     * URL that saves a user.
     */
    private static final String URL_SAVE_USER = "/save";
    /**
     * Model attribute for a user detail.
     */
    private static final String MDL_USER = "user";
    /**
     * User detail view.
     */
    private static final String VIEW_USER_DETAIL = "administration/users/detail";
    /**
     * Message code for saving successfully a user.
     */
    private static final String MSG_USER_SAVE_OK = "user.save.ok";
    /**
     * Message code for error when saving a user.
     */
    private static final String MSG_USER_SAVE_KO = "user.save.ko";
    /**
     * User service.
     */
    private final UserService userService;

    /**
     * Controller.
     *
     * @param userService user service.
     */
    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @return
     */
    @GetMapping(value = URL_USERS_JSON_LIST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }

    /**
     * User detail.
     *
     * @param id    user identificator.
     * @param model form model.
     * @return detail page.
     */
    @GetMapping(URL_USER_DETAIL)
    public String getUserDetail(@PathVariable String id,
                                Model model) {
        Optional<User> user = userService.findById(id);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException();
        }

        model.addAttribute(MDL_USER, user.get());
        return VIEW_USER_DETAIL;
    }

    /**
     * Saves an existing user.
     *
     * @param user   user to save.
     * @param result object containing the binding model.
     * @param rda    attributes for redirection.
     * @return destination page.
     */
    @PutMapping(URL_SAVE_USER)
    public String saveUser(@ModelAttribute(MDL_USER) @Valid User user,
                          BindingResult result,
                          RedirectAttributes rda) {

        String resultPage = VIEW_USER_DETAIL;

        if (!result.hasErrors()) {
            userService.save(user);
            rda.addAttribute("id", user.getId());
            rda.addFlashAttribute(Constants.MSG, MSG_USER_SAVE_OK);
            resultPage = Constants.REDIRECT + "/administration/users" + URL_USER_DETAIL;
        }

        return resultPage;
    }
}
