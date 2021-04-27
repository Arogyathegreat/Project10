package kr.pnu.project10.Utility;

public class CourseModel {
    private String Course_Name;
//    private String Course_Type;

    public CourseModel() {
    }

    public CourseModel(String course_Name) {
        this.Course_Name = course_Name;
    }

    public String getCourse_Name() {
        return Course_Name;
    }
}
