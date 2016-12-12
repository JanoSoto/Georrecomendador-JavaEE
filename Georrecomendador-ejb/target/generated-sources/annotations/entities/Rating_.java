package entities;

import entities.User;
import entities.Venue;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-12T01:42:44")
@StaticMetamodel(Rating.class)
public class Rating_ { 

    public static volatile SingularAttribute<Rating, Venue> venue;
    public static volatile SingularAttribute<Rating, Integer> rating;
    public static volatile SingularAttribute<Rating, Long> id;
    public static volatile SingularAttribute<Rating, User> user;

}