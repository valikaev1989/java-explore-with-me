package ru.practicum.server.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class FormatPage {
    public static Pageable getPage(int from,int size){
        return PageRequest.of(from / size, size);
    }
}