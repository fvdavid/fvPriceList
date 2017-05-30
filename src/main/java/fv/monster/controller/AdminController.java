
package fv.monster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author fvsaddam - saddamtbg@gmail.com
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    
    @GetMapping("home")
    public String adminHome() {
        return "admin/home";
    }

}
