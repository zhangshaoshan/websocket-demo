package com.gj.sendone;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Gjing
 **/
@ServerEndpoint("/test-one")
@Component
@Slf4j
public class MyOneToOneServer {
    /**
     * 用于存放所有在线客户端
     */
    private static Map<String, Session> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        log.info("有新的客户端上线: {}", session.getId());
        clients.put(session.getId(), session);
    }

    @OnClose
    public void onClose(Session session) {
        String sessionId = session.getId();
        log.info("有客户端离线: {}", sessionId);
        clients.remove(sessionId);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        String sessionId = session.getId();
        if (clients.get(sessionId) != null) {
            log.info("发生了错误,移除客户端: {}", sessionId);
            clients.remove(sessionId);
        }
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message,Session session) {
        log.info("收到客户端发来的消息: {}", message);
        try {
          Message msg = JSONObject.parseObject(message,Message.class);
            this.sendTo(msg);
        } catch (JSONException ex) {
            this.sendString(message,session);
        }
    }

    /**
     * 发送消息
     *
     * @param message 消息对象
     */
    private void sendTo(Message message) {
        Session s = clients.get(message.getUserId());
        if (s != null) {
            try {
                s.getBasicRemote().sendText(message.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 非json字符串
     * 直接返回传过来的消息
     *
     * @param message 消息对象
     */
    private void sendString(String message,Session session){
        if (session != null && !StringUtils.isEmpty(message)){
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
