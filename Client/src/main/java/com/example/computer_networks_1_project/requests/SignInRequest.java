package com.example.computer_networks_1_project.requests;

public class SignInRequest extends ServerRequest{
    @Override
    public String send() {
        return null;
    }

    private SignInRequest(SignInRequestBuilder x) {
        this.port = x.port;
        this.username = x.username;
        this.password = x.password;
    }

    public static class SignInRequestBuilder extends ServerRequestBuilder<SignInRequest>{

        public SignInRequestBuilder(){

        }
        @Override
        public SignInRequest build(){
            return new SignInRequest(this);
        }


    }


}
