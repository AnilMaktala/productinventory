import { BedrockClient } from '../bedrock';
import { Issue } from '../types';

describe('BedrockClient', () => {
  let client: BedrockClient;

  beforeEach(() => {
    client = new BedrockClient();
  });

  describe('analyzeIssue', () => {
    it('should analyze issue and return labels and duplicates', async () => {
      const issue: Issue = {
        number: 1,
        title: 'Bug: Application crashes',
        body: 'The application crashes when I click the button',
        state: 'open',
        labels: [],
        created_at: '2024-01-01T00:00:00Z',
        updated_at: '2024-01-01T00:00:00Z',
      };

      const existingIssues: Issue[] = [];

      // This test requires AWS credentials and will be skipped in CI
      if (!process.env.AWS_ACCESS_KEY_ID) {
        console.log('Skipping Bedrock test - no AWS credentials');
        return;
      }

      const result = await client.analyzeIssue(issue, existingIssues);

      expect(result).toHaveProperty('labels');
      expect(result).toHaveProperty('duplicates');
      expect(Array.isArray(result.labels)).toBe(true);
      expect(Array.isArray(result.duplicates)).toBe(true);
    });
  });
});
