import * as dotenv from 'dotenv';

dotenv.config();

export const config = {
  github: {
    token: process.env.GITHUB_TOKEN || '',
    repository: process.env.GITHUB_REPOSITORY || '',
  },
  aws: {
    accessKeyId: process.env.AWS_ACCESS_KEY_ID || '',
    secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY || '',
    region: process.env.AWS_REGION || 'us-east-1',
  },
  bedrock: {
    modelId: 'us.anthropic.claude-sonnet-4-20250514-v1:0',
    maxTokens: 4096,
    temperature: 0.3,
  },
  thresholds: {
    duplicateSimilarity: 0.8,
    duplicateCloseDays: 3,
    staleIssueDays: 7,
  },
};

export function validateConfig(): void {
  const required = [
    'GITHUB_TOKEN',
    'GITHUB_REPOSITORY',
  ];

  const missing = required.filter(key => !process.env[key]);
  
  if (missing.length > 0) {
    throw new Error(`Missing required environment variables: ${missing.join(', ')}`);
  }
}
