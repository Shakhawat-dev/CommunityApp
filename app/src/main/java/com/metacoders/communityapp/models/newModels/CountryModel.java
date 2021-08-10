package com.metacoders.communityapp.models.newModels;

import android.text.Html;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.metacoders.communityapp.utils.AppPreferences;

import java.io.Serializable;

public class CountryModel implements Serializable {
    @Expose
    @SerializedName("wikiDataId")
    private String wikiDataId;
    @Expose
    @SerializedName("flag")
    private int flag;
    @Expose
    @SerializedName("emojiU")
    private String emojiU;
    @Expose
    @SerializedName("emoji")
    private String emoji;
    @Expose
    @SerializedName("native")
    private String native_name;
    @Expose
    @SerializedName("currency")
    private String currency;
    @Expose
    @SerializedName("capital")
    private String capital;
    @Expose
    @SerializedName("phonecode")
    private String phonecode;
    @Expose
    @SerializedName("iso2")
    private String iso2;
    @Expose
    @SerializedName("iso3")
    private String iso3;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("id")
    private int id;

    public String getWikiDataId() {
        return wikiDataId;
    }

    public void setWikiDataId(String wikiDataId) {
        this.wikiDataId = wikiDataId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getEmojiU() {
        return emojiU;
    }

    public void setEmojiU(String emojiU) {
        this.emojiU = emojiU;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getNative_name() {
        return native_name;
    }

    public void setNative_name(String native_name) {
        this.native_name = native_name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return   AppPreferences.localeToEmoji(this.iso2) + "  " + this.name + ""  ;
    }
}
