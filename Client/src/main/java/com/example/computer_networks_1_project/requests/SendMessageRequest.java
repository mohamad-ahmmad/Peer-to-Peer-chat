package com.example.computer_networks_1_project.requests;

public class SendMessageRequest extends PeerRequest{

    private SendMessageRequest(SendMessageRequestBuilder x){
        this.header = x.header;
        this.content = x.content;
        this.index = x.index;
    }

    @Override
    public String send() {
        return null;
    }

    public static class SendMessageRequestBuilder extends PeerRequestBuilder<SendMessageRequest> {

        @Override
        public SendMessageRequest build() {
            return new SendMessageRequest(this);
        }
    }

}
