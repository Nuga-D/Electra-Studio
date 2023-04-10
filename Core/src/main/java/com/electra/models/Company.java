package com.electra.models;

import javax.persistence.*;

@Entity
@Table(name = "company")
public class Company {

    public Company() {

    }

    public Company(String name, String address, String registrationNumber, String taxID, String representativePhoneNo) {
        this.name = name;
        this.address = address;
        this.registrationNumber = registrationNumber;
        this.taxID = taxID;
        this.representativePhoneNo = representativePhoneNo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String registrationNumber;

    private String taxID;

    private String representativePhoneNo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public String getRepresentativePhoneNo() {
        return representativePhoneNo;
    }

    public void setRepresentativePhoneNo(String representativePhoneNo) {
        this.representativePhoneNo = representativePhoneNo;
    }
}
