package com.example.computer_networks_1_project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


// we can also set the methods as synchronized instead of a monitor object.
// or we can use Collections.SynchronizedList(List<> list); this is a wrapper to a List similar to this "Shared Buffer" class.
public class SharedBuffer {
    private List<Peer> activePeers = new ArrayList<>();

    final private Object monitor = new Object();

    public void addPeers(List<Peer> users) {
        boolean flag = true;
        synchronized (monitor) {
            List<Peer> activePeersToRemove = new ArrayList<>();
            List<Peer> usersToRemove = new ArrayList<>();

            for(Peer i: activePeers) {
                for(Peer j: users) {
                    if (i.equals(j)) {
                        i.update(j.getName(), j.getIP(), j.getPort());
                        flag = false;

                        usersToRemove.add(j);
                        break;
                    }
                }
                if (flag) {
//                    activePeers.remove(i);
                    activePeersToRemove.add(i);
                }
            }
            activePeers.removeAll(activePeersToRemove);
            users.removeAll(usersToRemove);
            activePeers.addAll(users);
        }
    }

    public void printActivePeers(){
        synchronized (monitor){
            int i = 1;
            activePeers.forEach(peer -> System.out.println(i + ":" + peer.getName() + " " + + peer.getPort() + " " + peer.getIP()));
        }
    }

    public List<String> getListOfActivePeers(){
        synchronized (monitor) {
            return Arrays.asList(activePeers.stream()
                    .map(peer -> peer.getIP() + ":" + peer.getPort())
                    .toArray(String[]::new)); // typecast object[] to string[]
        } // or return an unmodifiable list of activePeers to ensure thread safety
    }
    /*
    * @param direction if false, the message is stored in the send buffer, the message is sent to that peer
    *                  otherwise, the message is stored in the received buffer.
    * */
    public void setPeerMessage(String ip, int port, String message, boolean direction) {
        synchronized (monitor) {
            Peer requestedPeer = activePeers.stream()
                    .filter(peer -> (Objects.equals(peer.getPort(), port) && Objects.equals(peer.getIP(),ip)))
                    .findAny()
                    .orElse(null);
            if (direction == false) { // message is sent to this peer
                requestedPeer.getMessagesSent().add(message);
            } else {
                requestedPeer.getMessagesReceived().add(message);
            }
        }
    }

    public void deletePeerMessage(String ip, int port, int messageIndex, boolean direction) {
        synchronized (monitor){
            Peer requestedPeer = activePeers.stream()
                    .filter(peer -> (Objects.equals(peer.getPort(), port) && Objects.equals(peer.getIP(),ip)))
                    .findAny()
                    .orElse(null);

            if(requestedPeer != null) requestedPeer.deleteMessage(messageIndex, direction);
        }
    }
    public Peer findPeer(Peer peer) {
        synchronized (monitor){
            Peer result = activePeers.stream()
                    .filter(p -> p.equals(peer))
                    .findAny()
                    .orElse(null);

            return result;
        }
    }

    public void setPeerMessage(Peer peer, String message, boolean direction) {
        synchronized (monitor){
            if(!direction) {
                peer.getMessagesSent().add(message);
            } else {
                peer.getMessagesReceived().add(message);
            }
        }
    }

    public Peer getPeerByIndex(int index) {
        synchronized (monitor) {
            try {
                return activePeers.get(index);
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
        }
    }
}
