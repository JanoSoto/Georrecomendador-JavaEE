/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entities.Correlation;
import entities.User;
import entities.Venue;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import managedbeans.util.ItemPrediction;
import managedbeans.util.SortPredictionByRating;
import sessionbeans.CorrelationFacadeLocal;
import sessionbeans.RatingFacadeLocal;
import sessionbeans.UserFacadeLocal;
import sessionbeans.VenueFacadeLocal;

/**
 *
 * @author JAno
 */
@Named("predictionController")
@SessionScoped
public class PredictionController implements Serializable {
    
    @EJB
    private VenueFacadeLocal ejbVenue;
    @EJB
    private RatingFacadeLocal ejbRating;
    @EJB
    private UserFacadeLocal ejbUser;
    @EJB
    private CorrelationFacadeLocal ejbCorrelation;
    
    private List<ItemPrediction> topKPredictions = null;
    private List<ItemPrediction> allPredictions;

    private int r_inner = 100;
    private int r_outer = 1000;
    
    public PredictionController(){
        
    }

    public int getR_inner() {
        return r_inner;
    }

    public void setR_inner(int r_inner) {
        this.r_inner = r_inner;
    }

    public int getR_outer() {
        return r_outer;
    }

    public void setR_outer(int r_outer) {
        this.r_outer = r_outer;
    }
        
    public List<ItemPrediction> getTopKPredictions() {
        return topKPredictions;
    }

    public void setTopKPredictions(List<ItemPrediction> topKPredictions) {
        this.topKPredictions = topKPredictions;
    }
    
    
    
    public void getTopKItems(int k, User user){
        topKPredictions = new ArrayList();
        long timeStart = System.currentTimeMillis();
        
        double avgRatingUser = ejbRating.getAverageRatingByUser(user.getId());
        List<Correlation> vecindario = ejbCorrelation.getNeighborhood(user.getId(), 20);
        double avgCorrelation = getCorrelationAverage(vecindario);
        Map<Long, Double> avgVecindario = new HashMap<>();
        for(Correlation vecino : vecindario){
            if(avgVecindario.get(vecino.getUser1().getId()) == null){
                avgVecindario.put(vecino.getUser1().getId(), ejbRating.getAverageRatingByUser(vecino.getUser1().getId()));
            }
            if(avgVecindario.get(vecino.getUser2().getId()) == null){
                avgVecindario.put(vecino.getUser2().getId(), ejbRating.getAverageRatingByUser(vecino.getUser2().getId()));
            }
        }
        allPredictions = new ArrayList();
        
        List<Venue> allVenues = ejbVenue.getFirstNVenues(1000);
        System.out.println("CALCULANDO LAS PREDICCIONES...");
        int ratingUserVenue;
        for(Venue venue : allVenues){
            System.out.println("Venue "+venue.getId());
            double sumaVecindario = 0;
            for(Correlation vecino : vecindario){
                double usersCorrelation;
                if(!Objects.equals(user.getId(), vecino.getUser1().getId())){
                    if((usersCorrelation = ejbCorrelation.getCorrelationByUsers(user.getId(), vecino.getUser1().getId())) == -1){
                        usersCorrelation = ejbCorrelation.getCorrelationByUsers(vecino.getUser1().getId(), user.getId());
                    }
                    ratingUserVenue = ejbRating.getRatingByUserAndVenue(vecino.getUser1().getId(), venue.getId());
                    if(ratingUserVenue != -1){
                        sumaVecindario += (ratingUserVenue - avgVecindario.get(vecino.getUser1().getId())) * usersCorrelation;   
                    }
                }
                else {
                    if((usersCorrelation = ejbCorrelation.getCorrelationByUsers(user.getId(), vecino.getUser2().getId())) == -1){
                        usersCorrelation = ejbCorrelation.getCorrelationByUsers(vecino.getUser2().getId(), user.getId());
                    }
                    ratingUserVenue = ejbRating.getRatingByUserAndVenue(vecino.getUser2().getId(), venue.getId());
                    if(ratingUserVenue != -1){
                        sumaVecindario += (ratingUserVenue - avgVecindario.get(vecino.getUser2().getId())) * usersCorrelation;   
                    }
                }
            }
            allPredictions.add(new ItemPrediction(venue, avgRatingUser + (sumaVecindario / avgCorrelation)));
        }
        System.out.println("OBTENIENDO LAS TOP K PREDICCIONES");
        //allPredictions.sort(new SortPredictionByRating());
        Collections.sort(allPredictions, new SortPredictionByRating());
        
        System.out.println("TIEMPO TOTAL: " + (System.currentTimeMillis() - timeStart) + "ms");
        
        for(int i=0; i<k; i++){
            topKPredictions.add(allPredictions.get(i));
        }
        System.out.println("PROCESO TERMINADO");
        
    }
    
    public double getCorrelationAverage(List<Correlation> vecindario){
        double suma = 0;
        
        for(Correlation corr : vecindario){
            suma += corr.getCorrelation_value();
        }
        
        return suma/vecindario.size();
    }
    
    /*
    public void getTopK(int k, User user){
        topKPredictions = getTopKItems(k, user);
    }
    */
    
    /**
     * Implementación de lo propuesto en Mobile 3D-Gis
     * @param user
     * @param k
     */
    public void reRankFromDistance(User user, int k){
        System.out.println("Re-rankeando");
        topKPredictions = new ArrayList();
        for(ItemPrediction item : allPredictions){
            topKPredictions.add(new ItemPrediction(item.getVenue(), item.getRating() * distanceDecay(user, item.getVenue())));
            
        }
        topKPredictions.sort(new SortPredictionByRating());
        /*
        for(int i=0; i<k; i++){
            topKPredictions.add(allPredictions.get(i));
        }
        */
    }
    
    private double distanceDecay(User user, Venue venue){
        double R_INNER = r_inner/1000;
        double R_OUTER = r_outer/1000;
        double lambda = -1*Math.log(0.1)/(R_OUTER - R_INNER);
        double distance = calculateDistance(user.getLatitude(), user.getLongitude(), 
                                            venue.getLatitude(), venue.getLongitude());
        if(distance < R_INNER){
            return 1;
        }
        else if (R_INNER <= distance && distance < R_OUTER){
            return Math.exp(-1*lambda*(distance-R_INNER));
        }
        else{
            return 0;
        }
    }
    
    /**
     * Cálculo de la distancia de Haversine
     * @param user_latitude
     * @param user_longitude
     * @param venue_latitude
     * @param venue_longitude
     * @return 
     */
    public double calculateDistance(double user_latitude, double user_longitude, double venue_latitude, double venue_longitude) {
        double R = 6372.8; // In kilometers
        double dLat = Math.toRadians(venue_latitude - user_latitude);
        double dLon = Math.toRadians(venue_longitude - user_longitude);
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(Math.toRadians(user_latitude)) * Math.cos(Math.toRadians(venue_latitude));
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
