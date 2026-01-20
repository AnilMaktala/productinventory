import { RateLimiter } from '../../utils/rate-limiter';

describe('RateLimiter', () => {
  it('should limit rate of function calls', async () => {
    const limiter = new RateLimiter(2); // 2 calls per second
    const calls: number[] = [];

    const promises = [1, 2, 3, 4].map(i =>
      limiter.execute(async () => {
        calls.push(Date.now());
        return i;
      })
    );

    const results = await Promise.all(promises);

    expect(results).toEqual([1, 2, 3, 4]);
    expect(calls.length).toBe(4);

    // Check that calls are spaced out
    for (let i = 1; i < calls.length; i++) {
      const timeDiff = calls[i] - calls[i - 1];
      expect(timeDiff).toBeGreaterThanOrEqual(400); // ~500ms between calls
    }
  });

  it('should handle errors in rate-limited functions', async () => {
    const limiter = new RateLimiter(1);

    await expect(
      limiter.execute(async () => {
        throw new Error('Test error');
      })
    ).rejects.toThrow('Test error');
  });
});
