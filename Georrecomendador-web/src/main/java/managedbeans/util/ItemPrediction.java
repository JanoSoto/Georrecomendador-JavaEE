/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans.util;

import entities.Venue;

/**
 *
 * @author JAno
 */
public class ItemPrediction {
    private Venue venue;
    private double rating;
    
    public ItemPrediction(Venue venue, double rating){
        this.rating = rating;
        this.venue = venue;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
