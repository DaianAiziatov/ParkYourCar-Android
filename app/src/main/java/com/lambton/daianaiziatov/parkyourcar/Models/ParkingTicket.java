package com.lambton.daianaiziatov.parkyourcar.Models;

public class ParkingTicket {

    private String userEmail;
    private String carPlate;
    private String carManufacturer;
    private String carModel;
    private String carColor;
    private Timing timing;
    private String date;
    private String slotNumber;
    private String spotNumber;
    private PaymentMethod paymentMethod;
    private double paymentAmount;

    enum Timing {
        halfAHour,
        oneHour,
        twoHour,
        threeHour,
        dayEnds;
    }

}
