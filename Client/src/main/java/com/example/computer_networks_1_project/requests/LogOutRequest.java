package com.example.computer_networks_1_project.requests;

public class LogOutRequest extends ServerRequest{
    @Override
    public String send() {
        return null;
    }

    private LogOutRequest(LogOutRequestBuilder x) {
        this.port = x.port;
        this.username = x.username;
        this.password = x.password;
    }

    public static class LogOutRequestBuilder extends ServerRequestBuilder<LogOutRequest>{

        public LogOutRequestBuilder(){

        }
        @Override
        public LogOutRequest build(){
            return new LogOutRequest(this);
        }


    }
}
