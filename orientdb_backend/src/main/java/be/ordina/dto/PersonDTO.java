package be.ordina.dto;

/**
 * Created by ToVn on 05/06/15.
 */
public class PersonDTO {


    private Long twitterId;
    private String screenName;

    private String name;
    private String description;

    public PersonDTO (Long twitterId, String screenName, String name, String description) {
        this.twitterId = twitterId;
        this.description = description;
        this.name = name;
        this.screenName = screenName;

    }


    public Long getTwitterId() {
        return twitterId;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
