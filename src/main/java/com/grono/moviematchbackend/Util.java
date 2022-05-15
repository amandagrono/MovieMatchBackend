package com.grono.moviematchbackend;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


public class Util {
    public static <E> List<E> addElement(List<E> list, E object ){
        List<E> newList = new ArrayList<>(list);
        newList.add(object);
        return newList;
    }


}
