package com.example.app;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Acceleration {
    @Id(autoincrement = true)
    private Long id;

    private double x;
    private double y;
    private double z;

    private Long time;

    @Generated(hash = 1748291965)
    public Acceleration(Long id, double x, double y, double z, Long time) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.time = time;
    }

    @Generated(hash = 1084766539)
    public Acceleration() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
