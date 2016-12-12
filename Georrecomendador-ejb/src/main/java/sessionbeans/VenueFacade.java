/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Correlation;
import entities.Venue;
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
public class VenueFacade extends AbstractFacade<Venue> implements VenueFacadeLocal {

    @PersistenceContext(unitName = "cl.usach_Georrecomendador-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VenueFacade() {
        super(Venue.class);
    }

    @Override
    public List<Venue> getFirstNVenues(int n) {
        Query query = em.createNamedQuery("Venue.findNVenues");
        query.setMaxResults(n);
        try{
            return (List<Venue>) query.getResultList();
        }
        catch(NoResultException e){
            return new ArrayList();
        }
    }
    
}
