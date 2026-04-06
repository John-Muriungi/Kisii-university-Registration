package model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String studentNumber;
    private String name;
    private String academicStanding; // e.g., "Good", "Probation"
    private List<String> completedCourses; // For checking prerequisites
    private List<Course> registeredCourses; // Courses registered in current semester

    // Constructor
    public Student(String studentNumber, String name, String academicStanding) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.academicStanding = academicStanding;
        this.completedCourses = new ArrayList<>();
        this.registeredCourses = new ArrayList<>();
    }

    // Helper methods
    public void completeCourse(String courseId) {
        this.completedCourses.add(courseId);
    }

    public void registerForCourse(Course course) {
        this.registeredCourses.add(course);
    }

    // Getters
    public String getStudentNumber() { return studentNumber; }
    public String getAcademicStanding() { return academicStanding; }
    public List<String> getCompletedCourses() { return completedCourses; }
    public List<Course> getRegisteredCourses() { return registeredCourses; }
}