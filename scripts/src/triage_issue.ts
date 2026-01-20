import { GitHubClient } from './github';
import { BedrockClient } from './bedrock';
import { config, validateConfig } from './config';

async function main() {
  try {
    validateConfig();

    const issueNumber = parseInt(process.env.ISSUE_NUMBER || '0');
    if (!issueNumber) {
      throw new Error('ISSUE_NUMBER environment variable is required');
    }

    console.log(`Triaging issue #${issueNumber}...`);

    const github = new GitHubClient();
    const bedrock = new BedrockClient();

    // Get the issue to triage
    const issue = await github.getIssue(issueNumber);
    console.log(`Issue: ${issue.title}`);

    // Get existing open issues for duplicate detection
    const existingIssues = await github.getOpenIssues();
    const otherIssues = existingIssues.filter(i => i.number !== issueNumber);
    console.log(`Comparing against ${otherIssues.length} existing issues...`);

    // Analyze with Bedrock
    const analysis = await bedrock.analyzeIssue(issue, otherIssues);

    // Apply labels
    if (analysis.labels.length > 0) {
      const labelNames = analysis.labels.map(l => l.label);
      console.log(`Adding labels: ${labelNames.join(', ')}`);
      await github.addLabels(issueNumber, labelNames);
    }

    // Handle duplicates
    if (analysis.duplicates.length > 0) {
      console.log(`Found ${analysis.duplicates.length} potential duplicates`);
      
      const duplicateList = analysis.duplicates
        .map(d => `- #${d.issueNumber}: ${d.title} (similarity: ${(d.similarity * 100).toFixed(0)}%)`)
        .join('\n');

      const comment = `🤖 **Potential Duplicate Detected**

This issue appears to be similar to:

${duplicateList}

If this is indeed a duplicate, it will be automatically closed in ${config.thresholds.duplicateCloseDays} days unless the \`duplicate\` label is removed.

${analysis.duplicates[0].reasoning}`;

      await github.addComment(issueNumber, comment);
      await github.addLabels(issueNumber, ['duplicate']);
      
      // Remove pending-triage and bug labels when marked as duplicate
      console.log('Removing pending-triage and bug labels from duplicate issue...');
      await github.removeLabel(issueNumber, 'pending-triage');
      await github.removeLabel(issueNumber, 'bug');
    }

    console.log('✅ Triage completed successfully');
  } catch (error) {
    console.error('❌ Error during triage:', error);
    process.exit(1);
  }
}

main();
