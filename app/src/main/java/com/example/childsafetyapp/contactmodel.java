package com.example.childsafetyapp;

public class contactmodel {
    String orgname, orgnumber, orgemail;

    public contactmodel(String orgname, String orgnumber, String orgemail) {
        this.orgname = orgname;
        this.orgnumber = orgnumber;
        this.orgemail = orgemail;


    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getOrgnumber() {
        return orgnumber;
    }

    public void setOrgnumber(String orgnumber) {
        this.orgnumber = orgnumber;
    }

    public String getOrgemail() {
        return orgemail;
    }

    public void setOrgemail(String orgemail) {
        this.orgemail = orgemail;
    }

}
