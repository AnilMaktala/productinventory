# Quick Test Guide - 5 Minutes

## Fastest Way to Test GitHub Issue Automation

### Prerequisites (One-Time Setup)

1. **AWS Bedrock** - Enable and get credentials
2. **GitHub Secrets** - Add AWS credentials
3. **Labels** - Create required labels
4. **Permissions** - Enable workflow write access

### Quick Test (5 Minutes)

#### Step 1: Add Secrets (2 min)

```bash
gh secret set AWS_ACCESS_KEY_ID
gh secret set AWS_SECRET_ACCESS_KEY
```

#### Step 2: Create Labels (1 min)

```bash
gh label create "bug" --color "d73a4a" --description "Something isn't working"
gh label create "enhancement" --color "a2eeef" --description "New feature"
gh label create "duplicate" --color "cfd3d7" --description "Duplicate issue"
gh label create "question" --color "d876e3" --description "Question"
gh label create "documentation" --color "0075ca" --description "Documentation"
```

#### Step 3: Create Test Issue (30 sec)

```bash
gh issue create \
  --title "Bug: Application crashes on startup" \
  --body "The app crashes immediately when I start it. Error code 500."
```

#### Step 4: Check Results (1 min)

```bash
# View workflow run
gh run list --workflow=issue-triage.yml --limit 1

# View issue (should have labels)
gh issue view 1
```

### Expected Results

✅ Workflow runs automatically  
✅ Issue gets "bug" label  
✅ Workflow completes in ~10-20 seconds  
✅ No errors in logs

### Quick Troubleshooting

**No labels applied?**
```bash
# Check workflow logs
gh run view <run-id> --log | grep -i error
```

**Workflow didn't run?**
```bash
# Check if workflows are enabled
gh workflow list
```

**AWS error?**
```bash
# Verify secrets
gh secret list
```

### Test Scenarios

#### 1. Bug Report
```bash
gh issue create --title "Bug: Crash on startup" --body "App crashes with error 500"
```
Expected: `bug` label

#### 2. Feature Request
```bash
gh issue create --title "Feature: Add dark mode" --body "Please add dark mode support"
```
Expected: `enhancement` label

#### 3. Question
```bash
gh issue create --title "How to configure database?" --body "Need help with DB setup"
```
Expected: `question` label

#### 4. Duplicate (create after #1)
```bash
gh issue create --title "Bug: App crashes" --body "Application crashes on startup"
```
Expected: `bug` + `duplicate` labels + comment

### Monitor Workflow

```bash
# Watch in real-time
gh run watch

# View logs
gh run view <run-id> --log

# Check status
gh run list --workflow=issue-triage.yml
```

### Verify Success

```bash
# Check issue has labels
gh issue view <issue-number>

# Should show:
# Labels: bug (or enhancement, question, etc.)
# If duplicate: comment with similar issues
```

### Cleanup

```bash
# Close test issues
gh issue close 1 --comment "Test complete"
```

---

**Total Time**: 5 minutes  
**Success Rate**: High (if prerequisites met)  
**Next**: See `TESTING_IN_GITHUB.md` for detailed guide
