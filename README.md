# 159.251 - Assignment 1 - Text Editor

## Group Members
- **Yuxi Du** - [你的ID]
- **Ying Yang** - [你的ID]

## Project Overview
A feature-rich text editor developed in Java with GUI support, file format compatibility, and code syntax highlighting.

## Features
- ✅ Full GUI text editor
- ✅ File operations (New, Open, Save, Exit)
- ✅ Text editing (Copy, Paste, Cut, Select)
- ✅ Search functionality
- ✅ Time/Date insertion
- ✅ PDF export
- ✅ Syntax highlighting for Java, Python, C++
- ✅ ODT and RTF file support
- ✅ Docker containerization
- ✅ CI/CD with GitHub Actions

## How to Run

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker (optional)

### Using Maven
\\\ash
# Compile and run
mvn compile exec:java

# Run tests
mvn test

# Code quality checks
mvn pmd:pmd checkstyle:checkstyle
\\\

### Using Docker
\\\ash
# Build image
docker build -t text-editor .

# Run container
docker run -it --rm text-editor
\\\

## Configuration
Edit \config/editor-config.yml\ to customize editor settings.

## Repository Link
[GitHub Repository](https://github.com/lingran0131/251-Assignment1-2025-YuxiDu-YingYang)

## Git Contributions
### Yuxi Du's Key Commits:
- \6cb3b11\ - Initial project setup

### Ying Yang's Key Commits:  
- \[提交ID]\ - Add Maven dependencies and plugins
- \[提交ID]\ - Configure code quality tools
- \[提交ID]\ - Setup GitHub Actions CI
