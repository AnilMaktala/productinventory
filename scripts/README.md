# GitHub Issue Automation Scripts

TypeScript scripts for automated GitHub issue management using AWS Bedrock AI.

## Features

- **Automatic Issue Triage**: AI-powered label assignment and duplicate detection
- **Duplicate Closure**: Automatically close confirmed duplicates after 3 days
- **Stale Issue Management**: Close inactive issues after 7 days

## Prerequisites

- Node.js 20+
- AWS Account with Bedrock access
- GitHub repository with appropriate permissions

## Setup

1. Install dependencies:
```bash
npm install
```

2. Build TypeScript:
```bash
npm run build
```

3. Configure environment variables (copy `.env.example` to `.env`):
```bash
cp .env.example .env
```

Edit `.env` with your credentials:
- `GITHUB_TOKEN`: GitHub personal access token with repo permissions
- `GITHUB_REPOSITORY`: Repository in format `owner/repo`
- `AWS_ACCESS_KEY_ID`: AWS access key
- `AWS_SECRET_ACCESS_KEY`: AWS secret key
- `AWS_REGION`: AWS region (default: us-east-1)

## Usage

### Triage a Single Issue

```bash
ISSUE_NUMBER=123 npm run build && node dist/triage_issue.js
```

### Close Duplicate Issues

```bash
npm run build && node dist/close_duplicates.js
```

### Close Stale Issues

```bash
npm run build && node dist/close_stale.js
```

## Configuration

Edit thresholds in `src/config.ts`:

- `duplicateSimilarity`: Minimum similarity score for duplicate detection (default: 0.8)
- `duplicateCloseDays`: Days before closing duplicate issues (default: 3)
- `staleIssueDays`: Days of inactivity before closing (default: 7)

## Development

### Linting

```bash
npm run lint
```

### Testing

```bash
npm test
```

## Project Structure

```
scripts/
├── src/
│   ├── bedrock.ts          # AWS Bedrock client
│   ├── github.ts           # GitHub API client
│   ├── config.ts           # Configuration
│   ├── types.ts            # TypeScript types
│   ├── triage_issue.ts     # Issue triage script
│   ├── close_duplicates.ts # Duplicate closure script
│   └── close_stale.ts      # Stale issue closure script
├── dist/                   # Compiled JavaScript (generated)
├── package.json
├── tsconfig.json
└── .eslintrc.json
```

## How It Works

### Issue Triage

1. When a new issue is opened, the workflow triggers
2. The script fetches the issue and all existing open issues
3. AWS Bedrock analyzes the issue content
4. Labels are automatically applied based on AI classification
5. Duplicate detection compares against existing issues
6. If duplicates found, a comment is posted and duplicate label added

### Duplicate Closure

1. Runs daily via scheduled workflow
2. Finds all issues with "duplicate" label
3. Checks when the label was applied
4. Closes issues labeled for 3+ days
5. Posts a closing comment

### Stale Issue Closure

1. Runs daily via scheduled workflow
2. Finds all issues with "pending-response" label
3. Checks last activity (comments or label changes)
4. Closes issues with no activity for 7+ days
5. Posts a closing comment

## Troubleshooting

### AWS Authentication Errors

Ensure your AWS credentials have Bedrock permissions:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["bedrock:InvokeModel"],
      "Resource": "arn:aws:bedrock:*::foundation-model/anthropic.claude-sonnet-4-*"
    }
  ]
}
```

### GitHub API Rate Limits

The scripts include basic rate limit handling. For high-volume repositories, consider:
- Using GitHub Apps instead of personal access tokens
- Implementing more sophisticated rate limiting
- Reducing batch sizes

## License

MIT
