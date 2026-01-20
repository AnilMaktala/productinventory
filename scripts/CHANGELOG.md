# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-01-20

### Added

#### Core Features
- Automatic issue triage using AWS Bedrock Claude Sonnet 4
- AI-powered label assignment based on issue content
- Semantic duplicate detection with similarity scoring
- Automatic closure of duplicate issues after 3 days
- Automatic closure of stale issues after 7 days
- Comprehensive error handling and retry logic

#### GitHub Actions Workflows
- `issue-triage.yml` - Triggered on new issue creation
- `close-duplicates.yml` - Daily scheduled workflow
- `close-stale.yml` - Daily scheduled workflow

#### TypeScript Scripts
- `triage_issue.ts` - Main issue triage logic
- `close_duplicates.ts` - Duplicate issue closure
- `close_stale.ts` - Stale issue closure
- `bedrock.ts` - AWS Bedrock client implementation
- `github.ts` - GitHub API client implementation
- `config.ts` - Configuration management
- `types.ts` - TypeScript type definitions

#### Utilities
- `rate-limiter.ts` - API rate limiting utility
- `retry.ts` - Retry logic with exponential backoff
- `logger.ts` - Logging utility with log levels

#### Testing
- Unit tests for all core modules
- Integration tests for API clients
- Test utilities and fixtures
- Jest configuration with coverage reporting
- Mock implementations for testing

#### Documentation
- Comprehensive README with setup instructions
- Quick start guide (10-minute setup)
- Detailed setup checklist
- Security guidelines and best practices
- Testing guide with examples
- Contributing guidelines
- PR description template
- Workflow documentation

#### Configuration
- TypeScript configuration with strict mode
- ESLint configuration for code quality
- Jest configuration for testing
- Environment variable templates
- Package.json with all dependencies

### Configuration

#### Default Thresholds
- Duplicate similarity: 0.8 (80% match required)
- Duplicate close delay: 3 days
- Stale issue delay: 7 days
- Rate limits: Configurable per API

#### Supported Labels
- `bug` - Something isn't working
- `enhancement` - New feature or request
- `documentation` - Documentation improvements
- `question` - Further information requested
- `duplicate` - Duplicate issue
- `pending-triage` - Awaiting maintainer review
- `pending-response` - Awaiting user response
- `pending-maintainer-response` - Awaiting maintainer response
- `help-wanted` - Extra attention needed
- `good-first-issue` - Good for newcomers

### Dependencies

#### Production
- `@aws-sdk/client-bedrock-runtime` ^3.700.0
- `@octokit/rest` ^21.0.2
- `dotenv` ^16.4.7

#### Development
- `@types/jest` ^29.5.14
- `@types/node` ^22.10.2
- `@typescript-eslint/eslint-plugin` ^8.18.2
- `@typescript-eslint/parser` ^8.18.2
- `eslint` ^9.17.0
- `jest` ^29.7.0
- `ts-jest` ^29.2.5
- `typescript` ^5.7.2

### Security

#### Implemented Protections
- Input sanitization for issue content
- Output validation for AI responses
- Prompt injection prevention
- Rate limiting for API calls
- Secure credential management
- Audit logging for all operations

#### Best Practices
- Minimal IAM permissions
- GitHub token scope limiting
- Secret rotation guidelines
- Vulnerability scanning setup
- Security incident response plan

### Performance

#### Optimizations
- Rate limiting to prevent API throttling
- Exponential backoff for retries
- Efficient batch processing
- Caching where appropriate
- Minimal API calls

#### Metrics
- Average triage time: ~2-3 seconds per issue
- Duplicate detection: ~5-10 seconds for 100 issues
- API rate limit compliance: 100%

### Known Limitations

1. **AWS Bedrock Access**: Requires Claude Sonnet 4 model access
2. **GitHub API**: Subject to rate limits (5,000 requests/hour)
3. **Language Support**: Currently optimized for English issues
4. **Duplicate Detection**: May have false positives/negatives
5. **Cost**: AWS Bedrock charges apply (~$0.01-0.02 per issue)

### Breaking Changes

None - Initial release

### Deprecated

None - Initial release

### Removed

None - Initial release

### Fixed

None - Initial release

### Migration Guide

Not applicable - Initial release

## [Unreleased]

### Planned Features
- Multi-language support for issue analysis
- Custom label taxonomy configuration
- Webhook support for real-time processing
- Dashboard for monitoring automation metrics
- Advanced duplicate detection algorithms
- Integration with project management tools
- Customizable AI prompts
- Batch processing improvements

### Under Consideration
- Support for other AI models (GPT-4, etc.)
- Issue priority assignment
- Automatic issue assignment to team members
- Integration with CI/CD pipelines
- Custom workflow triggers
- Advanced analytics and reporting

---

## Version History

- **1.0.0** (2024-01-20) - Initial release

## Links

- [GitHub Repository](https://github.com/your-org/your-repo)
- [Documentation](./README.md)
- [Security Policy](./SECURITY.md)
- [Contributing Guide](./CONTRIBUTING.md)

## Support

For questions or issues:
- Check the [documentation](./README.md)
- Review [troubleshooting guide](./TESTING.md)
- Open an issue on GitHub
