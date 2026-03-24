package com.it.Mujakjung_be.gobal.memeber.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private int status;

   public String getMessage(){
       return message;
   }
   public int getStatus(){
       return status;
   }
}
