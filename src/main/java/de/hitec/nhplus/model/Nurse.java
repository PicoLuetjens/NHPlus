package de.hitec.nhplus.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Nurse extends Person{
    private SimpleLongProperty nid;
    private final SimpleStringProperty phoneNumber;
    private final SimpleStringProperty locked;

    /**
     * Constructor to initiate an object of class <code>Nurse</code> with the given parameter. Use this constructor
     * to initiate objects, which are not persisted yet, because it will not have a nurse id (nid).
     *
     * @param firstName First name of the nurse.
     * @param surname Last name of the nurse.
     * @param phoneNumber Phone number of the nurse.
     * @param locked lock status of the nurse.
     */

    public Nurse(String firstName, String surname, String phoneNumber, String locked) {
        super(firstName, surname);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.locked = new SimpleStringProperty(locked);
    }

    /**
     * Constructor to initiate an object of class <code>Nurse</code> with the given parameter. Use this constructor
     * to initiate objects, which are already persisted and have a nurse id (nid).
     *
     * @param nid Nurse id.
     * @param firstName First name of the nurse.
     * @param surname Last name of the nurse.
     * @param phoneNumber Phone number of the nurse.
     * @param locked lock status of the nurse.
     */

    public Nurse(long nid, String firstName, String surname, String phoneNumber, String locked) {
        super(firstName, surname);
        this.nid = new SimpleLongProperty(nid);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.locked = new SimpleStringProperty(locked);
    }

    public long getNid() {
        return nid.get();
    }

    public SimpleLongProperty nidProperty() {
        return nid;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String isLocked() {return locked.get();}

    public SimpleStringProperty lockedProperty() {return locked;}

    public void setLocked(String locked) {this.locked.set(locked);}
}
