/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans.util;

import java.util.Comparator;

/**
 *
 * @author JAno
 */
public class SortPredictionByRating implements Comparator<ItemPrediction>{

    @Override
    public int compare(ItemPrediction o1, ItemPrediction o2) {
        return o2.getRating() > o1.getRating() ? 1 : (o2.getRating() < o1.getRating() ) ? -1 : 0;
    }
    
}
