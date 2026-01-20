# Quick Start Guide - GitHub Issue Automation

Get the GitHub issue automation system up and running in 10 minutes.

## Prerequisites

- AWS account with Bedrock access
- GitHub repository admin access
- AWS IAM credentials

## Step 1: AWS Setup (3 minutes)

1. **Enable Bedrock** in AWS Console
2. **Request Claude Sonnet 4 access** (may take a few minutes to approve)
3. **Create IAM user** with this policy:

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

4. **Generate access key** and save credentials

## Step 2: GitHub Secrets (2 minutes)

Go to **Settings → Secrets and variables → Actions** and add:

```
AWS_ACCESS_KEY_ID = your_access_key_here
AWS_SECRET_ACCESS_KEY = your_secret_key_here
```

## Step 3: Create Labels (2 minutes)

Run this one command:

```bash
gh label create "bug" --color "d73a4a" --description "Something isn't working" && \
gh label create "enhancement" --color "a2eeef" --description "New feature or request" && \
gh label create "documentation" --color "0075ca" --description "Improvements or additions to documentation" && \
gh label create "question" --color "d876e3" --description "Further information is requested" && \
gh label create "duplicate" --color "cfd3d7" --description "This issue is a duplicate" && \
gh label create "pending-triage" --color "fbca04" --description "Awaiting maintainer review" && \
gh label create "pending-response" --color "d4c5f9" --description "Awaiting response from issue author" && \
gh label create "pending-maintainer-response" --color "ededed" --description "Awaiting maintainer response" && \
gh label create "help-wanted" --color "008672" --description "Extra attention is needed" && \
gh label create "good-first-issue" --color "7057ff" --description "Good for newcomers"
```

Or create them manually in GitHub UI.

## Step 4: Enable Workflows (1 minute)

1. Go to **Settings → Actions → General**
2. Select **"Read and write permissions"**
3. Check **"Allow GitHub Actions to create and approve pull requests"**
4. Click **Save**

## Step 5: Test (2 minutes)

Create a test issue:

```bash
gh issue create --title "Bug: Application crashes on startup" --body "When I start the application, it immediately crashes with error code 500. This happens every time."
```

Wait 1-2 minutes, then check:
- Issue should have labels automatically applied
- Check **Actions** tab for workflow run
- Review logs for any errors

## That's It! 🎉

Your automation system is now active. Here's what happens automatically:

- **New issues** → AI analyzes and applies labels + detects duplicates
- **Duplicate issues** → Closed after 3 days
- **Stale issues** → Closed after 7 days of inactivity

## Next Steps

- Read `ISSUE_AUTOMATION.md` for detailed documentation
- Customize thresholds in `scripts/src/config.ts`
- Monitor workflow runs in Actions tab
- Adjust labels based on your needs

## Troubleshooting

**Workflow not running?**
- Check Actions are enabled in Settings
- Verify secrets are set correctly
- Check workflow permissions

**No labels applied?**
- Verify labels exist in repository
- Check AWS credentials have Bedrock access
- Review workflow logs for errors

**AWS authentication error?**
- Confirm IAM user has correct permissions
- Verify access key and secret are correct
- Check Bedrock is enabled in your region

## Cost Estimate

- **Per issue triage**: ~$0.01-0.02
- **Monthly (100 issues)**: ~$1-2
- **GitHub Actions**: Free for public repos

## Support

Need help? Check:
1. Workflow logs in Actions tab
2. `ISSUE_AUTOMATION.md` documentation
3. `SETUP_CHECKLIST.md` for detailed steps

---

**Total Setup Time**: ~10 minutes

**Maintenance Required**: Minimal - just monitor workflow runs occasionally
