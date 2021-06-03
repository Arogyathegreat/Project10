package kr.pnu.project10.Utility;

public class CreatorModel {
    private String creator_name;
    private String creator_instagram;
    private String creator_twitter;
    private String creator_youtube;
    private String creator_image;

    public CreatorModel() {
    }

    public CreatorModel(String creator_name, String creator_instagram, String creator_twitter, String creator_youtube, String creator_image) {
        this.creator_name = creator_name;
        this.creator_instagram = creator_instagram;
        this.creator_twitter = creator_twitter;
        this.creator_youtube = creator_youtube;
        this.creator_image = creator_image;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public String getCreator_instagram() {
        return creator_instagram;
    }

    public String getCreator_twitter() {
        return creator_twitter;
    }

    public String getCreator_youtube() {
        return creator_youtube;
    }

    public String getCreator_image() {
        return creator_image;
    }
}
