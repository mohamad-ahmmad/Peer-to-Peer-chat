package com.example.computer_networks_1_project.requests;

public class DeleteMessageRequest extends PeerRequest{

    private DeleteMessageRequest(DeleteMessageRequest.DeleteMessageRequestBuilder x){
        this.header = x.header;
        this.content = x.content;
        this.index = x.index;
    }

    @Override
    public String toString(){
        return header + "," + index + "," + content;
    }
    public static class DeleteMessageRequestBuilder extends PeerRequestBuilder<DeleteMessageRequest> {

        @Override
        public DeleteMessageRequest build() {
            return new DeleteMessageRequest(this);
        }
    }

}
