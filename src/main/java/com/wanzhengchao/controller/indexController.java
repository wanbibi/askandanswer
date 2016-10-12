package com.wanzhengchao.controller;

import com.wanzhengchao.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 16.10.12.
 */
@Controller
public class indexController {
    @RequestMapping(path = {"/", "/index"}, method = RequestMethod.GET)
    @ResponseBody
    public String index() {
        return "hello wanzhengchao";
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
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("map",map);
        model.addAttribute("user", new User("HI"));
        return "home";


    }


}
