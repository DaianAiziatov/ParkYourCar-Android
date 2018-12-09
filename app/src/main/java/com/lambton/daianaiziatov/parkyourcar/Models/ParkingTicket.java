package com.lambton.daianaiziatov.parkyourcar.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class ParkingTicket implements Parcelable {

    private String userEmail;
    private String plate;
    private String manufacturer;
    private String model;
    private String color;
    private Timing timing;
    private String date;
    private String slotNumber;
    private String spotNumber;
    private PaymentMethod payment;
    private double total;

    protected ParkingTicket(Parcel in) {
        userEmail = in.readString();
        plate = in.readString();
        manufacturer = in.readString();
        model = in.readString();
        color = in.readString();
        date = in.readString();
        slotNumber = in.readString();
        spotNumber = in.readString();
        total = in.readDouble();
    }

    public static final Creator<ParkingTicket> CREATOR = new Creator<ParkingTicket>() {
        @Override
        public ParkingTicket createFromParcel(Parcel in) {
            return new ParkingTicket(in);
        }

        @Override
        public ParkingTicket[] newArray(int size) {
            return new ParkingTicket[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userEmail);
        dest.writeString(plate);
        dest.writeString(manufacturer);
        dest.writeString(model);
        dest.writeString(color);
        dest.writeString(date);
        dest.writeString(slotNumber);
        dest.writeString(spotNumber);
        dest.writeDouble(total);
    }

    public enum Timing {
        HALF_A_HOUR {
            @Override
            public String toString() {
                return "30 mins";
            }
        },
        ONE_HOUR {
            @Override
            public String toString() {
                return "1 hour";
            }
        },
        TWO_HOURS {
            @Override
            public String toString() {
                return "2 hours";
            }
        },
        THREE_HOURS {
            @Override
            public String toString() {
                return "3 hours";
            }
        },
        DAY_ENDS {
            @Override
            public String toString() {
                return "Day Ends";
            }
        };
    }

    public enum PaymentMethod {
        VISA_DEBIT {
            @Override
            public String toString() {
                return "Visa Debit";
            }
        },
        VISA_CREDIT {
            @Override
            public String toString() {
                return "Visa Credit";
            }
        },
        MASTERCARD {
            @Override
            public String toString() {
                return "Mastercard";
            }
        },
        PAYPAL {
            @Override
            public String toString() {
                return "PayPal";
            }
        },
        ALI_PAY {
            @Override
            public String toString() {
                return "Ali Pay";
            }
        },
        WECHAT_PAY {
            @Override
            public String toString() {
                return "WeChat Pay";
            }
        };
    }

    public ParkingTicket() {
    }

    public ParkingTicket(String userEmail, String plate, String manufacturer, String model, String color, Timing timing, String date, String slotNumber, String spotNumber, PaymentMethod payment, double total) {
        this.userEmail = userEmail;
        this.plate = plate;
        this.manufacturer = manufacturer;
        this.model = model;
        this.color = color;
        this.timing = timing;
        this.date = date;
        this.slotNumber = slotNumber;
        this.spotNumber = spotNumber;
        this.payment = payment;
        this.total = total;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTiming() {
        switch (timing) {
            case HALF_A_HOUR: return "30 mins";
            case ONE_HOUR: return "1 hour";
            case TWO_HOURS: return "2 hours";
            case THREE_HOURS: return "3 hours";
            default: return "Day Ends";
        }
    }

    public void setTiming(Timing timing) {
        this.timing = timing;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }

    public String getPayment() {
        switch (payment) {
            case VISA_DEBIT: return "Visa Debit";
            case VISA_CREDIT: return "Visa Credit";
            case MASTERCARD: return "Mastercard";
            case PAYPAL: return "PayPal";
            case ALI_PAY: return "Ali Pay";
            default: return "WeChat Pay";
        }
    }

    public void setPayment(PaymentMethod payment) {
        this.payment = payment;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

}
