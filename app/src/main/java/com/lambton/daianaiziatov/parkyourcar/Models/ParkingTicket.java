package com.lambton.daianaiziatov.parkyourcar.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class ParkingTicket implements Parcelable {

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

    protected ParkingTicket(Parcel in) {
        userEmail = in.readString();
        carPlate = in.readString();
        carManufacturer = in.readString();
        carModel = in.readString();
        carColor = in.readString();
        date = in.readString();
        slotNumber = in.readString();
        spotNumber = in.readString();
        paymentAmount = in.readDouble();
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
        dest.writeString(carPlate);
        dest.writeString(carManufacturer);
        dest.writeString(carModel);
        dest.writeString(carColor);
        dest.writeString(date);
        dest.writeString(slotNumber);
        dest.writeString(spotNumber);
        dest.writeDouble(paymentAmount);
    }

    enum Timing {
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

    enum PaymentMethod {
        visaDebit {
            @Override
            public String toString() {
                return "Visa Debit";
            }
        },
        visaCredit {
            @Override
            public String toString() {
                return "Visa Credit";
            }
        },
        masterCard {
            @Override
            public String toString() {
                return "Mastercard";
            }
        },
        paypal {
            @Override
            public String toString() {
                return "Visa Debit";
            }
        },
        aliPay {
            @Override
            public String toString() {
                return "AliPay";
            }
        },
        wechatPay {
            @Override
            public String toString() {
                return "WeChatPay";
            }
        };
    }

    public ParkingTicket() {
    }

    public ParkingTicket(String userEmail, String carPlate, String carManufacturer, String carModel, String carColor, Timing timing, String date, String slotNumber, String spotNumber, PaymentMethod paymentMethod, double paymentAmount) {
        this.userEmail = userEmail;
        this.carPlate = carPlate;
        this.carManufacturer = carManufacturer;
        this.carModel = carModel;
        this.carColor = carColor;
        this.timing = timing;
        this.date = date;
        this.slotNumber = slotNumber;
        this.spotNumber = spotNumber;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getCarManufacturer() {
        return carManufacturer;
    }

    public void setCarManufacturer(String carManufacturer) {
        this.carManufacturer = carManufacturer;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public Timing getTiming() {
        return timing;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
