package kr.pnu.project10.Utility;

public class VideoModel {
    private String Video_Name;
    private String Video_Link;
    private String Video_Course;

    public VideoModel() {
    }

    public VideoModel(String video_Name, String video_Link, String video_Course) {
        this.Video_Name = video_Name;
        this.Video_Link = video_Link;
        this.Video_Course = video_Course;
    }

    public String getVideo_Name() {
        return Video_Name;
    }

    public String getVideo_Link() {
        return Video_Link;
    }

    public String getVideo_Course() {
        return Video_Course;
    }
}
