package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;

public class CommonOperations {
    public static final Operation DELETE_ALL =
            deleteAllFrom("tbl_user_role","tbl_role","tbl_confirmation_token","tbl_user", "tbl_post");
    public static final Operation DELETE_ALL_FOR_F1 =
            deleteAllFrom("tbl_racer_team","tbl_race_team","tbl_result","tbl_season_raceteam","tbl_racer","tbl_grand_prix","tbl_race_course",
                    "tbl_city","tbl_nation");
    public static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
