package com.github.bootsrc.bootpush.server.controller;

import com.github.bootsrc.bootpush.server.biz.PushUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ApiController {

    @RequestMapping("/push")
    public String push(String regId, String msg) {
        PushUtil.push(regId, msg);
        return "push success";
    }
}
