package com.delaroystodios.metakar.Model;

public class DetailsAdvertismentModel
{
    private String result;
    private AdvertisementDetails advertisement;
    protected boolean seen;

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public AdvertisementDetails getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(AdvertisementDetails advertisement) {
        this.advertisement = advertisement;
    }
}
