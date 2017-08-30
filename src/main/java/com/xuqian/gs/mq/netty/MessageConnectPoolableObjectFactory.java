package com.xuqian.gs.mq.netty;

public class MessageConnectPoolableObjectFactory implements PoolableObjectFactory<MessageConnectFactory> {

    private String serverAddress;
    private int sessionTimeOut = 3 * 1000;

    public MessageConnectPoolableObjectFactory(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public MessageConnectPoolableObjectFactory(String serverAddress, int sessionTimeOut) {
        this.serverAddress = serverAddress;
        this.sessionTimeOut = sessionTimeOut;
    }

    public MessageConnectFactory makeObject() throws Exception {
        MessageConnectFactory factory = new MessageConnectFactory(serverAddress);
        return factory;
    }

    public void destroyObject(MessageConnectFactory obj) throws Exception {
        if (obj instanceof MessageConnectFactory) {
            ((MessageConnectFactory) obj).close();
        }
    }

    public boolean validateObject(MessageConnectFactory obj) {
        return true;
    }

    public void activateObject(MessageConnectFactory obj) throws Exception {

    }

    public void passivateObject(MessageConnectFactory obj) throws Exception {
        MessageConnectFactory factory = (MessageConnectFactory) obj;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }
}
