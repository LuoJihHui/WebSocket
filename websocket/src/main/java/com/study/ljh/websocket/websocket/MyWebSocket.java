package com.study.ljh.websocket.websocket;

import com.study.ljh.websocket.common.CommonConstant;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket具体实现类
 *
 * @author luojihHui
 * @date 2019/7/2
 */
@Component
@ServerEndpoint(value = "/websocket")
public class MyWebSocket {

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * concurrent包的线程安全Set，存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的会话，通过session对象发送数据
     */
    private Session session;

    /**
     * 连接建立成功后
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        // 添加到set中
        webSocketSet.add(this);
        System.out.println("有新连接加入!当前在线人数为" + getOnlineCount());
        // 推送消息
        try {
            sendMessage(String.valueOf(CommonConstant.CURRENT_WANG_ING_NUMBER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭后
     */
    @OnClose
    public void onClose() {
        // 从set中删除
        webSocketSet.remove(this);
        System.out.println("有一连接关闭！当前连接人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后
     *
     * @param message 客户端消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("收到客户端" + session.getId() + "的消息：" + message);
        // 群发消息
        sendInfo(message);
    }

    /**
     * 发生错误时
     *
     * @param error 错误消息
     */
    @OnError
    public void onError(Throwable error) {
        System.err.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 群发消息
     *
     * @param message 消息内容
     */
    private static void sendInfo(String message) {
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 推送消息
     *
     * @param message 消息内容
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 获取当前在线连接数
     *
     * @return onlineCount 在线连接数
     */
    private static synchronized int getOnlineCount() {
        return webSocketSet.size();
    }
}
