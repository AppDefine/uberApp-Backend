package com.appdefine.uber.uberApp.configs;

import com.appdefine.uber.uberApp.dto.PointDto;
import com.appdefine.uber.uberApp.utils.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(PointDto.class, Point.class).setConverter(mappingContext -> {
           PointDto pointDto = mappingContext.getSource();
           return GeometryUtil.createPoint(pointDto);
        });

        modelMapper.typeMap(Point.class,PointDto.class).setConverter(mappingContext -> {
            Point point = mappingContext.getSource();
            double coordinate[] = {
                    point.getX(),
                    point.getY()
            };
            return new PointDto(coordinate);
        });

        return modelMapper;
    }
}
