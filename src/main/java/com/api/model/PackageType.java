package com.api.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "packageTypes")
public class PackageType {

    @Id
    private String packageId;
    private String packageName;
    private String discription;
    private double price;
    private List<FeaturePlan> feature;
    private String subcribeType;
    private String roleId;
    private int subcribeCount;
    private double discount;

    public PackageType() {
        this.subcribeCount = 0;
        this.discount  = 0;
    }
    
    
    public PackageType(String packageId, String packageName, String discription, double price, List<FeaturePlan> feature, String subcribeType, String roleId, int subcribeCount, double discount) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.discription = discription;
        this.price = price;
        this.feature = feature;
        this.subcribeType = subcribeType;
        this.roleId = roleId;
        this.subcribeCount = subcribeCount;
        this.discount = discount;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<FeaturePlan> getFeature() {
        return feature;
    }

    public void setFeature(List<FeaturePlan> feature) {
        this.feature = feature;
    }

    public String getSubcribeType() {
        return subcribeType;
    }

    public void setSubcribeType(String subcribeType) {
        this.subcribeType = subcribeType;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public int getSubcribeCount() {
        return subcribeCount;
    }

    public void setSubcribeCount(int subcribeCount) {
        this.subcribeCount = subcribeCount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
}
