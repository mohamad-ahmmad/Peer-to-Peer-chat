package com.example.computer_networks_1_project.requests;

public abstract class PeerRequest implements Request{
    protected String header;
    protected int index;
    protected String content;

    public static abstract class PeerRequestBuilder<T> implements Builder<T>{
        protected String header;
        protected int index;
        protected String content;

        public PeerRequestBuilder<T> setHeader(String header){
            this.header = header;
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
