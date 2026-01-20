import { Issue, LabelPrediction, DuplicateMatch, BedrockResponse } from '../types';

describe('Types', () => {
  describe('Issue', () => {
    it('should match expected structure', () => {
      const issue: Issue = {
        number: 1,
        title: 'Test Issue',
        body: 'Test body',
        state: 'open',
        labels: [{ name: 'bug' }],
        created_at: '2024-01-01T00:00:00Z',
        updated_at: '2024-01-01T00:00:00Z',
      };

      expect(issue.number).toBe(1);
      expect(issue.title).toBe('Test Issue');
      expect(issue.body).toBe('Test body');
      expect(issue.state).toBe('open');
      expect(issue.labels).toHaveLength(1);
    });

    it('should allow null body', () => {
      const issue: Issue = {
        number: 1,
        title: 'Test Issue',
        body: null,
        state: 'open',
        labels: [],
        created_at: '2024-01-01T00:00:00Z',
        updated_at: '2024-01-01T00:00:00Z',
      };

      expect(issue.body).toBeNull();
    });
  });

  describe('LabelPrediction', () => {
    it('should match expected structure', () => {
      const prediction: LabelPrediction = {
        label: 'bug',
        confidence: 0.95,
        reasoning: 'Issue describes a crash',
      };

      expect(prediction.label).toBe('bug');
      expect(prediction.confidence).toBe(0.95);
      expect(prediction.reasoning).toBe('Issue describes a crash');
    });
  });

  describe('DuplicateMatch', () => {
    it('should match expected structure', () => {
      const match: DuplicateMatch = {
        issueNumber: 123,
        title: 'Similar Issue',
        similarity: 0.85,
        reasoning: 'Both describe crashes',
      };

      expect(match.issueNumber).toBe(123);
      expect(match.title).toBe('Similar Issue');
      expect(match.similarity).toBe(0.85);
      expect(match.reasoning).toBe('Both describe crashes');
    });
  });

  describe('BedrockResponse', () => {
    it('should match expected structure', () => {
      const response: BedrockResponse = {
        labels: [
          {
            label: 'bug',
            confidence: 0.95,
            reasoning: 'Issue describes a crash',
          },
        ],
        duplicates: [
          {
            issueNumber: 123,
            title: 'Similar Issue',
            similarity: 0.85,
            reasoning: 'Both describe crashes',
          },
        ],
      };

      expect(response.labels).toHaveLength(1);
      expect(response.duplicates).toHaveLength(1);
      expect(response.labels[0].label).toBe('bug');
      expect(response.duplicates[0].issueNumber).toBe(123);
    });

    it('should allow empty arrays', () => {
      const response: BedrockResponse = {
        labels: [],
        duplicates: [],
      };

      expect(response.labels).toHaveLength(0);
      expect(response.duplicates).toHaveLength(0);
    });
  });
});
