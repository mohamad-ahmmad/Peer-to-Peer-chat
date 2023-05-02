package com.example.computer_networks_1_project.requests;

public class RetrieveListRequest extends ServerRequest{
    @Override
    public String send() {
        return null;
    }

    private RetrieveListRequest(RetrieveListRequestBuilder x) {
        this.port = x.port;
        this.username = x.username;
        this.password = x.password;
    }

    public static class RetrieveListRequestBuilder extends ServerRequestBuilder<RetrieveListRequest> {

        public RetrieveListRequestBuilder(){

        }
        @Override
        public RetrieveListRequest build() {
            return new RetrieveListRequest(this);
        }
    }
}
