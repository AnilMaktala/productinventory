# GitHub Issue Automation Setup Guide

This repository includes automated GitHub issue management using AWS Bedrock AI.

## Quick Start

### 1. Set Up AWS Bedrock

1. **Enable AWS Bedrock** in your AWS account
2. **Request access** to Claude Sonnet 4 model in the Bedrock console
3. **Create IAM user** with these permissions:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["bedrock:InvokeModel"],
      "Resource": "arn:aws:bedrock:*::foundation-model/anthropic.claude-sonnet-4-*"
    }
  ]
}
```

### 2. Configure GitHub Secrets

Go to **Settings → Secrets and variables → Actions** and add:

- `AWS_ACCESS_KEY_ID` - Your AWS access key
- `AWS_SECRET_ACCESS_KEY` - Your AWS secret key
- (Optional) `AWS_REGION` - AWS region (defaults to us-east-1)

### 3. Create Labels

Run these commands or create labels manually in GitHub:

```bash
gh label create "bug" --color "d73a4a" --description "Something isn't working"
gh label create "enhancement" --color "a2eeef" --description "New feature or request"
gh label create "documentation" --color "0075ca" --description "Improvements or additions to documentation"
gh label create "question" --color "d876e3" --description "Further information is requested"
gh label create "duplicate" --color "cfd3d7" --description "This issue is a duplicate"
gh label create "pending-triage" --color "fbca04" --description "Awaiting maintainer review"
gh label create "pending-response" --color "d4c5f9" --description "Awaiting response from issue author"
gh label create "pending-maintainer-response" --color "ededed" --description "Awaiting maintainer response"
```

### 4. Test the Automation

Create a test issue to verify the automation works:

```bash
gh issue create --title "Test issue for automation" --body "This is a test issue to verify the automation system is working correctly."
```

The issue should automatically receive labels within a few minutes.

## What Gets Automated

### ✅ New Issue Triage (Automatic)
- **Trigger**: When any issue is opened
- **Actions**:
  - AI analyzes issue content
  - Assigns relevant labels (bug, enhancement, documentation, etc.)
  - Detects potential duplicates
  - Posts comment if duplicates found
  - Adds "duplicate" label if applicable

### ✅ Close Duplicates (Daily at Midnight UTC)
- **Trigger**: Scheduled daily or manual
- **Actions**:
  - Finds issues with "duplicate" label
  - Closes issues labeled for 3+ days
  - Posts closing comment

### ✅ Close Stale Issues (Daily at Midnight UTC)
- **Trigger**: Scheduled daily or manual
- **Actions**:
  - Finds issues with "pending-response" label
  - Closes issues with no activity for 7+ days
  - Posts closing comment

## Manual Workflow Triggers

You can manually trigger the scheduled workflows:

```bash
# Close duplicate issues now
gh workflow run close-duplicates.yml

# Close stale issues now
gh workflow run close-stale.yml
```

## Customization

### Adjust Thresholds

Edit `scripts/src/config.ts`:

```typescript
thresholds: {
  duplicateSimilarity: 0.8,    // Duplicate detection threshold (0-1)
  duplicateCloseDays: 3,       // Days before closing duplicates
  staleIssueDays: 7,           // Days before closing stale issues
}
```

### Modify Labels

Edit the label list in `scripts/src/bedrock.ts` in the `buildAnalysisPrompt` method.

### Change Schedules

Edit the cron expressions in workflow files (`.github/workflows/*.yml`):

```yaml
on:
  schedule:
    - cron: "0 0 * * *"  # Daily at midnight UTC
```

## Monitoring

### View Workflow Runs

1. Go to **Actions** tab in GitHub
2. Select a workflow (Issue Triage, Close Duplicates, or Close Stale)
3. Click on a run to see detailed logs

### Check Workflow Status

```bash
# List recent workflow runs
gh run list

# View specific run
gh run view <run-id>
```

## Troubleshooting

### Issue: Workflows not running

**Check:**
- Workflows are enabled in Settings → Actions
- GitHub Actions has write permissions for issues
- Secrets are correctly configured

### Issue: AWS authentication fails

**Check:**
- AWS credentials are valid
- IAM user has Bedrock permissions
- Bedrock is enabled in your AWS region
- Claude Sonnet 4 model access is granted

### Issue: No labels applied

**Check:**
- Labels exist in the repository
- Workflow logs for errors
- AWS Bedrock API is responding

### Issue: Rate limiting

**Check:**
- GitHub API rate limits: `gh api rate_limit`
- Consider using GitHub App instead of personal token for higher limits

## Local Development

To test scripts locally:

```bash
cd scripts
npm install
npm run build

# Set environment variables
export GITHUB_TOKEN="your_token"
export GITHUB_REPOSITORY="owner/repo"
export AWS_ACCESS_KEY_ID="your_key"
export AWS_SECRET_ACCESS_KEY="your_secret"
export ISSUE_NUMBER=123

# Run triage
node dist/triage_issue.js
```

## Cost Considerations

### AWS Bedrock Costs

- Claude Sonnet 4 pricing varies by region
- Typical cost per issue triage: ~$0.01-0.02
- Monitor usage in AWS Cost Explorer

### GitHub Actions

- Public repositories: Free unlimited minutes
- Private repositories: Free tier includes 2,000 minutes/month

## Security Best Practices

1. **Never commit credentials** - Use GitHub Secrets
2. **Limit IAM permissions** - Only grant Bedrock access
3. **Rotate credentials** regularly
4. **Monitor AWS usage** for unexpected activity
5. **Review workflow logs** for sensitive data

## Support

For issues with the automation system:
1. Check workflow logs in Actions tab
2. Review this documentation
3. Open an issue with the "automation" label

## Architecture

```
┌─────────────────┐
│  GitHub Issue   │
│    Created      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ GitHub Actions  │
│  Workflow Run   │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  TypeScript     │
│    Scripts      │
└────────┬────────┘
         │
    ┌────┴────┐
    │         │
    ▼         ▼
┌────────┐ ┌──────────┐
│ GitHub │ │   AWS    │
│  API   │ │ Bedrock  │
└────────┘ └──────────┘
```

## Files Overview

```
.github/workflows/
├── issue-triage.yml       # Triggered on new issues
├── close-duplicates.yml   # Daily scheduled
├── close-stale.yml        # Daily scheduled
└── README.md              # Workflow documentation

scripts/
├── src/
│   ├── triage_issue.ts    # Main triage logic
│   ├── close_duplicates.ts
│   ├── close_stale.ts
│   ├── bedrock.ts         # AWS Bedrock client
│   ├── github.ts          # GitHub API client
│   ├── config.ts          # Configuration
│   └── types.ts           # TypeScript types
├── package.json
├── tsconfig.json
└── README.md              # Scripts documentation
```
