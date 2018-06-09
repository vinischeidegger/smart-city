package com.scheideggergroup.core.service;

import com.scheideggergroup.core.model.Polyline;
import com.scheideggergroup.core.model.Route;

/**
 * Interface used in object transformations.
 * @author scheidv1
 *
 */
public interface TransformationService {
	
    <F, T> T map(F mapFromObject, Class<T> mapToClass);
    
    Route map(Polyline polyline);

}
