package io.itman.admin.controller;

import io.itman.admin.service.HomePageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/*首页数据*/
@Controller
@Slf4j
@RequestMapping(value = "homePage")
public class HomePageController {

    @Autowired
    private HomePageService homePageService;

    /*首页数据*/
    @GetMapping(value = "myCenterPage")
    @ResponseBody
    public Map HomePageData() {
        return homePageService.homePageData();

    }
}
