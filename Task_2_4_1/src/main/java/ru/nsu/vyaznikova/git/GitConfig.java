package ru.nsu.vyaznikova.git;

import ru.nsu.vyaznikova.exception.GitOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles Git configuration and validation.
 */
public class GitConfig {
    private final String gitExecutable;
    private boolean isConfigured;

    public GitConfig() {
        this.gitExecutable = findGitExecutable();
        this.isConfigured = false;
    }

    public void validateConfiguration() {
        try {
            // Проверяем наличие глобальных настроек Git
            checkGlobalConfig();
            // Проверяем, настроена ли аутентификация
            checkAuthentication();
            isConfigured = true;
        } catch (IOException e) {
            throw new GitOperationException("Failed to validate Git configuration", e);
        }
    }

    private String findGitExecutable() {
        try {
            Process process = new ProcessBuilder("where", "git")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                if (line != null) {
                    return line.trim();
                }
            }

            throw new GitOperationException("Git executable not found in PATH");
        } catch (IOException e) {
            throw new GitOperationException("Failed to locate Git executable", e);
        }
    }

    private void checkGlobalConfig() throws IOException {
        Process process = new ProcessBuilder(gitExecutable, "config", "--global", "--list")
                .redirectErrorStream(true)
                .start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            
            boolean hasUserConfig = false;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("user.name=") || line.startsWith("user.email=")) {
                    hasUserConfig = true;
                    break;
                }
            }

            if (!hasUserConfig) {
                throw new GitOperationException("Git global configuration is missing. " +
                        "Please configure user.name and user.email");
            }
        }
    }

    private void checkAuthentication() throws IOException {
        Process process = new ProcessBuilder(gitExecutable, "config", "--global", "credential.helper")
                .redirectErrorStream(true)
                .start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String credentialHelper = reader.readLine();
            if (credentialHelper == null || credentialHelper.trim().isEmpty()) {
                throw new GitOperationException("Git credential helper is not configured. " +
                        "Please configure credential.helper");
            }
        }
    }

    public String getGitExecutable() {
        return gitExecutable;
    }

    public boolean isConfigured() {
        return isConfigured;
    }
}
