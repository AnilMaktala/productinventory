import { GitHubClient } from './github';
import { config, validateConfig } from './config';

async function main() {
  try {
    validateConfig();

    console.log('Checking for stale issues to close...');

    const github = new GitHubClient();

    // Get all open issues with pending-response label
    const staleIssues = await github.getIssuesWithLabel('pending-response');
    console.log(`Found ${staleIssues.length} issues with pending-response label`);

    let closedCount = 0;
    const now = new Date();

    for (const issue of staleIssues) {
      try {
        // Get comments and timeline
        const comments = await github.getIssueComments(issue.number);
        const timeline = await github.getIssueTimeline(issue.number);

        // Find last activity (comment or label change)
        const lastComment = comments.length > 0 
          ? new Date(comments[comments.length - 1].created_at)
          : null;

        const labelEvents = timeline
          .filter((event: any) => event.event === 'labeled' || event.event === 'unlabeled')
          .map((event: any) => new Date(event.created_at));

        const lastLabelChange = labelEvents.length > 0
          ? new Date(Math.max(...labelEvents.map(d => d.getTime())))
          : null;

        const lastActivity = [lastComment, lastLabelChange, new Date(issue.updated_at)]
          .filter(d => d !== null)
          .reduce((latest, current) => 
            current && current > latest ? current : latest
          );

        const daysSinceActivity = (now.getTime() - lastActivity.getTime()) / (1000 * 60 * 60 * 24);

        console.log(`Issue #${issue.number}: Last activity ${daysSinceActivity.toFixed(1)} days ago`);

        if (daysSinceActivity >= config.thresholds.staleIssueDays) {
          console.log(`Closing stale issue #${issue.number}...`);

          const comment = `🤖 **Closing Due to Inactivity**

This issue has been marked as pending response with no activity for ${config.thresholds.staleIssueDays}+ days and is now being automatically closed.

If you still need help with this issue, please comment and we'll be happy to reopen it.`;

          await github.addComment(issue.number, comment);
          await github.closeIssue(issue.number);
          closedCount++;
        }
      } catch (error) {
        console.error(`Error processing issue #${issue.number}:`, error);
      }
    }

    console.log(`✅ Closed ${closedCount} stale issues`);
  } catch (error) {
    console.error('❌ Error closing stale issues:', error);
    process.exit(1);
  }
}

main();
