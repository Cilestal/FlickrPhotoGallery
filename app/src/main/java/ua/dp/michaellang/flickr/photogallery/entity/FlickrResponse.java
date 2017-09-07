package ua.dp.michaellang.flickr.photogallery.entity;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlickrResponse {

    @SerializedName("photos")
    @Expose
    private Photos photos;
    @SerializedName("stat")
    @Expose
    private String stat;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

}
