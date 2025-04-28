package ru.nsu.fit.model;

/**
 * Represents a student in the OOP course.
 */
public class Student {
    private final String githubUsername;
    private final String fullName;
    private final String repositoryUrl;
    private Group group;

    public Student(String githubUsername, String fullName, String repositoryUrl) {
        this.githubUsername = githubUsername;
        this.fullName = fullName;
        this.repositoryUrl = repositoryUrl;
    }

    // Getters
    public String getGithubUsername() {
        return githubUsername;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public Group getGroup() {
        return group;
    }

    // Setter for group (needed for bidirectional relationship)
    void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Student{" +
                "githubUsername='" + githubUsername + '\'' +
                ", fullName='" + fullName + '\'' +
                ", group=" + (group != null ? group.getName() : "null") +
                '}';
    }
}
