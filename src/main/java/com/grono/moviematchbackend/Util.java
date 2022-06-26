package com.grono.moviematchbackend;

import com.google.common.collect.Sets;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;


public class Util {
    public static <E> List<E> addElement(List<E> list, E object ){
        List<E> newList = new ArrayList<>(list);
        newList.add(object);
        return newList;
    }

    public static <E> Set<E> intersection(List<Set<E>> sets){
        Set<E> result = Sets.newHashSet(sets.get(0));
        for(Set<E> set : sets){
            result = Sets.intersection(result, Sets.newHashSet(set));
        }
        return result;
    }

}
