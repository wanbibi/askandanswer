package com.wanzhengchao.controller;

import com.wanzhengchao.aspect.LogAspect;
import com.wanzhengchao.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Administrator on 16.10.12.
 */
//@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @RequestMapping(path = {"/", "/index"}, method = RequestMethod.GET)
    @ResponseBody
    public String index(HttpSession session) {
        logger.info("vist home");
        return "hello wanzhengchao" + session.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", required = false) int type,
                          @RequestParam(value = "key", defaultValue = "hello", required = true) String key) {
        return String.format("Profile Page of %d / %s, type:%d,key:%s", userId, groupId, type, key);
    }

    @RequestMapping(path = "/vm", method = RequestMethod.GET)
    public String template(Model model) {
        model.addAttribute("value1", "vvvv1");
        List<String> list = Arrays.asList(new String[]{"red", "green"});
        model.addAttribute("colors", list);

        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);
        //model.addAttribute("user", new User("HI"));
        return "home";
    }

    @RequestMapping(path = "/request", method = RequestMethod.GET)
    @ResponseBody
    public String request(Model model, HttpServletResponse resp,
                          HttpServletRequest req,
                          HttpSession session,
                          @CookieValue("JSESSIONID") String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("cookieValue" + id);
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ":" + req.getHeader(name) + "<br>");
        }
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                sb.append("cookie:" + c.getName() + c.getValue());
            }
        }
        sb.append(req.getMethod() + "<br>");
        sb.append(req.getPathInfo() + "<br>");
        sb.append(req.getRequestURI() + "<br>");

        resp.addHeader("idid", "hello");
        resp.addCookie(new Cookie("loginUser", "wanzhengchao"));

        return sb.toString();
    }

    @RequestMapping(path = "/redirect/{code}", method = RequestMethod.GET)
    public RedirectView redirect(@PathVariable("code") int code, HttpSession session) {
        session.setAttribute("msg", "session");
        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

    @RequestMapping(path = "/admin", method = RequestMethod.GET)
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if (key.equals("admin")) {
            return "hello";
        }
        throw new IllegalArgumentException("wrong param");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e) {
        return "error:　" + e.getMessage();
    }

}
