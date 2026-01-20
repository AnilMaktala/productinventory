# Testing Checklist - GitHub Issue Automation

Use this checklist to test the issue automation system step by step.

## Pre-Testing Setup

### AWS Configuration
- [ ] AWS account created
- [ ] Bedrock service enabled
- [ ] Claude Sonnet 4 model access requested and approved
- [ ] IAM user created with Bedrock permissions
- [ ] Access key ID generated
- [ ] Secret access key generated and saved securely

### GitHub Configuration
- [ ] Repository has admin access
- [ ] GitHub CLI installed (optional but recommended)
- [ ] AWS_ACCESS_KEY_ID secret added
- [ ] AWS_SECRET_ACCESS_KEY secret added
- [ ] AWS_REGION secret added (optional)

### Repository Setup
- [ ] All workflow files committed and pushed
- [ ] Workflows enabled in Actions tab
- [ ] Workflow permissions set to "Read and write"
- [ ] Required labels created (bug, enhancement, documentation, question, duplicate, etc.)

## Testing Phase 1: Basic Functionality

### Test 1: Bug Report
- [ ] Create bug report issue
- [ ] Wait 1-2 minutes for workflow to run
- [ ] Check Actions tab - workflow should be running/completed
- [ ] Verify issue has "bug" label applied
- [ ] Check workflow logs for success message
- [ ] No errors in workflow logs

**Command**:
```bash
gh issue create --title "Bug: Test crash" --body "Application crashes on startup"
```

**Expected**: Issue #1 with "bug" label

### Test 2: Feature Request
- [ ] Create feature request issue
- [ ] Wait for workflow to complete
- [ ] Verify issue has "enhancement" label
- [ ] Check workflow logs

**Command**:
```bash
gh issue create --title "Feature: Add dark mode" --body "Please add dark mode support"
```

**Expected**: Issue #2 with "enhancement" label

### Test 3: Documentation Issue
- [ ] Create documentation issue
- [ ] Wait for workflow to complete
- [ ] Verify issue has "documentation" label
- [ ] Check workflow logs

**Command**:
```bash
gh issue create --title "Docs: Missing API guide" --body "API documentation is incomplete"
```

**Expected**: Issue #3 with "documentation" label

### Test 4: Question
- [ ] Create question issue
- [ ] Wait for workflow to complete
- [ ] Verify issue has "question" label
- [ ] Check workflow logs

**Command**:
```bash
gh issue create --title "How to configure database?" --body "Need help with setup"
```

**Expected**: Issue #4 with "question" label

## Testing Phase 2: Duplicate Detection

### Test 5: Duplicate Issue
- [ ] Create issue similar to Test 1
- [ ] Wait for workflow to complete
- [ ] Verify issue has "bug" label
- [ ] Verify issue has "duplicate" label
- [ ] Check for bot comment mentioning original issue
- [ ] Verify similarity percentage shown in comment
- [ ] Similarity should be >80%

**Command**:
```bash
gh issue create --title "Bug: App crashes" --body "The application crashes when starting"
```

**Expected**: Issue #5 with "bug" + "duplicate" labels + comment

### Test 6: Multiple Duplicates
- [ ] Create another similar issue
- [ ] Verify duplicate detection works again
- [ ] Check comment lists all similar issues

**Command**:
```bash
gh issue create --title "Bug: Crash on launch" --body "App crashes immediately"
```

**Expected**: Issue #6 with duplicate detection

## Testing Phase 3: Workflow Monitoring

### Workflow Execution
- [ ] Go to Actions tab
- [ ] Click on "Issue Triage" workflow
- [ ] Verify all runs completed successfully
- [ ] Check execution time (should be <30 seconds)
- [ ] Review logs for any warnings

### Workflow Logs Review
- [ ] Open latest workflow run
- [ ] Expand "Run issue triage" step
- [ ] Look for success messages:
  - [ ] "Triaging issue #X..."
  - [ ] "Issue: [title]"
  - [ ] "Comparing against X existing issues..."
  - [ ] "Adding labels: ..."
  - [ ] "✅ Triage completed successfully"
- [ ] No error messages present

## Testing Phase 4: Manual Workflow Triggers

### Test Close Duplicates Workflow
- [ ] Run: `gh workflow run close-duplicates.yml`
- [ ] Wait for completion
- [ ] Check workflow logs
- [ ] Verify no errors (won't close issues yet - need 3+ days)

### Test Close Stale Workflow
- [ ] Add "pending-response" label to an issue
- [ ] Run: `gh workflow run close-stale.yml`
- [ ] Wait for completion
- [ ] Check workflow logs
- [ ] Verify no errors (won't close issues yet - need 7+ days)

## Testing Phase 5: Edge Cases

### Test 7: Empty Issue Body
- [ ] Create issue with title only (no body)
- [ ] Verify workflow handles it gracefully
- [ ] Check if labels are still applied

**Command**:
```bash
gh issue create --title "Bug: Empty body test" --body ""
```

### Test 8: Very Long Issue
- [ ] Create issue with very long description
- [ ] Verify workflow completes
- [ ] Check for any truncation issues

### Test 9: Special Characters
- [ ] Create issue with special characters in title/body
- [ ] Verify workflow handles it correctly
- [ ] Check labels applied correctly

## Testing Phase 6: Performance & Cost

### Performance Metrics
- [ ] Average workflow execution time: _____ seconds
- [ ] All workflows complete in <30 seconds
- [ ] No timeout errors
- [ ] API rate limits not exceeded

### Cost Monitoring
- [ ] Check AWS Cost Explorer
- [ ] Verify Bedrock costs are reasonable (~$0.01-0.02 per issue)
- [ ] Set up billing alerts if needed

## Testing Phase 7: Integration Verification

### GitHub API Integration
- [ ] Issues fetched correctly
- [ ] Labels applied successfully
- [ ] Comments posted correctly
- [ ] No API rate limit errors

### AWS Bedrock Integration
- [ ] AI analysis working
- [ ] Labels suggested are relevant
- [ ] Duplicate detection accurate
- [ ] Response times acceptable

## Post-Testing Verification

### Overall System Check
- [ ] All test issues have appropriate labels
- [ ] Duplicate detection working accurately
- [ ] No workflow failures
- [ ] Logs show successful execution
- [ ] No security issues or exposed credentials
- [ ] Performance is acceptable
- [ ] Costs are within budget

### Documentation Review
- [ ] README is accurate
- [ ] Setup instructions work
- [ ] Troubleshooting guide is helpful
- [ ] All commands tested and working

## Cleanup

### Remove Test Issues
- [ ] Close all test issues
- [ ] Add comment explaining they were tests
- [ ] Verify closed issues don't trigger workflows

**Command**:
```bash
for i in {1..6}; do
  gh issue close $i --comment "Closing test issue"
done
```

### Final Verification
- [ ] System ready for production use
- [ ] Team trained on how it works
- [ ] Monitoring set up
- [ ] Documentation updated with any findings

## Test Results Summary

**Date Tested**: _______________

**Tested By**: _______________

**Results**:
- Total Issues Created: _____
- Successful Label Applications: _____
- Duplicate Detections: _____
- Workflow Failures: _____
- Average Execution Time: _____ seconds
- Total Cost: $_____ 

**Status**: 
- [ ] ✅ All tests passed - Ready for production
- [ ] ⚠️ Minor issues found - Needs adjustment
- [ ] ❌ Major issues found - Needs fixes

**Notes**:
_______________________________________
_______________________________________
_______________________________________

## Next Steps

After successful testing:
- [ ] Monitor first few real issues
- [ ] Adjust thresholds if needed
- [ ] Review label accuracy
- [ ] Optimize costs if necessary
- [ ] Document any custom configurations
- [ ] Train team members
- [ ] Set up alerts and monitoring

---

**Testing Complete**: _______________

**Approved By**: _______________

**Production Deployment Date**: _______________
