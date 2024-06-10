package com.cocus.codechallenge.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true, setterPrefix = "with")
@EqualsAndHashCode
public class File {

    private String name;

    private byte[] data;

}
