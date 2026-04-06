package model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseId;
    private String description;
    private List<String> prerequisites;
    private String scheduleSlot; // e.g., "MWF 08:00-09:00"

    // Constructor
    public Course(String courseId, String description, String scheduleSlot) {
        this.courseId = courseId;
        this.description = description;
        this.scheduleSlot = scheduleSlot;
        this.prerequisites = new ArrayList<>();
    }

    // Method to add a prerequisite course ID
    public void addPrerequisite(String courseCode) {
        this.prerequisites.add(courseCode);
    }

    // Getters and Setters
    public String getCourseId() { return courseId; }
    public String getDescription() { return description; }
    public List<String> getPrerequisites() { return prerequisites; }
    public String getScheduleSlot() { return scheduleSlot; }
}
