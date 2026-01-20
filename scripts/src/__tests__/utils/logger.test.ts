import { logger, LogLevel } from '../../utils/logger';

describe('Logger', () => {
  let consoleDebugSpy: jest.SpyInstance;
  let consoleInfoSpy: jest.SpyInstance;
  let consoleWarnSpy: jest.SpyInstance;
  let consoleErrorSpy: jest.SpyInstance;

  beforeEach(() => {
    consoleDebugSpy = jest.spyOn(console, 'debug').mockImplementation();
    consoleInfoSpy = jest.spyOn(console, 'info').mockImplementation();
    consoleWarnSpy = jest.spyOn(console, 'warn').mockImplementation();
    consoleErrorSpy = jest.spyOn(console, 'error').mockImplementation();
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  it('should log at appropriate levels', () => {
    logger.setLevel(LogLevel.DEBUG);

    logger.debug('debug message');
    logger.info('info message');
    logger.warn('warn message');
    logger.error('error message');

    expect(consoleDebugSpy).toHaveBeenCalledWith('[DEBUG] debug message');
    expect(consoleInfoSpy).toHaveBeenCalledWith('[INFO] info message');
    expect(consoleWarnSpy).toHaveBeenCalledWith('[WARN] warn message');
    expect(consoleErrorSpy).toHaveBeenCalledWith('[ERROR] error message');
  });

  it('should respect log level', () => {
    logger.setLevel(LogLevel.WARN);

    logger.debug('debug message');
    logger.info('info message');
    logger.warn('warn message');
    logger.error('error message');

    expect(consoleDebugSpy).not.toHaveBeenCalled();
    expect(consoleInfoSpy).not.toHaveBeenCalled();
    expect(consoleWarnSpy).toHaveBeenCalled();
    expect(consoleErrorSpy).toHaveBeenCalled();
  });
});
