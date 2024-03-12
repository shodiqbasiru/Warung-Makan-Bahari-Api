package com.enigma.wmb_api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {
    private String url;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("id")
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("path")
    private String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("size")
    private Long size;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("contentType")
    private String contentType;



}
