package com.scheideggergroup.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.scheideggergroup.core.model.Coordinate;
import com.scheideggergroup.core.model.Polyline;
import com.scheideggergroup.core.model.Route;
import com.scheideggergroup.core.service.TransformationService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Service(value = "transformationService")
public class SmartCityTransformationServiceImpl implements TransformationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartCityTransformationServiceImpl.class);

    private MapperFacade mapper;

    /**
     * Builds and configures the Orika MapperFactory with Converts and Class Mappings
     */
    public SmartCityTransformationServiceImpl() {

        LOGGER.debug("On SmartCityTransformationServiceImpl() Constructor.");

        // Instantiate the Mapper Factory
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        // Application to ApplicationDto
        mapperFactory.classMap(
                LatLng.class,
                Coordinate.class)
                .field("lat", "latitude")
                .field("lng", "longitude")
                .mapNulls(true)
                .register();

        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * Maps a single object to instance of a Class.
     *
     * @param mapFromObject Object to be mapped
     * @param mapToClass    Class object to be mapped to
     * @param <F>           From Object Class
     * @param <T>           To Object Class
     * @return Mapped single instance
     */
    @Override
    public <F, T> T map(F mapFromObject, Class<T> mapToClass) {
        LOGGER.debug("Mapping from Class [{}] to class [{}]", mapFromObject.getClass().getName(), mapToClass.getClass().getName());
        return mapper.map(mapFromObject, mapToClass);
    }

	@Override
	public Route map(Polyline polyline) {
		EncodedPolyline abc = new EncodedPolyline(polyline.getEncodedPolyline());
        List<LatLng> t = abc.decodePath();
        Route route = new Route();
        List<Coordinate> coordList = new ArrayList<Coordinate>(); 

        for(LatLng coord : t){
        	coordList.add(map(coord, Coordinate.class));
        }
        
        route.setPath(coordList);
        
		return route;
	}

}
