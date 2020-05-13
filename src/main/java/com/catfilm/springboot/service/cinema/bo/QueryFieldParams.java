package com.catfilm.springboot.service.cinema.bo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

@Data
public class QueryFieldParams {
    private String cinemaId;
    private String fieldId;
}
