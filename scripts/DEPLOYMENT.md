# Deployment Guide

## Overview

This guide covers deploying the GitHub issue automation system to production.

## Prerequisites

- GitHub repository with admin access
- AWS account with Bedrock access
- GitHub Actions enabled
- Required secrets configured

## Deployment Steps

### 1. Prepare Repository

Ensure all files are committed and pushed:

```bash
git add .
git commit -m "feat: add GitHub issue automation system"
git push origin main
```

### 2. Configure GitHub Secrets

Add secrets in **Settings → Secrets and variables → Actions**:

```bash
# Using GitHub CLI
gh secret set AWS_ACCESS_KEY_ID
gh secret set AWS_SECRET_ACCESS_KEY
gh secret set AWS_REGION  # Optional, defaults to us-east-1
```

Or manually through the GitHub UI.

### 3. Create Labels

Run the label creation script:

```bash
# Create all required labels
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

### 4. Enable Workflows

1. Go to **Actions** tab
2. Enable workflows if prompted
3. Verify all three workflows appear:
   - Issue Triage
   - Close Duplicates
   - Close Stale Issues

### 5. Set Workflow Permissions

1. Go to **Settings → Actions → General**
2. Under "Workflow permissions":
   - Select **"Read and write permissions"**
   - Check **"Allow GitHub Actions to create and approve pull requests"**
3. Click **Save**

### 6. Test Deployment

Create a test issue to verify automation:

```bash
gh issue create \
  --title "Test: Deployment verification" \
  --body "This is a test issue to verify the automation system is working correctly after deployment."
```

Wait 1-2 minutes and verify:
- Labels were automatically applied
- Check workflow run in Actions tab
- Review logs for any errors

### 7. Monitor Initial Runs

Watch the first few automated runs:

```bash
# List recent workflow runs
gh run list --workflow=issue-triage.yml --limit 5

# View specific run
gh run view <run-id>

# Watch run in real-time
gh run watch <run-id>
```

## Production Configuration

### Environment-Specific Settings

For production, consider adjusting thresholds in `scripts/src/config.ts`:

```typescript
export const config = {
  // ... other config
  thresholds: {
    duplicateSimilarity: 0.85,  // Higher threshold for production
    duplicateCloseDays: 5,      // Longer grace period
    staleIssueDays: 14,         // Longer before closing
  },
};
```

### Workflow Schedules

Adjust cron schedules for production load:

```yaml
# .github/workflows/close-duplicates.yml
on:
  schedule:
    - cron: "0 2 * * *"  # 2 AM UTC daily
```

### Rate Limiting

Configure rate limits for production:

```typescript
// In scripts/src/github.ts or bedrock.ts
const githubLimiter = new RateLimiter(0.5);  // Slower for production
const bedrockLimiter = new RateLimiter(0.3);
```

## Rollback Procedure

If issues occur, disable workflows immediately:

### Option 1: Disable via UI

1. Go to **Actions** tab
2. Select problematic workflow
3. Click **"..."** → **"Disable workflow"**

### Option 2: Disable via CLI

```bash
gh workflow disable issue-triage.yml
gh workflow disable close-duplicates.yml
gh workflow disable close-stale.yml
```

### Option 3: Emergency Rollback

```bash
# Revert the commit
git revert <commit-hash>
git push origin main
```

## Monitoring

### Workflow Monitoring

Set up monitoring for workflow failures:

```bash
# Check workflow status
gh run list --workflow=issue-triage.yml --status=failure

# Get workflow logs
gh run view <run-id> --log
```

### AWS Monitoring

Monitor AWS Bedrock usage:

1. Go to AWS CloudWatch
2. Create dashboard for Bedrock metrics
3. Set up alarms for:
   - High error rates
   - Throttling
   - Unexpected costs

### GitHub API Monitoring

Monitor GitHub API rate limits:

```bash
# Check current rate limit
gh api rate_limit

# Monitor in script
curl -H "Authorization: token $GITHUB_TOKEN" \
  https://api.github.com/rate_limit
```

## Cost Management

### AWS Bedrock Costs

Monitor and control costs:

1. **Set up billing alerts** in AWS
2. **Monitor usage** in Cost Explorer
3. **Set budget limits** for Bedrock

Estimated costs:
- Per issue triage: ~$0.01-0.02
- 100 issues/month: ~$1-2
- 1000 issues/month: ~$10-20

### GitHub Actions Costs

- **Public repos**: Free unlimited minutes
- **Private repos**: 2,000 free minutes/month

Monitor usage:
1. Go to **Settings → Billing**
2. View Actions usage
3. Set spending limits if needed

## Scaling Considerations

### High-Volume Repositories

For repositories with >100 issues/day:

1. **Increase rate limits**:
```typescript
const githubLimiter = new RateLimiter(2);  // 2 calls/second
```

2. **Batch processing**:
```typescript
// Process issues in batches
const batchSize = 10;
for (let i = 0; i < issues.length; i += batchSize) {
  const batch = issues.slice(i, i + batchSize);
  await Promise.all(batch.map(processIssue));
}
```

3. **Optimize duplicate detection**:
```typescript
// Only check recent issues
const recentIssues = existingIssues.filter(issue => {
  const age = Date.now() - new Date(issue.created_at).getTime();
  return age < 30 * 24 * 60 * 60 * 1000; // Last 30 days
});
```

### Multi-Region Deployment

For global teams, consider multi-region AWS setup:

```typescript
const regions = ['us-east-1', 'eu-west-1', 'ap-southeast-1'];
const region = selectClosestRegion(userLocation);
```

## Security Hardening

### Production Security Checklist

- [ ] Rotate all credentials
- [ ] Enable AWS CloudTrail logging
- [ ] Set up AWS GuardDuty
- [ ] Enable GitHub security features
- [ ] Configure Dependabot
- [ ] Set up secret scanning
- [ ] Enable 2FA for all accounts
- [ ] Review IAM permissions
- [ ] Set up audit logging
- [ ] Configure backup procedures

### Credential Rotation

Schedule regular credential rotation:

```bash
# Every 90 days
1. Generate new AWS access key
2. Update GitHub secret
3. Test with new credentials
4. Delete old access key
5. Document rotation in log
```

## Backup and Recovery

### Backup Configuration

Backup critical files:

```bash
# Backup workflow files
tar -czf workflows-backup.tar.gz .github/workflows/

# Backup scripts
tar -czf scripts-backup.tar.gz scripts/

# Store in secure location
aws s3 cp workflows-backup.tar.gz s3://backup-bucket/
```

### Recovery Procedure

If system fails:

1. **Restore from backup**:
```bash
aws s3 cp s3://backup-bucket/workflows-backup.tar.gz .
tar -xzf workflows-backup.tar.gz
```

2. **Verify configuration**:
```bash
cd scripts
npm install
npm run build
npm test
```

3. **Redeploy**:
```bash
git add .
git commit -m "restore: recover automation system"
git push origin main
```

## Maintenance

### Regular Maintenance Tasks

**Weekly**:
- Review workflow runs
- Check error logs
- Monitor costs

**Monthly**:
- Update dependencies
- Review and adjust thresholds
- Analyze automation effectiveness

**Quarterly**:
- Rotate credentials
- Security audit
- Performance review
- Cost optimization

### Dependency Updates

Keep dependencies up to date:

```bash
# Update npm packages
cd scripts
npm update
npm audit fix

# Test after updates
npm test
npm run build
```

## Troubleshooting

### Common Issues

**Workflows not running**:
```bash
# Check workflow status
gh workflow list

# Enable if disabled
gh workflow enable issue-triage.yml
```

**AWS authentication errors**:
```bash
# Verify secrets
gh secret list

# Test AWS credentials locally
aws bedrock list-foundation-models --region us-east-1
```

**High costs**:
```bash
# Review AWS usage
aws ce get-cost-and-usage \
  --time-period Start=2024-01-01,End=2024-01-31 \
  --granularity MONTHLY \
  --metrics BlendedCost
```

## Support

For deployment issues:
1. Check workflow logs
2. Review this documentation
3. Check TROUBLESHOOTING.md
4. Open an issue with deployment logs

## Rollout Strategy

### Phased Rollout

1. **Phase 1**: Deploy to test repository
2. **Phase 2**: Monitor for 1 week
3. **Phase 3**: Deploy to staging
4. **Phase 4**: Monitor for 1 week
5. **Phase 5**: Deploy to production
6. **Phase 6**: Monitor and optimize

### Success Criteria

- [ ] All workflows running successfully
- [ ] No authentication errors
- [ ] Labels applied correctly
- [ ] Duplicates detected accurately
- [ ] Costs within budget
- [ ] No performance issues

## Post-Deployment

### Verification Checklist

- [ ] Create test issue - labels applied
- [ ] Check workflow runs - all successful
- [ ] Review logs - no errors
- [ ] Monitor costs - within budget
- [ ] Test duplicate detection
- [ ] Test stale issue closure
- [ ] Verify notifications working
- [ ] Document any issues
- [ ] Update team on deployment

### Documentation Updates

After deployment:
1. Update README with production URLs
2. Document any configuration changes
3. Update troubleshooting guide
4. Share deployment notes with team

---

**Deployment Date**: _____________

**Deployed By**: _____________

**Version**: 1.0.0

**Status**: ✅ Deployed | ⚠️ Issues | ❌ Failed
