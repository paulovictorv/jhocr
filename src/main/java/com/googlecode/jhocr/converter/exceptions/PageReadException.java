package com.googlecode.jhocr.converter.exceptions;

/**
 * Created by paulo on 10/06/15.
 */
public class PageReadException extends RuntimeException {
    public PageReadException(Exception e) {
        super(e);
    }
}
