import {
  BedrockRuntimeClient,
  InvokeModelCommand,
} from '@aws-sdk/client-bedrock-runtime';
import { config } from './config';
import { BedrockResponse, Issue } from './types';

export class BedrockClient {
  private client: BedrockRuntimeClient;

  constructor() {
    this.client = new BedrockRuntimeClient({
      region: config.aws.region,
      credentials: {
        accessKeyId: config.aws.accessKeyId,
        secretAccessKey: config.aws.secretAccessKey,
      },
    });
  }

  async analyzeIssue(
    issue: Issue,
    existingIssues: Issue[]
  ): Promise<BedrockResponse> {
    const prompt = this.buildAnalysisPrompt(issue, existingIssues);

    const payload = {
      anthropic_version: 'bedrock-2023-05-31',
      max_tokens: config.bedrock.maxTokens,
      temperature: config.bedrock.temperature,
      messages: [
        {
          role: 'user',
          content: prompt,
        },
      ],
    };

    const command = new InvokeModelCommand({
      modelId: config.bedrock.modelId,
      contentType: 'application/json',
      accept: 'application/json',
      body: JSON.stringify(payload),
    });

    const response = await this.client.send(command);
    const responseBody = JSON.parse(new TextDecoder().decode(response.body));

    return this.parseResponse(responseBody.content[0].text);
  }

  private buildAnalysisPrompt(issue: Issue, existingIssues: Issue[]): string {
    const issueContext = `
Title: ${issue.title}
Body: ${issue.body || 'No description provided'}
`;

    const existingIssuesContext = existingIssues
      .map(
        (i) => `
Issue #${i.number}
Title: ${i.title}
Body: ${i.body?.substring(0, 500) || 'No description'}
`
      )
      .join('\n---\n');

    return `You are a GitHub issue triage assistant. Analyze the following issue and provide:

1. Appropriate labels from this list:
   - bug, enhancement, documentation, question, help-wanted, good-first-issue
   - pending-triage, pending-response, pending-maintainer-response

2. Check if this issue is a duplicate of any existing issues (similarity threshold: ${config.thresholds.duplicateSimilarity})

NEW ISSUE:
${issueContext}

EXISTING OPEN ISSUES:
${existingIssuesContext}

Respond in JSON format:
{
  "labels": [
    {
      "label": "label-name",
      "confidence": 0.95,
      "reasoning": "why this label applies"
    }
  ],
  "duplicates": [
    {
      "issueNumber": 123,
      "title": "issue title",
      "similarity": 0.85,
      "reasoning": "why this is a duplicate"
    }
  ]
}

Only include labels with confidence > 0.7 and duplicates with similarity > ${config.thresholds.duplicateSimilarity}.`;
  }

  private parseResponse(text: string): BedrockResponse {
    try {
      // Extract JSON from markdown code blocks if present
      const jsonMatch = text.match(/```json\n([\s\S]*?)\n```/) || text.match(/\{[\s\S]*\}/);
      const jsonText = jsonMatch ? (jsonMatch[1] || jsonMatch[0]) : text;
      
      const parsed = JSON.parse(jsonText);
      return {
        labels: parsed.labels || [],
        duplicates: parsed.duplicates || [],
      };
    } catch (error) {
      console.error('Failed to parse Bedrock response:', error);
      console.error('Response text:', text);
      return { labels: [], duplicates: [] };
    }
  }
}
