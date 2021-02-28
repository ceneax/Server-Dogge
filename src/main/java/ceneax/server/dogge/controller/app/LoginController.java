package ceneax.server.dogge.controller.app;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 控制器 登录
 * Author: ceneax
 * Website: ceneax.com
 * Date: 2021/2/28 19:44
 */
@RestController
@RequestMapping("/app")
public class LoginController {

    @PostMapping("/login")
    public String login() {
        return "login success";
    }

}
