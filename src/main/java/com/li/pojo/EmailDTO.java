package com.li.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.mail.Multipart;
import java.util.List;

/**
 * @author liql
 * @date 2021/7/2
 */
@Setter
@Getter
public class EmailDTO {

    private List<String> to;
    private List<String> cc;
    private String subject;

    private String text;

    private Multipart multiparts;



}
