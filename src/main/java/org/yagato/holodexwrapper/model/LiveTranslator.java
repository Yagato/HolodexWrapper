package org.yagato.holodexwrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveTranslator {

    @JsonProperty("en")
    private int en;

    @JsonProperty("ja")
    private int ja;

    @JsonProperty("es")
    private int es;

    @JsonProperty("zh")
    private int zh;

    @JsonProperty("id")
    private int id;

    @JsonProperty("ru")
    private int ru;

    @JsonProperty("ko")
    private int ko;

}
