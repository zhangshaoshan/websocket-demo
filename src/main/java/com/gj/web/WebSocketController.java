package com.gj.web;

import com.alibaba.fastjson.JSONObject;
import com.gj.sendall.MyWebsocketServer;
import com.gj.sendone.Message;
import com.gj.sendone.MyOneToOneServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webSocket")
public class WebSocketController {

    @Autowired
    MyOneToOneServer websocketServer;

    @PostMapping("/all")
    public String sendMessageToAll(@RequestBody Message message){
        //websocketServer.onMessage(JSONObject.toJSONString(message),message.getUserId());
        return "ok";
    }

}
