import { defineConfig, devices } from '@playwright/test';

/**
 * Browser tests (`npm run e2e`). They run against a throwaway copy of the app in Docker
 * (e2e/docker-compose.yml), started and removed by e2e/global-setup.ts.
 */
export default defineConfig({
  testDir: './e2e',
  globalSetup: './e2e/global-setup.ts',
  // Each test creates its own uniquely named data, so tests can run side by side
  fullyParallel: true,
  forbidOnly: !!process.env['CI'],
  retries: 0,
  reporter: [['list'], ['html', { open: 'never' }]],
  use: {
    baseURL: `http://localhost:${process.env['E2E_PORT'] ?? 8090}`,
    // A step-by-step replay of any failing test, viewable with `npx playwright show-trace`
    trace: 'retain-on-failure',
    screenshot: 'only-on-failure',
  },
  projects: [{ name: 'chromium', use: { ...devices['Desktop Chrome'] } }],
});
