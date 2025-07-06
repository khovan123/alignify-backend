package com.api.model;

public class Contact {

    private String contact_type;
    private String contact_infor;

    public Contact() {
    }

    public Contact(String contact_type, String contact_infor) {
        this.contact_type = contact_type;
        this.contact_infor = contact_infor;
    }

    public String getContact_type() {
        return contact_type;
    }

    public void setContact_type(String contact_type) {
        this.contact_type = contact_type;
    }

    public String getContact_infor() {
        return contact_infor;
    }

    public void setContact_infor(String contact_infor) {
        this.contact_infor = contact_infor;
    }

}
