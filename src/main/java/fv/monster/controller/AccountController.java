package fv.monster.controller;

import fv.monster.model.Account;
import fv.monster.service.AccountService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author fvsaddam - saddamtbg@gmail.com
 */
@Controller
public class AccountController {
    
    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;

    @RequestMapping("/login")
    public String login(Account account) {
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Account account) {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(@Valid Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        Account registeredUser = accountService.register(account);
        if (registeredUser != null) {
            accountService.autoLogin(account);
            return "redirect:/materi/list";
        } else {
            // log.error("User already exists: " + user.getUserName());
            result.rejectValue("email", "error.alreadyExists", "This username or email already exists, please try to reset password instead.");
            return "register";
        }
    }
}
