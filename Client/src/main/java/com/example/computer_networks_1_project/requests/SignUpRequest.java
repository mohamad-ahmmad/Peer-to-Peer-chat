package com.example.computer_networks_1_project.requests;

public class SignUpRequest extends ServerRequest implements Request{
    public SignUpRequest(SignUpRequestBuilder signUpRequestBuilder) {
        this.port = signUpRequestBuilder.port;
        this.username = signUpRequestBuilder.username;
        this.password = signUpRequestBuilder.password;
    }

    @Override
    public String toString(){
        return "sign-up," + super.toString();
    }
    public static class SignUpRequestBuilder extends ServerRequest.ServerRequestBuilder< SignUpRequest> {

        public SignUpRequestBuilder(){

        }
        @Override
        public SignUpRequest build() {
            return new SignUpRequest(this);
        }
    }
}
