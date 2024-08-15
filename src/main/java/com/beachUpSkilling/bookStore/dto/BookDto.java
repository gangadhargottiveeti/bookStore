package com.beachUpSkilling.bookStore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private UUID id;

    private String title;

    private String description;

    private int release_year;
}
