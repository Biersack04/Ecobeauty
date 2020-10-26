package com.example.ecobeauty.mycosmetics;

class UserCosmetics {
    public String name;
    public long date;


   public UserCosmetics(String name, long date) {

       this.name = name;
       this.date = date;
   }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return this.date;
    }

   /* public void setDate(long date) {
        this.date = date;
    }*/

}

