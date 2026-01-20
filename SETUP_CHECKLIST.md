# GitHub Issue Automation - Setup Checklist

Use this checklist to set up the GitHub issue automation system in your repository.

## ☐ Prerequisites

- [ ] AWS Account with access to Bedrock
- [ ] GitHub repository with admin access
- [ ] Node.js 20+ installed (for local testing)

## ☐ AWS Configuration

- [ ] Enable AWS Bedrock in your account
- [ ] Request access to Claude Sonnet 4 model
- [ ] Create IAM user with Bedrock permissions
- [ ] Generate access key and secret key
- [ ] Save credentials securely

## ☐ GitHub Secrets

Go to **Settings → Secrets and variables → Actions**

- [ ] Add `AWS_ACCESS_KEY_ID`
- [ ] Add `AWS_SECRET_ACCESS_KEY`
- [ ] (Optional) Add `AWS_REGION`

## ☐ GitHub Labels

Create these labels in your repository:

- [ ] `bug` (color: d73a4a)
- [ ] `enhancement` (color: a2eeef)
- [ ] `documentation` (color: 0075ca)
- [ ] `question` (color: d876e3)
- [ ] `duplicate` (color: cfd3d7)
- [ ] `pending-triage` (color: fbca04)
- [ ] `pending-response` (color: d4c5f9)
- [ ] `pending-maintainer-response` (color: ededed)
- [ ] `help-wanted` (color: 008672)
- [ ] `good-first-issue` (color: 7057ff)

**Quick command:**
```bash
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

## ☐ Workflow Permissions

- [ ] Go to **Settings → Actions → General**
- [ ] Under "Workflow permissions", ensure "Read and write permissions" is selected
- [ ] Check "Allow GitHub Actions to create and approve pull requests"

## ☐ Enable Workflows

- [ ] Go to **Actions** tab
- [ ] Enable workflows if prompted
- [ ] Verify all three workflows appear:
  - Issue Triage
  - Close Duplicates
  - Close Stale Issues

## ☐ Test the System

- [ ] Create a test issue
- [ ] Wait 1-2 minutes for workflow to run
- [ ] Check that labels were automatically applied
- [ ] Review workflow run in Actions tab
- [ ] Verify no errors in logs

**Test command:**
```bash
gh issue create --title "Test: Automation verification" --body "This is a test issue to verify the automation system works correctly. Please ignore."
```

## ☐ Local Development Setup (Optional)

- [ ] Navigate to `scripts/` directory
- [ ] Run `npm install`
- [ ] Copy `.env.example` to `.env`
- [ ] Fill in environment variables
- [ ] Run `npm run build`
- [ ] Test locally with a sample issue

## ☐ Customization (Optional)

- [ ] Review thresholds in `scripts/src/config.ts`
- [ ] Adjust duplicate similarity threshold (default: 0.8)
- [ ] Adjust duplicate close days (default: 3)
- [ ] Adjust stale issue days (default: 7)
- [ ] Modify workflow schedules if needed

## ☐ Documentation

- [ ] Read `ISSUE_AUTOMATION.md` for detailed documentation
- [ ] Review `.github/workflows/README.md` for workflow details
- [ ] Check `scripts/README.md` for script documentation
- [ ] Share setup guide with team members

## ☐ Monitoring

- [ ] Set up AWS CloudWatch alerts for Bedrock usage
- [ ] Monitor GitHub Actions usage
- [ ] Review workflow runs weekly
- [ ] Check for failed runs and errors

## Verification

After completing all steps, verify the system is working:

1. **Create a test issue** with a clear bug description
2. **Wait 2-3 minutes** for the workflow to complete
3. **Check the issue** - it should have:
   - Automatically assigned labels
   - A comment if duplicates were detected
4. **Review the workflow run** in the Actions tab
5. **Check for any errors** in the logs

## Troubleshooting

If something doesn't work:

1. **Check workflow logs** in Actions tab
2. **Verify secrets** are correctly set
3. **Confirm AWS credentials** have Bedrock access
4. **Ensure labels exist** in the repository
5. **Check GitHub Actions permissions** are set correctly

## Next Steps

Once setup is complete:

- [ ] Monitor the first few automated triages
- [ ] Adjust thresholds based on results
- [ ] Train team on how the system works
- [ ] Document any custom configurations
- [ ] Set up cost monitoring in AWS

## Support

For issues or questions:
- Review `ISSUE_AUTOMATION.md`
- Check workflow logs
- Open an issue with the "automation" label

---

**Setup Date:** _____________

**Completed By:** _____________

**Notes:**
