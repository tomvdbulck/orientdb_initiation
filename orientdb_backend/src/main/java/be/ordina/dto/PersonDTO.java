package be.ordina.dto;

/**
 * Created by ToVn on 05/06/15.
 */
public class PersonDTO {


    private Long twitterId;
    private String screenName;

    private String name;
    private String description;

    private Integer numberOfFollowers;
    private Integer linkedFollowers;

    public PersonDTO (Long twitterId, String screenName, String name, String description, Integer numberOfFollowers) {
        this.twitterId = twitterId;
        this.description = description;
        this.name = name;
        this.screenName = screenName;
        this.numberOfFollowers = numberOfFollowers;

        linkedFollowers = 0;
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

    public Integer getNumberOfFollowers() {return numberOfFollowers;}

    public void setLinkedFollowers(Integer linkedFollowers) {this.linkedFollowers = linkedFollowers;}

    public Integer getLinkedFollowers() {return linkedFollowers;}
}
