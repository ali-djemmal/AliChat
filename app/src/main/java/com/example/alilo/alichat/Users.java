package com.example.alilo.alichat;

/**
 * Created by alilo on 27/10/2018.
 */

public class Users {
    public String name ;
    public String image ;
    public String statut ;
    public String thumb_image ;
    public Users() {
    }

    public String getName() {
        return name;
    }



    public Users(String name, String image, String statut, String thumb_image) {
        this.name = name;
        this.image = image;
        this.statut = statut;
        this.thumb_image = thumb_image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
