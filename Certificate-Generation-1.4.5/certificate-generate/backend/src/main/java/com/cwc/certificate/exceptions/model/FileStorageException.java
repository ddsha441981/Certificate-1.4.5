package com.cwc.certificate.exceptions.model;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }


    public FileStorageException(String msg, Throwable e){
        super(msg,e);
    }

}
