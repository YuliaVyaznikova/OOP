package ru.nsu.vyaznikova.model;

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
