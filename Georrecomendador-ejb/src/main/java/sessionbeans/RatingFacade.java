/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Rating;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JAno
 */
@Stateless
public class RatingFacade extends AbstractFacade<Rating> implements RatingFacadeLocal {

    @PersistenceContext(unitName = "cl.usach_Georrecomendador-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RatingFacade() {
        super(Rating.class);
    }
    
    @Override
    public List<Rating> findByUser(Long user_id) {
        Query query = em.createNamedQuery("Rating.findByUser").setParameter("user_id", user_id);
        try{
            return (List<Rating>) query.getResultList();
        }
        catch(NoResultException e){
            return new ArrayList<>();
        }
    }

    @Override
    public double getAverageRatingByUser(Long user_id) {
        Query query = em.createNamedQuery("Rating.getAverageRatingByUser").setParameter("user_id", user_id);
        try{
            /*
            String resultado = (String)query.getSingleResult();
            if(resultado != null){
                return Double.parseDouble((String) query.getSingleResult());
            }            
            else{
                return 0;
            }
            */
            if(query.getSingleResult() != null){
                return (double) query.getSingleResult();
            }
            else{
                return -1;
            }
        }
        catch(NoResultException e){
            return -1;
        }
    }

    @Override
    public int getRatingByUserAndVenue(Long user_id, Long venue_id) {
        Query query = em.createNamedQuery("Rating.getRatingByUserAndVenue");
        query.setParameter("user_id", user_id);
        query.setParameter("venue_id", venue_id);
        try{
            List result = (List) query.getResultList();
            if(result.size() > 0){
                return (int) result.get(0);
            }
            else {
                return -1;
            }
        }
        catch(NoResultException e){
            return -1;
        }
    }
    
}
