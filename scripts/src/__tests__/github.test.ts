import { GitHubClient } from '../github';

describe('GitHubClient', () => {
  let client: GitHubClient;

  beforeEach(() => {
    if (process.env.GITHUB_TOKEN && process.env.GITHUB_REPOSITORY) {
      client = new GitHubClient();
    }
  });

  describe('getOpenIssues', () => {
    it('should fetch open issues', async () => {
      if (!client) {
        console.log('Skipping GitHub test - no credentials');
        return;
      }

      const issues = await client.getOpenIssues();
      expect(Array.isArray(issues)).toBe(true);
    });
  });

  describe('getIssuesWithLabel', () => {
    it('should fetch issues with specific label', async () => {
      if (!client) {
        console.log('Skipping GitHub test - no credentials');
        return;
      }

      const issues = await client.getIssuesWithLabel('bug');
      expect(Array.isArray(issues)).toBe(true);
    });
  });
});
