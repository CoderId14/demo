package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;

public class CommonOperations {
    public static final Operation DELETE_ALL =
            deleteAllFrom("tbl_user_role","tbl_role","tbl_confirmation_token","tbl_user", "tbl_post" );
    public static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
