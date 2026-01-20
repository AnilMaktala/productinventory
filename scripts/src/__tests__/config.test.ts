import { config, validateConfig } from '../config';

describe('Config', () => {
  const originalEnv = process.env;

  beforeEach(() => {
    jest.resetModules();
    process.env = { ...originalEnv };
  });

  afterAll(() => {
    process.env = originalEnv;
  });

  describe('config object', () => {
    it('should have required properties', () => {
      expect(config).toHaveProperty('github');
      expect(config).toHaveProperty('aws');
      expect(config).toHaveProperty('bedrock');
      expect(config).toHaveProperty('thresholds');
    });

    it('should have correct default values', () => {
      expect(config.bedrock.modelId).toBe('us.anthropic.claude-sonnet-4-20250514-v1:0');
      expect(config.bedrock.maxTokens).toBe(4096);
      expect(config.bedrock.temperature).toBe(0.3);
      expect(config.thresholds.duplicateSimilarity).toBe(0.8);
      expect(config.thresholds.duplicateCloseDays).toBe(3);
      expect(config.thresholds.staleIssueDays).toBe(7);
    });
  });

  describe('validateConfig', () => {
    it('should not throw when required env vars are set', () => {
      process.env.GITHUB_TOKEN = 'test_token';
      process.env.GITHUB_REPOSITORY = 'owner/repo';

      expect(() => validateConfig()).not.toThrow();
    });

    it('should throw when GITHUB_TOKEN is missing', () => {
      delete process.env.GITHUB_TOKEN;
      process.env.GITHUB_REPOSITORY = 'owner/repo';

      expect(() => validateConfig()).toThrow('Missing required environment variables');
    });

    it('should throw when GITHUB_REPOSITORY is missing', () => {
      process.env.GITHUB_TOKEN = 'test_token';
      delete process.env.GITHUB_REPOSITORY;

      expect(() => validateConfig()).toThrow('Missing required environment variables');
    });
  });
});
