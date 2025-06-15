package ru.nsu.vyaznikova.git;

import ru.nsu.vyaznikova.exception.GitOperationException;
import ru.nsu.vyaznikova.model.Student;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Manages Git operations for student repositories.
 */
public class GitManager {
    private final GitConfig gitConfig;
    private final Path baseDirectory;
    private static final int COMMAND_TIMEOUT_SECONDS = 60;

    public GitManager(GitConfig gitConfig, String baseDirectory) {
        this.gitConfig = gitConfig;
        this.baseDirectory = Paths.get(baseDirectory);
        if (!Files.exists(this.baseDirectory)) {
            try {
                Files.createDirectories(this.baseDirectory);
            } catch (IOException e) {
                throw new GitOperationException("Failed to create base directory", e);
            }
        }
    }

    public Path cloneOrUpdateRepository(Student student) {
        Path repoPath = baseDirectory.resolve(student.getGithubUsername());
        
        if (Files.exists(repoPath)) {
            updateRepository(repoPath);
        } else {
            cloneRepository(student.getRepositoryUrl(), repoPath);
        }
        
        return repoPath;
    }

    private void cloneRepository(String repositoryUrl, Path targetPath) {
        executeGitCommand(targetPath.getParent(),
                gitConfig.getGitExecutable(),
                "clone",
                repositoryUrl,
                targetPath.toString());
    }

    private void updateRepository(Path repoPath) {
        executeGitCommand(repoPath,
                gitConfig.getGitExecutable(),
                "reset",
                "--hard",
                "HEAD");
        
        executeGitCommand(repoPath,
                gitConfig.getGitExecutable(),
                "pull",
                "--rebase");
    }

    public List<LocalDateTime> getCommitHistory(Path repoPath, LocalDateTime since, LocalDateTime until) {
        List<LocalDateTime> commits = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        String[] command = {
            gitConfig.getGitExecutable(),
            "log",
            "--since=" + since.format(formatter),
            "--until=" + until.format(formatter),
            "--format=%aI"  // ISO 8601-strict format
        };

        try {
            Process process = new ProcessBuilder(command)
                    .directory(repoPath.toFile())
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    commits.add(LocalDateTime.parse(line.trim(), formatter));
                }
            }

            if (!process.waitFor(COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                process.destroy();
                throw new GitOperationException("Git log command timed out");
            }

            if (process.exitValue() != 0) {
                throw new GitOperationException("Git log command failed");
            }
        } catch (IOException | InterruptedException e) {
            throw new GitOperationException("Failed to get commit history", e);
        }

        return commits;
    }

    private void executeGitCommand(Path workingDir, String... command) {
        try {
            Process process = new ProcessBuilder(command)
                    .directory(workingDir.toFile())
                    .redirectErrorStream(true)
                    .start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            if (!process.waitFor(COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                process.destroy();
                throw new GitOperationException("Git command timed out: " + String.join(" ", command));
            }

            if (process.exitValue() != 0) {
                throw new GitOperationException("Git command failed: " + String.join(" ", command) +
                        "\nOutput: " + output);
            }
        } catch (IOException | InterruptedException e) {
            throw new GitOperationException("Failed to execute Git command: " + 
                    String.join(" ", command), e);
        }
    }
}
