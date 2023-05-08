package com.example.computer_networks_1_project.requests;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public abstract class PeerRequest implements Request{
    protected String header;
    protected int index;
    protected String content;
    protected InetSocketAddress destinationAddress;

    public String getHeader() {
        return header;
    }
    public int getIndex() {
        return index;
    }
    public String getContent() {
        return content;
    }
    public InetSocketAddress getDestinationAddress() {
        return destinationAddress;
    }

    public static abstract class PeerRequestBuilder<T> implements Builder<T>{
        protected String header;
        protected int index;
        protected String content;
        protected InetSocketAddress destinationAddress;

        public PeerRequestBuilder<T> setHeader(String header){
            this.header = header;
            return this;
        }

        public PeerRequestBuilder<T> setDestinationAddress(InetSocketAddress address){
            this.destinationAddress = new InetSocketAddress(address.getAddress(), address.getPort());
            return this;
        }

        public PeerRequestBuilder<T> setindex(int index){
            this.index = index;
            return this;
        }

        public PeerRequestBuilder<T> setContent(String content){
            this.content = content;
            return this;
        }
    }
}
