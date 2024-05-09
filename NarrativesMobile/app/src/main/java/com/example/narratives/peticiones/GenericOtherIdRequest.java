package com.example.narratives.peticiones;

public class GenericOtherIdRequest {
    int other_id;

    public GenericOtherIdRequest(int id){
        this.other_id = id;
    }
    public int getOther_id() {
        return other_id;
    }

    public void setOther_id(int other_id) {
        this.other_id = other_id;
    }
}
