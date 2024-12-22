package com.cwc.certificate.service;

import com.cwc.certificate.dto.response.BirthdayResponse;

import java.util.List;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
public interface BirthdayService {

    List<BirthdayResponse> getBirthdaysForToday();
}
