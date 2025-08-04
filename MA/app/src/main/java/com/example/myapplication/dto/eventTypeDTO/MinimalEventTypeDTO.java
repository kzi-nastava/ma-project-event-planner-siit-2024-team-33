package com.example.myapplication.dto.eventTypeDTO;

import java.io.Serializable;

public class MinimalEventTypeDTO implements Serializable {
    public Integer id;
    public String name;
    public String description;

    @Override
    public String toString() {
        return name;
    }
}
