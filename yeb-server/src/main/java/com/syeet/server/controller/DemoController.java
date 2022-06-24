package com.syeet.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xhl
 * @date 2022/1/12
 */
@RestController
@Api(tags = "demo测试")
public class DemoController {

    @ApiOperation(value = "测试test")
    @GetMapping("/test")
    public String test(){
        return "hello";
    }
}
