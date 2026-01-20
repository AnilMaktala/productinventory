# Contributing to GitHub Issue Automation

Thank you for your interest in contributing! This document provides guidelines for contributing to the project.

## Getting Started

### Prerequisites

- Node.js 20+
- Git
- GitHub account
- AWS account (for testing Bedrock integration)

### Setup Development Environment

1. Fork the repository
2. Clone your fork:
```bash
git clone https://github.com/your-username/repo-name.git
cd repo-name/scripts
```

3. Install dependencies:
```bash
npm install
```

4. Create `.env` file:
```bash
cp .env.example .env
# Edit .env with your credentials
```

5. Build the project:
```bash
npm run build
```

## Development Workflow

### 1. Create a Branch

```bash
git checkout -b feature/your-feature-name
# or
git checkout -b fix/your-bug-fix
```

### 2. Make Changes

- Write clean, readable code
- Follow existing code style
- Add tests for new features
- Update documentation

### 3. Test Your Changes

```bash
# Run linter
npm run lint

# Run tests
npm test

# Build project
npm run build
```

### 4. Commit Changes

Use conventional commit messages:

```bash
git commit -m "feat: add new feature"
git commit -m "fix: resolve bug in duplicate detection"
git commit -m "docs: update README"
git commit -m "test: add tests for rate limiter"
```

Commit types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `test`: Test additions/changes
- `refactor`: Code refactoring
- `style`: Code style changes
- `chore`: Build/tooling changes

### 5. Push and Create PR

```bash
git push origin feature/your-feature-name
```

Then create a pull request on GitHub.

## Code Style

### TypeScript

- Use TypeScript strict mode
- Provide type annotations
- Avoid `any` type when possible
- Use interfaces for object shapes

**Example**:
```typescript
interface IssueData {
  number: number;
  title: string;
  body: string | null;
}

function processIssue(issue: IssueData): void {
  // Implementation
}
```

### Naming Conventions

- **Files**: kebab-case (`rate-limiter.ts`)
- **Classes**: PascalCase (`GitHubClient`)
- **Functions**: camelCase (`analyzeIssue`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_RETRIES`)
- **Interfaces**: PascalCase with `I` prefix optional (`IssueData`)

### Code Organization

```typescript
// 1. Imports
import { Octokit } from '@octokit/rest';
import { config } from './config';

// 2. Types/Interfaces
interface MyInterface {
  // ...
}

// 3. Constants
const MAX_RETRIES = 3;

// 4. Class/Function definitions
export class MyClass {
  // ...
}
```

### Comments

- Use JSDoc for public APIs
- Explain "why", not "what"
- Keep comments up to date

```typescript
/**
 * Analyzes an issue using AWS Bedrock AI
 * @param issue - The issue to analyze
 * @param existingIssues - List of existing issues for duplicate detection
 * @returns Analysis results with labels and duplicates
 */
async function analyzeIssue(
  issue: Issue,
  existingIssues: Issue[]
): Promise<BedrockResponse> {
  // Implementation
}
```

## Testing Guidelines

### Writing Tests

1. **Test file naming**: `*.test.ts`
2. **Test organization**: Group related tests with `describe`
3. **Test naming**: Use descriptive names

```typescript
describe('GitHubClient', () => {
  describe('getOpenIssues', () => {
    it('should fetch all open issues', async () => {
      // Test implementation
    });

    it('should handle API errors gracefully', async () => {
      // Test implementation
    });
  });
});
```

### Test Coverage

- Aim for > 80% coverage
- Test happy paths and error cases
- Test edge cases and boundary conditions

### Running Tests

```bash
# All tests
npm test

# Specific file
npm test -- github.test.ts

# With coverage
npm test -- --coverage

# Watch mode
npm test -- --watch
```

## Documentation

### Code Documentation

- Document all public APIs
- Include usage examples
- Explain complex logic

### README Updates

When adding features:
1. Update main README.md
2. Add usage examples
3. Update configuration docs
4. Add troubleshooting tips

### Changelog

Update CHANGELOG.md (if exists) with:
- New features
- Bug fixes
- Breaking changes
- Deprecations

## Pull Request Process

### Before Submitting

- [ ] Code follows style guidelines
- [ ] Tests pass locally
- [ ] New tests added for new features
- [ ] Documentation updated
- [ ] Commit messages follow conventions
- [ ] No merge conflicts

### PR Description

Include:
1. **What**: What changes were made
2. **Why**: Why these changes were needed
3. **How**: How the changes work
4. **Testing**: How to test the changes
5. **Screenshots**: If UI changes

**Template**:
```markdown
## Description
Brief description of changes

## Motivation
Why these changes are needed

## Changes
- Change 1
- Change 2

## Testing
How to test these changes

## Checklist
- [ ] Tests pass
- [ ] Documentation updated
- [ ] No breaking changes
```

### Review Process

1. Maintainer reviews PR
2. Address feedback
3. Update PR as needed
4. Maintainer approves and merges

## Issue Guidelines

### Reporting Bugs

Include:
- Clear title
- Steps to reproduce
- Expected behavior
- Actual behavior
- Environment details
- Error messages/logs

### Feature Requests

Include:
- Clear description
- Use case
- Proposed solution
- Alternative solutions considered

### Issue Labels

- `bug`: Something isn't working
- `enhancement`: New feature request
- `documentation`: Documentation improvements
- `good-first-issue`: Good for newcomers
- `help-wanted`: Extra attention needed

## Security

**Never commit**:
- API keys or tokens
- AWS credentials
- Passwords
- Private keys

**Report security issues**:
- Do NOT open public issues
- Email maintainers directly
- Use GitHub Security Advisories

## Code of Conduct

### Our Standards

- Be respectful and inclusive
- Welcome newcomers
- Accept constructive criticism
- Focus on what's best for the community

### Unacceptable Behavior

- Harassment or discrimination
- Trolling or insulting comments
- Personal or political attacks
- Publishing others' private information

## Getting Help

- Check documentation first
- Search existing issues
- Ask in discussions
- Tag maintainers if urgent

## Recognition

Contributors will be:
- Listed in CONTRIBUTORS.md
- Mentioned in release notes
- Credited in documentation

## License

By contributing, you agree that your contributions will be licensed under the same license as the project.

## Questions?

Feel free to:
- Open a discussion
- Ask in issues
- Contact maintainers

Thank you for contributing! 🎉
