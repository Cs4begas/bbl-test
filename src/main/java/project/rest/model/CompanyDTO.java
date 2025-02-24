package project.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CompanyDTO {
    private String name;
    private String catchPhrase;
    private String bs;
}
