package com.prox.passgpt.model;


import lombok.Getter;

@Getter
public class Proxy {
    public Proxy(String formIp) {
        String[] splitForm = formIp.split(":");
        if(splitForm.length == 4){
            this.username = splitForm[0];
            this.password = splitForm[1];
            this.host = splitForm[2];
            this.port = Integer.parseInt(splitForm[3]);
        }
    }
    public Proxy(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    private String host;
    private int port;
    private String username;
    private String password;
}
