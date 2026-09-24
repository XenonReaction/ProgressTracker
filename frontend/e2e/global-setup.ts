import { execFileSync } from 'node:child_process';
import { join } from 'node:path';

import type { FullConfig } from '@playwright/test';

const composeFile = join(__dirname, 'docker-compose.yml');

/** How long the stack gets to build and start before the run gives up. */
const STARTUP_TIMEOUT_MS = 5 * 60_000;

function compose(...args: string[]): void {
  execFileSync('docker', ['compose', '-f', composeFile, ...args], { stdio: 'inherit' });
}

/** Polls the API through the frontend's nginx until the backend answers. */
async function waitForApi(baseURL: string): Promise<void> {
  const deadline = Date.now() + STARTUP_TIMEOUT_MS;
  while (Date.now() < deadline) {
    try {
      const response = await fetch(`${baseURL}/api/v1/trees`);
      if (response.ok) {
        return;
      }
    } catch {
      // nginx isn't listening yet
    }
    await new Promise((resolve) => setTimeout(resolve, 1000));
  }
  throw new Error(`The e2e stack didn't answer at ${baseURL} within ${STARTUP_TIMEOUT_MS / 1000}s`);
}

/**
 * Starts a fresh copy of the app in Docker and returns the teardown that removes it.
 * Set E2E_KEEP_STACK=1 to leave it running afterwards for a look around.
 */
export default async function globalSetup(config: FullConfig): Promise<() => void> {
  const baseURL = config.projects[0].use.baseURL!;
  const teardown = () => {
    if (process.env['E2E_KEEP_STACK']) {
      console.log(`E2E_KEEP_STACK is set: the stack is still running at ${baseURL}`);
      return;
    }
    compose('down', '-v', '--remove-orphans');
  };

  // Clear out anything left by an interrupted run, so every run starts from the sample data
  compose('down', '-v', '--remove-orphans');
  try {
    compose('up', '-d', '--build');
    await waitForApi(baseURL);
  } catch (error) {
    teardown();
    throw error;
  }
  return teardown;
}
