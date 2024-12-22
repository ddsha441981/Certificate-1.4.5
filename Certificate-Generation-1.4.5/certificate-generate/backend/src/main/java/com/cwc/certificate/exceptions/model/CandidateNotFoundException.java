package com.cwc.certificate.exceptions.model;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
public class CandidateNotFoundException extends  RuntimeException{

    public CandidateNotFoundException(){

    }
    public CandidateNotFoundException(String msg){
        super(msg);
    }

}
