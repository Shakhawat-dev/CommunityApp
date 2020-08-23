package com.metacoders.communityapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class SinglePostDetails {
     @SerializedName("getNewsDetails")
     @Expose
     private List<Post_Model> getNewsDetails = null;
     @SerializedName("getNewsAudioDetails")
     @Expose
     private List<GetNewsAudioDetail> getNewsAudioDetails = null;

     public List<Post_Model> getGetNewsDetails() {
         return getNewsDetails;
     }

     public void setGetNewsDetails(List<Post_Model> getNewsDetails) {
         this.getNewsDetails = getNewsDetails;
     }

     public List<GetNewsAudioDetail> getGetNewsAudioDetails() {
         return getNewsAudioDetails;
     }

     public void setGetNewsAudioDetails(List<GetNewsAudioDetail> getNewsAudioDetails) {
         this.getNewsAudioDetails = getNewsAudioDetails;
     }
     public  class GetNewsAudioDetail{
         @SerializedName("audio_path")
         @Expose
         private String audioPath;
         @SerializedName("audio_name")
         @Expose
         private String audioName;

         public String getAudioPath() {
             return audioPath;
         }

         public void setAudioPath(String audioPath) {
             this.audioPath = audioPath;
         }

         public String getAudioName() {
             return audioName;
         }

         public void setAudioName(String audioName) {
             this.audioName = audioName;
         }
    }
}
