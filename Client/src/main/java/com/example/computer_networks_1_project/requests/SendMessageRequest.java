package com.example.computer_networks_1_project.requests;

import java.net.DatagramPacket;

public class SendMessageRequest extends PeerRequest{

    private SendMessageRequest(SendMessageRequestBuilder x){
        this.header = x.header;
        this.content = x.content;
        this.index = x.index;
        this.destinationAddress = x.destinationAddress;
    }

    @Override
    public String toString(){
        return header + "," + content;
    }

    public static class SendMessageRequestBuilder extends PeerRequestBuilder<SendMessageRequest> {

        @Override
        public SendMessageRequest build() {
            return new SendMessageRequest(this);
        }
    }

}
