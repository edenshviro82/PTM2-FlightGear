package controller;

import java.net.Socket;

public class AgentSockets {
    Socket stream;
    Socket manage;

    public AgentSockets(Socket stream, Socket manage) {
        this.stream = stream;
        this.manage = manage;
    }

    public Socket getStream() {
        return stream;
    }

    public void setStream(Socket stream) {
        this.stream = stream;
    }

    public Socket getManage() {
        return manage;
    }

    public void setManage(Socket manage) {
        this.manage = manage;
    }

    public AgentSockets() { }
}
