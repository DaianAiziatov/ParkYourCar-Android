package com.lambton.daianaiziatov.parkyourcar.Models;

public class Car {

    private String carId;
    private String manufacturer;
    private String model;
    private String plateNumber;
    private String color;

    public Car(String carId, String manufacturer, String model, String plateNumber, String color) {
        this.carId = carId;
        this.manufacturer = manufacturer;
        this.model = model;
        this.plateNumber = plateNumber;
        this.color = color;
    }

    public Car() {
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carId='" + carId + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
