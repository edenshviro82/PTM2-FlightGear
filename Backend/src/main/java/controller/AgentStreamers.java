package controller;

import java.io.*;

public class AgentStreamers {
    PrintWriter operationOut;
    BufferedReader operationIn;
    ObjectInputStream objectInputStream;
    BufferedReader streamIn;

    public AgentStreamers(BufferedReader in) {
        this.streamIn = in;
    }

    public AgentStreamers(BufferedReader buff, PrintWriter out, ObjectInputStream obj) {
        this.operationIn = buff;
        this.operationOut = out;
        this.objectInputStream = obj;
    }

    public PrintWriter getOperationOut() {return operationOut;}
    public BufferedReader getOperationIn() {return operationIn;}
    public ObjectInputStream getObjectInputStream() {return objectInputStream;}
    public BufferedReader getStreamIn() {return streamIn;}
    public AgentStreamers setStreamIn(BufferedReader streamIn) {
        this.streamIn = streamIn;
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
