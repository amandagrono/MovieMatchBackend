package com.grono.moviematchbackend.model.Code;

import com.grono.moviematchbackend.Constants;
import lombok.Data;

import java.util.Date;
import java.util.Random;

@Data
public class Code {
    private String name;
    private Date expiry;


    private Code() {
        this.name = generateNewCode();
        this.expiry = new Date(System.currentTimeMillis() + 86400000);
    }

    public static Code getInstance(){
        return new Code();
    }

    public static String generateNewCode(){
        char[] array = new char[6];
        Random random = new Random();
        for(int i = 0; i < array.length; i++){
            array[i] = Constants.codeCharacters.charAt(random.nextInt(Constants.codeCharacters.length()));
        }
        return String.valueOf(array);
    }

}
