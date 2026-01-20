# Changelog Update

## [1.0.1] - 2026-01-20

### Changed

#### Issue Triage Workflow Enhancement

**Feature**: Automatic label cleanup for duplicate issues

When an issue is identified as a duplicate, the triage workflow now automatically removes the following labels:
- `pending-triage`
- `bug`

**Rationale**: 
- Duplicate issues don't need triage since they reference an existing issue
- Prevents confusion by removing the `bug` label from duplicates
- Keeps issue labels clean and accurate
- Reduces manual label management

**Behavior**:
1. Issue is analyzed by AI
2. If duplicates detected:
   - `duplicate` label is added
   - Comment posted with similar issues
   - `pending-triage` label removed (if present)
   - `bug` label removed (if present)
3. Other labels (enhancement, documentation, question) remain unchanged

**Example**:

Before:
```
Issue #5: "Bug: App crashes"
Labels: bug, pending-triage
```

After duplicate detection:
```
Issue #5: "Bug: App crashes"
Labels: duplicate
Comment: "🤖 Potential Duplicate Detected - Similar to #1"
```

**Files Modified**:
- `scripts/src/triage_issue.ts` - Added label removal logic
- `scripts/dist/triage_issue.js` - Compiled output updated

**Testing**:
- ✅ TypeScript compilation successful
- ✅ All unit tests passing (20/20)
- ✅ No breaking changes
- ✅ Backward compatible

**Impact**:
- Low risk change
- Improves label accuracy
- Reduces manual cleanup
- No configuration changes needed

---

## Implementation Details

### Code Changes

**Location**: `scripts/src/triage_issue.ts`

**Added**:
```typescript
// Remove pending-triage and bug labels when marked as duplicate
console.log('Removing pending-triage and bug labels from duplicate issue...');
await github.removeLabel(issueNumber, 'pending-triage');
await github.removeLabel(issueNumber, 'bug');
```

**Why these labels?**
- `pending-triage`: Duplicates don't need triage
- `bug`: Duplicate might not be a bug (could be duplicate of feature request, etc.)

**Why not remove other labels?**
- `enhancement`, `documentation`, `question` - May still be relevant for categorization
- Other workflow labels - May be needed for tracking

### Error Handling

The `removeLabel` method in `github.ts` already handles cases where:
- Label doesn't exist on the issue (gracefully ignores)
- Label was never applied (no error thrown)

### Workflow Execution

```
1. Issue created
2. Workflow triggered
3. AI analyzes issue
4. Labels applied (bug, enhancement, etc.)
5. Duplicate detection runs
6. If duplicate found:
   a. Add duplicate label
   b. Post comment
   c. Remove pending-triage label ← NEW
   d. Remove bug label ← NEW
7. Workflow completes
```

### Testing Recommendations

When testing this change:

1. **Test Case 1**: Duplicate bug report
   ```bash
   # Create original
   gh issue create --title "Bug: Crash" --body "App crashes"
   
   # Create duplicate
   gh issue create --title "Bug: App crash" --body "Application crashes"
   ```
   **Expected**: Second issue has only `duplicate` label

2. **Test Case 2**: Duplicate feature request
   ```bash
   # Create original
   gh issue create --title "Feature: Dark mode" --body "Add dark mode"
   
   # Create duplicate
   gh issue create --title "Feature: Dark theme" --body "Add dark theme"
   ```
   **Expected**: Second issue has `enhancement` + `duplicate` labels (bug not added)

3. **Test Case 3**: Non-duplicate issue
   ```bash
   gh issue create --title "Bug: New issue" --body "Different problem"
   ```
   **Expected**: Issue has `bug` label, no duplicate label

### Migration Notes

**For existing repositories**:
- No migration needed
- Change takes effect immediately after deployment
- Existing duplicate issues are not affected
- Only new duplicates will have labels removed

**For new repositories**:
- Works out of the box
- No additional configuration required

### Rollback Plan

If needed, revert by removing these lines from `scripts/src/triage_issue.ts`:

```typescript
// Remove these 3 lines:
console.log('Removing pending-triage and bug labels from duplicate issue...');
await github.removeLabel(issueNumber, 'pending-triage');
await github.removeLabel(issueNumber, 'bug');
```

Then rebuild:
```bash
cd scripts
npm run build
```

---

**Version**: 1.0.1  
**Date**: January 20, 2026  
**Type**: Enhancement  
**Breaking Changes**: None  
**Migration Required**: No
