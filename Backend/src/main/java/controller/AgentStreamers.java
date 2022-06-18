package controller;

import java.io.*;
import java.net.Socket;

public class AgentStreamers {
    PrintWriter operationOut;
    BufferedReader operationIn;
    ObjectInputStream objectInputStream;

    BufferedReader streamOut;

    public AgentStreamers(BufferedReader in) {
        this.streamOut = new BufferedReader(in);
    }

    public AgentStreamers(BufferedReader buff, PrintWriter out, ObjectInputStream obj) {
        this.operationIn = buff;
        this.operationOut = out;
        this.objectInputStream = obj;
    }

    public PrintWriter getOperationOut() {return operationOut;}
    public BufferedReader getOperationIn() {return operationIn;}
    public ObjectInputStream getObjectInputStream() {return objectInputStream;}
    public BufferedReader getStreamOut() {return streamOut;}
    public AgentStreamers setStreamOut(BufferedReader streamOut) {
        this.streamOut = streamOut;
        return this;
    }
    public AgentStreamers setOperationOut(PrintWriter operationOut) {
        this.operationOut = operationOut;
        return this;
    }
    public AgentStreamers setObjectInputStream(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
        return this;
    }
    public AgentStreamers setOperationIn(BufferedReader operationIn) {
        this.operationIn = operationIn;
        return this;
    }
}
