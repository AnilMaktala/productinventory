# Testing GitHub Issue Automation in Your Repository

## Complete Testing Guide

This guide walks you through testing the issue automation system with a real GitHub issue.

## Prerequisites

Before testing, ensure you have:
- [ ] GitHub repository with admin access
- [ ] AWS account with Bedrock access
- [ ] GitHub CLI installed (`gh`) - Optional but recommended

## Step 1: Configure AWS Bedrock

### 1.1 Enable AWS Bedrock

1. Log into AWS Console
2. Navigate to **Amazon Bedrock**
3. Go to **Model access** in the left sidebar
4. Click **Manage model access**
5. Find **Anthropic** → **Claude Sonnet 4**
6. Check the box and click **Request model access**
7. Wait for approval (usually instant)

### 1.2 Create IAM User

1. Go to **IAM** in AWS Console
2. Click **Users** → **Create user**
3. User name: `github-issue-automation`
4. Click **Next**
5. Select **Attach policies directly**
6. Click **Create policy** → **JSON**
7. Paste this policy:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "bedrock:InvokeModel"
      ],
      "Resource": "arn:aws:bedrock:*::foundation-model/anthropic.claude-sonnet-4-*"
    }
  ]
}
```

8. Name it: `BedrockInvokeModelPolicy`
9. Create the policy
10. Go back and attach it to your user
11. Click **Create user**

### 1.3 Generate Access Keys

1. Click on the created user
2. Go to **Security credentials** tab
3. Click **Create access key**
4. Select **Application running outside AWS**
5. Click **Next** → **Create access key**
6. **IMPORTANT**: Copy both:
   - Access key ID
   - Secret access key
7. Store them securely (you won't see the secret again)

## Step 2: Configure GitHub Secrets

### Option A: Using GitHub CLI (Recommended)

```bash
# Set AWS credentials
gh secret set AWS_ACCESS_KEY_ID
# Paste your access key ID when prompted

gh secret set AWS_SECRET_ACCESS_KEY
# Paste your secret access key when prompted

# Optional: Set AWS region (defaults to us-east-1)
gh secret set AWS_REGION
# Type: us-east-1 (or your preferred region)
```

### Option B: Using GitHub Web UI

1. Go to your repository on GitHub
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Add these secrets:

| Name | Value |
|------|-------|
| `AWS_ACCESS_KEY_ID` | Your AWS access key ID |
| `AWS_SECRET_ACCESS_KEY` | Your AWS secret access key |
| `AWS_REGION` | `us-east-1` (optional) |

## Step 3: Create Required Labels

### Option A: Using GitHub CLI (Fast)

```bash
# Create all labels at once
gh label create "bug" --color "d73a4a" --description "Something isn't working"
gh label create "enhancement" --color "a2eeef" --description "New feature or request"
gh label create "documentation" --color "0075ca" --description "Improvements or additions to documentation"
gh label create "question" --color "d876e3" --description "Further information is requested"
gh label create "duplicate" --color "cfd3d7" --description "This issue is a duplicate"
gh label create "pending-triage" --color "fbca04" --description "Awaiting maintainer review"
gh label create "pending-response" --color "d4c5f9" --description "Awaiting response from issue author"
gh label create "pending-maintainer-response" --color "ededed" --description "Awaiting maintainer response"
gh label create "help-wanted" --color "008672" --description "Extra attention is needed"
gh label create "good-first-issue" --color "7057ff" --description "Good for newcomers"
```

### Option B: Using GitHub Web UI

1. Go to **Issues** → **Labels**
2. Click **New label** for each:

| Label | Color | Description |
|-------|-------|-------------|
| bug | #d73a4a | Something isn't working |
| enhancement | #a2eeef | New feature or request |
| documentation | #0075ca | Improvements or additions to documentation |
| question | #d876e3 | Further information is requested |
| duplicate | #cfd3d7 | This issue is a duplicate |
| pending-triage | #fbca04 | Awaiting maintainer review |
| pending-response | #d4c5f9 | Awaiting response from issue author |
| pending-maintainer-response | #ededed | Awaiting maintainer response |
| help-wanted | #008672 | Extra attention is needed |
| good-first-issue | #7057ff | Good for newcomers |

## Step 4: Enable Workflow Permissions

1. Go to **Settings** → **Actions** → **General**
2. Scroll to **Workflow permissions**
3. Select **"Read and write permissions"**
4. Check **"Allow GitHub Actions to create and approve pull requests"**
5. Click **Save**

## Step 5: Verify Workflows Are Enabled

1. Go to **Actions** tab
2. You should see three workflows:
   - Issue Triage
   - Close Duplicates
   - Close Stale Issues
3. If you see "Workflows disabled", click **Enable workflows**

## Step 6: Create Test Issues

### Test 1: Bug Report (Basic Test)

```bash
gh issue create \
  --title "Bug: Application crashes on startup" \
  --body "When I start the application, it immediately crashes with error code 500. This happens every time I try to launch it. Steps to reproduce: 1. Start application 2. See crash"
```

**Expected Result**:
- ✅ Issue created
- ✅ Workflow runs automatically (check Actions tab)
- ✅ Labels applied (should get "bug" label)
- ✅ No duplicate comment (first issue)

### Test 2: Feature Request

```bash
gh issue create \
  --title "Feature Request: Add dark mode support" \
  --body "It would be great to have a dark mode option for the application. Many users prefer dark themes, especially when working at night. This would improve user experience significantly."
```

**Expected Result**:
- ✅ Should get "enhancement" label
- ✅ Workflow completes successfully

### Test 3: Documentation Issue

```bash
gh issue create \
  --title "Documentation: API endpoints not documented" \
  --body "The API documentation is missing information about the authentication endpoints. Please add documentation for /api/auth/* endpoints with examples and parameters."
```

**Expected Result**:
- ✅ Should get "documentation" label

### Test 4: Question

```bash
gh issue create \
  --title "Question: How to configure database connection?" \
  --body "I'm trying to set up the application but I'm not sure how to configure the database connection. Can someone help me understand the configuration options?"
```

**Expected Result**:
- ✅ Should get "question" label

### Test 5: Duplicate Detection

```bash
# Create a similar issue to Test 1
gh issue create \
  --title "Bug: App crashes when starting" \
  --body "The application crashes immediately after I start it. Getting error 500. This is blocking my work."
```

**Expected Result**:
- ✅ Should get "bug" label
- ✅ Should get "duplicate" label
- ✅ Should have a comment mentioning issue #1 as potential duplicate
- ✅ Comment should show similarity percentage

## Step 7: Monitor Workflow Execution

### Using GitHub Web UI

1. Go to **Actions** tab
2. Click on **Issue Triage** workflow
3. Click on the latest run
4. Expand the steps to see logs
5. Look for:
   - ✅ "Triaging issue #X..."
   - ✅ "Adding labels: ..."
   - ✅ "✅ Triage completed successfully"

### Using GitHub CLI

```bash
# List recent workflow runs
gh run list --workflow=issue-triage.yml --limit 5

# View specific run (get ID from list above)
gh run view <run-id>

# Watch run in real-time
gh run watch <run-id>

# View logs
gh run view <run-id> --log
```

## Step 8: Verify Results

### Check Issue Labels

```bash
# View issue details
gh issue view <issue-number>

# Should show:
# - Title
# - Body
# - Labels (automatically applied)
# - Comments (if duplicate detected)
```

### Check for Duplicate Comments

If you created the duplicate test issue:

1. Open the duplicate issue
2. Look for a comment from GitHub Actions bot
3. Should say: "🤖 **Potential Duplicate Detected**"
4. Should list similar issues with similarity percentages

## Step 9: Test Scheduled Workflows

### Test Duplicate Closure (Manual Trigger)

```bash
# Manually trigger the workflow
gh workflow run close-duplicates.yml

# Wait a few seconds, then check status
gh run list --workflow=close-duplicates.yml --limit 1
```

**Note**: Issues won't close immediately - they need the duplicate label for 3+ days.

### Test Stale Issue Closure (Manual Trigger)

```bash
# First, add pending-response label to an issue
gh issue edit <issue-number> --add-label "pending-response"

# Manually trigger the workflow
gh workflow run close-stale.yml

# Check status
gh run list --workflow=close-stale.yml --limit 1
```

**Note**: Issues won't close immediately - they need 7+ days of inactivity.

## Troubleshooting

### Issue 1: Workflow Doesn't Run

**Check**:
```bash
# Verify workflows are enabled
gh workflow list

# Check if workflow file exists
ls -la .github/workflows/issue-triage.yml
```

**Fix**:
1. Go to Actions tab
2. Enable workflows if disabled
3. Check workflow file syntax

### Issue 2: No Labels Applied

**Check**:
```bash
# View workflow logs
gh run view <run-id> --log

# Check for errors
gh run view <run-id> --log | grep -i error
```

**Common Causes**:
- AWS credentials not set correctly
- Labels don't exist in repository
- Bedrock model access not granted

**Fix**:
1. Verify secrets: `gh secret list`
2. Verify labels: `gh label list`
3. Check AWS Bedrock model access

### Issue 3: AWS Authentication Error

**Error Message**: `UnrecognizedClientException`

**Fix**:
1. Verify AWS credentials in GitHub Secrets
2. Check IAM user has Bedrock permissions
3. Verify AWS region is correct
4. Test credentials locally:

```bash
# Set credentials locally
export AWS_ACCESS_KEY_ID="your_key"
export AWS_SECRET_ACCESS_KEY="your_secret"
export AWS_REGION="us-east-1"

# Test Bedrock access
aws bedrock list-foundation-models --region us-east-1
```

### Issue 4: Workflow Fails with "Resource not found"

**Fix**:
1. Ensure Claude Sonnet 4 model access is granted
2. Wait a few minutes after requesting access
3. Try a different region if needed

## Advanced Testing

### Test with Multiple Issues

Create 5-10 issues with different types to see how the AI classifies them:

```bash
# Bug reports
gh issue create --title "Bug: Memory leak in production" --body "Application memory usage grows continuously..."

# Features
gh issue create --title "Feature: Add export to CSV" --body "Users need ability to export data..."

# Documentation
gh issue create --title "Docs: Missing installation guide" --body "New users can't find installation instructions..."

# Questions
gh issue create --title "How to enable caching?" --body "What's the best way to configure caching..."

# Duplicates
gh issue create --title "Bug: Memory leak issue" --body "Memory keeps growing in production environment..."
```

### Monitor Costs

1. Go to AWS Cost Explorer
2. Filter by service: "Amazon Bedrock"
3. Check daily costs
4. Expected: ~$0.01-0.02 per issue

### Test Duplicate Detection Accuracy

1. Create an issue
2. Create 2-3 similar issues with slight variations
3. Check if they're detected as duplicates
4. Verify similarity scores make sense

## Verification Checklist

After testing, verify:

- [ ] Workflows run automatically on new issues
- [ ] Labels are applied correctly
- [ ] Bug reports get "bug" label
- [ ] Feature requests get "enhancement" label
- [ ] Documentation issues get "documentation" label
- [ ] Questions get "question" label
- [ ] Duplicates are detected
- [ ] Duplicate comments are posted
- [ ] Similarity scores are reasonable (>80%)
- [ ] No errors in workflow logs
- [ ] AWS costs are reasonable
- [ ] Workflow execution time is acceptable (<30 seconds)

## Cleanup Test Issues

After testing, clean up test issues:

```bash
# List all open issues
gh issue list

# Close test issues
gh issue close <issue-number> --comment "Closing test issue"

# Or close multiple at once
for i in 1 2 3 4 5; do
  gh issue close $i --comment "Closing test issue"
done
```

## Next Steps

Once testing is successful:

1. ✅ Monitor first few real issues
2. ✅ Adjust thresholds if needed (in `scripts/src/config.ts`)
3. ✅ Review and improve label taxonomy
4. ✅ Set up cost alerts in AWS
5. ✅ Document any custom configurations
6. ✅ Train team on the system

## Support

If you encounter issues:

1. Check workflow logs in Actions tab
2. Review `TROUBLESHOOTING.md`
3. Check `scripts/TEST_REPORT.md`
4. Verify all prerequisites are met
5. Test AWS credentials locally
6. Open an issue with logs

## Quick Reference

### Essential Commands

```bash
# Create test issue
gh issue create --title "Test" --body "Testing automation"

# View workflow runs
gh run list --workflow=issue-triage.yml

# View specific run
gh run view <run-id> --log

# Check secrets
gh secret list

# Check labels
gh label list

# Manually trigger workflow
gh workflow run close-duplicates.yml
```

### Important URLs

- **Actions**: `https://github.com/YOUR_USERNAME/YOUR_REPO/actions`
- **Secrets**: `https://github.com/YOUR_USERNAME/YOUR_REPO/settings/secrets/actions`
- **Labels**: `https://github.com/YOUR_USERNAME/YOUR_REPO/labels`
- **AWS Bedrock**: `https://console.aws.amazon.com/bedrock`
- **AWS IAM**: `https://console.aws.amazon.com/iam`

---

**Testing Time**: ~15-20 minutes  
**Prerequisites Setup**: ~10 minutes  
**Total Time**: ~30 minutes
