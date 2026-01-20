# Testing Guide

## Overview

This document describes how to test the GitHub issue automation system locally and in CI/CD.

## Prerequisites

- Node.js 20+
- GitHub personal access token
- AWS credentials with Bedrock access
- Test repository (recommended: use a separate test repo)

## Setup for Testing

### 1. Install Dependencies

```bash
cd scripts
npm install
```

### 2. Configure Environment

Copy the example environment file:

```bash
cp .env.example .env
```

Edit `.env` with your test credentials:

```env
GITHUB_TOKEN=ghp_your_test_token
GITHUB_REPOSITORY=your-username/test-repo
AWS_ACCESS_KEY_ID=your_aws_key
AWS_SECRET_ACCESS_KEY=your_aws_secret
AWS_REGION=us-east-1
```

### 3. Build TypeScript

```bash
npm run build
```

## Running Tests

### Unit Tests

Run all unit tests:

```bash
npm test
```

Run tests with coverage:

```bash
npm test -- --coverage
```

Run specific test file:

```bash
npm test -- github.test.ts
```

Run tests in watch mode:

```bash
npm test -- --watch
```

### Integration Tests

Integration tests require valid credentials and will interact with real APIs.

**Warning**: Integration tests will create/modify issues in your test repository.

```bash
# Set up test environment
export GITHUB_TOKEN="your_token"
export GITHUB_REPOSITORY="your-username/test-repo"
export AWS_ACCESS_KEY_ID="your_key"
export AWS_SECRET_ACCESS_KEY="your_secret"

# Run integration tests
npm test -- --testPathPattern=integration
```

## Manual Testing

### Test Issue Triage

1. Create a test issue in your repository:

```bash
gh issue create \
  --title "Bug: Application crashes on startup" \
  --body "When I start the application, it crashes immediately with error code 500."
```

2. Run the triage script:

```bash
export ISSUE_NUMBER=<issue_number>
npm run build && node dist/triage_issue.js
```

3. Verify:
   - Labels were applied to the issue
   - If duplicates exist, a comment was posted
   - Check console output for any errors

### Test Duplicate Closure

1. Create issues with duplicate label:

```bash
gh issue create --title "Test duplicate 1" --body "Test" --label "duplicate"
gh issue create --title "Test duplicate 2" --body "Test" --label "duplicate"
```

2. Manually set the label date to 3+ days ago (or modify threshold in config)

3. Run the close duplicates script:

```bash
npm run build && node dist/close_duplicates.js
```

4. Verify issues were closed with appropriate comments

### Test Stale Issue Closure

1. Create an issue with pending-response label:

```bash
gh issue create \
  --title "Test stale issue" \
  --body "Test" \
  --label "pending-response"
```

2. Ensure no activity for 7+ days (or modify threshold)

3. Run the close stale script:

```bash
npm run build && node dist/close_stale.js
```

4. Verify the issue was closed

## Testing Workflows

### Local Workflow Testing

Use [act](https://github.com/nektos/act) to test GitHub Actions locally:

```bash
# Install act
brew install act  # macOS
# or
curl https://raw.githubusercontent.com/nektos/act/master/install.sh | sudo bash

# Test issue triage workflow
act issues -e test-events/issue-opened.json

# Test scheduled workflows
act schedule
```

### Test Event Files

Create test event files in `test-events/`:

**test-events/issue-opened.json**:
```json
{
  "action": "opened",
  "issue": {
    "number": 1,
    "title": "Test issue",
    "body": "This is a test issue",
    "state": "open",
    "labels": []
  },
  "repository": {
    "name": "test-repo",
    "owner": {
      "login": "test-user"
    }
  }
}
```

## Test Coverage

### Current Coverage

Run coverage report:

```bash
npm test -- --coverage
```

Target coverage:
- Statements: > 80%
- Branches: > 75%
- Functions: > 80%
- Lines: > 80%

### Coverage Reports

Coverage reports are generated in `coverage/` directory:
- `coverage/lcov-report/index.html` - HTML report
- `coverage/lcov.info` - LCOV format for CI tools

## Continuous Integration

### GitHub Actions Testing

The workflows include built-in testing:

1. **Syntax Check**: TypeScript compilation
2. **Linting**: ESLint validation
3. **Unit Tests**: Jest test suite
4. **Build Verification**: Successful build

### CI Configuration

Tests run automatically on:
- Pull requests
- Pushes to main branch
- Scheduled workflow runs

## Troubleshooting Tests

### Tests Fail with Authentication Error

**Problem**: Tests fail with GitHub or AWS authentication errors

**Solution**:
- Verify credentials are set correctly
- Check token/key permissions
- Ensure test repository exists

### Tests Timeout

**Problem**: Tests timeout waiting for API responses

**Solution**:
- Increase Jest timeout: `jest.setTimeout(30000)`
- Check network connectivity
- Verify API endpoints are accessible

### Mock Data Issues

**Problem**: Tests fail due to unexpected mock data

**Solution**:
- Update mock data to match current API responses
- Check API version compatibility
- Review test fixtures

## Best Practices

### Writing Tests

1. **Isolate Tests**: Each test should be independent
2. **Use Mocks**: Mock external APIs for unit tests
3. **Test Edge Cases**: Include error scenarios
4. **Clear Names**: Use descriptive test names
5. **Setup/Teardown**: Clean up after tests

### Test Organization

```
src/__tests__/
├── unit/              # Unit tests
│   ├── github.test.ts
│   ├── bedrock.test.ts
│   └── utils/
├── integration/       # Integration tests
│   └── workflows.test.ts
└── fixtures/          # Test data
    └── sample-issues.json
```

### Running Specific Test Suites

```bash
# Unit tests only
npm test -- --testPathPattern=unit

# Integration tests only
npm test -- --testPathPattern=integration

# Specific file
npm test -- github.test.ts

# Tests matching pattern
npm test -- --testNamePattern="should fetch"
```

## Performance Testing

### Load Testing

Test with multiple concurrent issues:

```bash
# Create multiple test issues
for i in {1..10}; do
  gh issue create --title "Test issue $i" --body "Test body $i"
done

# Monitor workflow performance
gh run list --workflow=issue-triage.yml
```

### Rate Limit Testing

Monitor API rate limits:

```bash
# Check GitHub rate limit
gh api rate_limit

# Check AWS Bedrock throttling
aws bedrock list-foundation-models --region us-east-1
```

## Security Testing

### Credential Validation

Test with invalid credentials to ensure proper error handling:

```bash
export GITHUB_TOKEN="invalid_token"
npm run build && node dist/triage_issue.js
# Should fail gracefully with clear error message
```

### Input Validation

Test with malicious input:

```bash
# Test with XSS attempt in issue body
gh issue create \
  --title "Test XSS" \
  --body "<script>alert('xss')</script>"

# Verify output is properly sanitized
```

## Reporting Issues

When reporting test failures, include:

1. Test command used
2. Full error output
3. Environment details (Node version, OS)
4. Relevant configuration
5. Steps to reproduce

## Resources

- [Jest Documentation](https://jestjs.io/docs/getting-started)
- [GitHub Actions Testing](https://docs.github.com/en/actions/automating-builds-and-tests)
- [AWS Bedrock Testing](https://docs.aws.amazon.com/bedrock/latest/userguide/testing.html)
