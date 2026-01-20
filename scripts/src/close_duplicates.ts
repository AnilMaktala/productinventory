import { GitHubClient } from './github';
import { config, validateConfig } from './config';

async function main() {
  try {
    validateConfig();

    console.log('Checking for duplicate issues to close...');

    const github = new GitHubClient();

    // Get all open issues with duplicate label
    const duplicateIssues = await github.getIssuesWithLabel('duplicate');
    console.log(`Found ${duplicateIssues.length} issues with duplicate label`);

    let closedCount = 0;
    const now = new Date();

    for (const issue of duplicateIssues) {
      try {
        // Get timeline to find when duplicate label was added
        const timeline = await github.getIssueTimeline(issue.number);
        
        const labelEvent = timeline
          .filter((event: any) => event.event === 'labeled')
          .find((event: any) => event.label?.name === 'duplicate');

        if (!labelEvent) {
          console.log(`Issue #${issue.number}: No label event found, skipping`);
          continue;
        }

        const labeledDate = new Date(labelEvent.created_at);
        const daysSinceLabeled = (now.getTime() - labeledDate.getTime()) / (1000 * 60 * 60 * 24);

        console.log(`Issue #${issue.number}: Labeled ${daysSinceLabeled.toFixed(1)} days ago`);

        if (daysSinceLabeled >= config.thresholds.duplicateCloseDays) {
          console.log(`Closing issue #${issue.number}...`);

          const comment = `🤖 **Closing as Duplicate**

This issue has been marked as a duplicate for ${config.thresholds.duplicateCloseDays}+ days and is now being automatically closed.

If you believe this was closed in error, please comment and a maintainer will review.`;

          await github.addComment(issue.number, comment);
          await github.closeIssue(issue.number);
          closedCount++;
        }
      } catch (error) {
        console.error(`Error processing issue #${issue.number}:`, error);
      }
    }

    console.log(`✅ Closed ${closedCount} duplicate issues`);
  } catch (error) {
    console.error('❌ Error closing duplicates:', error);
    process.exit(1);
  }
}

main();
