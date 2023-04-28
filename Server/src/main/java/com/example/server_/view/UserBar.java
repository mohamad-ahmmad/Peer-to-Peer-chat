package com.example.server_.view;

import com.example.server_.ServerApplication;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class UserBar extends FlowPane {

    //INDICES : 0=>disconnect, 1=>connected;
    public static Image[] userStatus = {
            new Image(String.valueOf(ServerApplication.class.getResource("app/imgs/red.png"))),
            new Image(String.valueOf(ServerApplication.class.getResource("app/imgs/green.png")))
    };

    private ImageView stat = new ImageView(userStatus[0]);;
    private String name;

    private Text ip= new Text("");
    private Text port= new Text("");

    public UserBar(String name){
        super();
        this.name = name;

        initUI();
    }

    public UserBar(String name, String ip, String port){
        super();
        this.name = name;
        this.port.setText(port);
        this.ip.setText(ip);
        this.stat.setImage(userStatus[1]);
        initUI();
    }



    public String getName() {
        return name;
    }

    private void initUI() {

        this.setHgap(10.5);
        stat.setFitHeight(10);
        stat.setFitWidth(10);
        this.getChildren().add(stat);
        this.getChildren().add(new Text(name));
        this.getChildren().add(this.ip);
        this.getChildren().add(this.port);



    }

    public void setConnected( boolean status){
        if(status)
            stat.setImage(userStatus[1]);
        else
            stat.setImage(userStatus[0]);
    }

    public void setPort(String port){
        this.port.setText(port);
    }

    public void setIP(String ip){
        this.ip.setText(ip);
    }

}
