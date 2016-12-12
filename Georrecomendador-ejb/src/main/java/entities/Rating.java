/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author JAno
 */
@Entity
@Table(name = "rating")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rating.findAll", query = "SELECT r FROM Rating r"),
    @NamedQuery(name = "Rating.findById", query = "SELECT r FROM Rating r WHERE r.id = :id"),
    @NamedQuery(name = "Rating.findByUserId", query = "SELECT r FROM Rating r WHERE r.user.id = :userId"),
    @NamedQuery(name = "Rating.findByVenueId", query = "SELECT r FROM Rating r WHERE r.venue.id = :venueId"),
    @NamedQuery(name = "Rating.findByRating", query = "SELECT r FROM Rating r WHERE r.rating = :rating"),
    @NamedQuery(name="Rating.getAverageRatingByUser", query="SELECT AVG(r.rating) FROM Rating r WHERE r.user.id = :user_id"),
    @NamedQuery(name="Rating.getRatingByUserAndVenue", query="SELECT r.rating FROM Rating r WHERE r.user.id = :user_id AND r.venue.id = :venue_id")
})
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    /*
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private long userId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "venue_id")
    private long venueId;
    */
    @Basic(optional = false)
    @NotNull
    @Column(name = "rating")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;
    
    public Rating() {
    }

    public Rating(Long id) {
        this.id = id;
    }

    public Rating(int rating, User user, Venue venue) {
        this.rating = rating;
        this.user = user;
        this.venue = venue;
    }    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Rating[ id=" + id + " ]";
    }
    
}
