package com.example.smessenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

@SpringBootApplication
public class MessengerApplication {
    public static <T> Page<T> toPage(List<T> objs, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), objs.size());
        List<T> subObjs = objs.subList(start, end);

        return new PageImpl<>(subObjs, pageable, objs.size());
    }
    public static void main(String[] args) {
        SpringApplication.run(MessengerApplication.class, args);
    }

}
