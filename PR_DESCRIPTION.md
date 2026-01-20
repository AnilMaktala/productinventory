# Add GitHub Issue Automation System

## Overview

This PR adds a comprehensive GitHub issue automation system using AWS Bedrock AI, based on the implementation from [kirodotdev/Kiro#4911](https://github.com/kirodotdev/Kiro/pull/4911).

## Features

### 🤖 Automatic Issue Triage
- AI-powered label assignment using AWS Bedrock Claude Sonnet 4
- Intelligent duplicate detection with semantic similarity analysis
- Automatic comments on potential duplicates
- Runs automatically when new issues are opened

### 🔄 Duplicate Management
- Automatically closes issues marked as duplicates after 3 days
- Posts informative closing comments
- Runs daily at midnight UTC

### 🧹 Stale Issue Management
- Closes issues with "pending-response" label after 7 days of inactivity
- Tracks comments and label changes for activity
- Runs daily at midnight UTC

## What's Included

### GitHub Actions Workflows
- `.github/workflows/issue-triage.yml` - Triggered on new issues
- `.github/workflows/close-duplicates.yml` - Daily scheduled workflow
- `.github/workflows/close-stale.yml` - Daily scheduled workflow
- `.github/workflows/README.md` - Workflow documentation

### TypeScript Scripts
- `scripts/src/triage_issue.ts` - Main triage logic
- `scripts/src/close_duplicates.ts` - Duplicate closure logic
- `scripts/src/close_stale.ts` - Stale issue closure logic
- `scripts/src/bedrock.ts` - AWS Bedrock client
- `scripts/src/github.ts` - GitHub API client
- `scripts/src/config.ts` - Configuration management
- `scripts/src/types.ts` - TypeScript type definitions

### Configuration Files
- `scripts/package.json` - Node.js dependencies
- `scripts/tsconfig.json` - TypeScript configuration
- `scripts/.eslintrc.json` - ESLint configuration
- `scripts/jest.config.js` - Jest test configuration

### Documentation
- `ISSUE_AUTOMATION.md` - Complete setup and usage guide
- `SETUP_CHECKLIST.md` - Step-by-step setup checklist
- `scripts/README.md` - Scripts documentation
- `scripts/.env.example` - Environment variable template

## Setup Required

Before merging, the following setup is required:

### 1. AWS Bedrock Access
- Enable AWS Bedrock in your AWS account
- Request access to Claude Sonnet 4 model
- Create IAM user with Bedrock permissions

### 2. GitHub Secrets
Add these secrets in **Settings → Secrets and variables → Actions**:
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- (Optional) `AWS_REGION`

### 3. GitHub Labels
Create the following labels (or run the commands in `SETUP_CHECKLIST.md`):
- bug, enhancement, documentation, question
- duplicate, pending-triage, pending-response, pending-maintainer-response
- help-wanted, good-first-issue

### 4. Workflow Permissions
- Go to **Settings → Actions → General**
- Enable "Read and write permissions"
- Allow GitHub Actions to create and approve pull requests

## Testing

After setup, test the system:

```bash
# Create a test issue
gh issue create --title "Test: Automation verification" --body "Testing the automation system"

# Manually trigger workflows
gh workflow run close-duplicates.yml
gh workflow run close-stale.yml
```

## Configuration

Default thresholds (customizable in `scripts/src/config.ts`):
- Duplicate similarity: 0.8 (80% match)
- Duplicate close delay: 3 days
- Stale issue delay: 7 days

## Benefits

1. **Reduced Manual Triage** - AI automatically categorizes issues
2. **Better Issue Management** - Duplicates are detected and closed automatically
3. **Cleaner Issue Tracker** - Stale issues are automatically closed
4. **Consistent Labeling** - AI applies labels consistently
5. **Time Savings** - Maintainers spend less time on routine issue management

## Cost Considerations

- **AWS Bedrock**: ~$0.01-0.02 per issue triage
- **GitHub Actions**: Free for public repos, included in private repo plans

## Breaking Changes

None - this is a new feature addition.

## Checklist

- [x] GitHub Actions workflows created
- [x] TypeScript scripts implemented
- [x] Configuration files added
- [x] Documentation written
- [x] Setup checklist provided
- [ ] AWS Bedrock access configured (required before merge)
- [ ] GitHub secrets added (required before merge)
- [ ] Labels created (required before merge)
- [ ] Tested with sample issue (recommended before merge)

## Related

- Original PR: https://github.com/kirodotdev/Kiro/pull/4911
- AWS Bedrock Documentation: https://docs.aws.amazon.com/bedrock/
- GitHub Actions Documentation: https://docs.github.com/en/actions

## Screenshots

_Add screenshots of successful workflow runs after testing_

## Notes

- The system uses AWS Bedrock's Claude Sonnet 4 model for AI analysis
- All workflows include error handling and logging
- Scripts are written in TypeScript for type safety
- Local development and testing is supported

---

**Please complete the setup steps in `SETUP_CHECKLIST.md` before merging this PR.**
