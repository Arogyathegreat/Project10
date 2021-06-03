package kr.pnu.project10.Utility;

class ProfileModel {
    private String user_name;
    private String user_email;
    private String user_uid;
    private String user_score;

    public ProfileModel(){}

    public ProfileModel(String user_name, String user_email, String user_uid, String user_score){
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_uid = user_uid;
        this.user_score = user_score;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public String getUser_score() {
        return user_score;
    }
}
