package com.BDD;

public class Students {

    String ID;
    String Name;
    String Image;
    String Team;

    public Students(String ID, String Name, String Image, String Team){
        this.ID = ID;
        this.Name = Name;
        this.Image = Image;
        this.Team = Team;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTeam() {
        return Team;
    }

    public void setTeam(String team) {
        Team = team;
    }
}
