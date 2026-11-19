#!/usr/bin/env node

/**
 * Single Unified Startup Script for Haryana Roadways Real-Time Transport System
 * 
 * Automatically starts all 3 project components simultaneously:
 * 1. [BACKEND] Spring Boot REST & WebSocket Server (Port 8081)
 * 2. [AI/ML]   Python/Flask ETA Prediction Microservice (Port 5000)
 * 3. [MOBILE]  Expo Metro Bundler with Auto-Cleared Cache (Port 8082)
 * 
 * Clean exit: Pressing Ctrl+C terminates all services cleanly without leaving zombie ports.
 */

const { spawn, execSync } = require('child_process');
const path = require('path');
const fs = require('fs');
const os = require('os');

// ANSI Color formatting
const colors = {
  reset: '\x1b[0m',
  bold: '\x1b[1m',
  cyan: '\x1b[36m',
  magenta: '\x1b[35m',
  green: '\x1b[32m',
  yellow: '\x1b[33m',
  red: '\x1b[31m',
  blue: '\x1b[34m',
};

function log(prefix, color, message) {
  const lines = message.toString().split('\n');
  lines.forEach((line) => {
    if (line.trim().length > 0) {
      console.log(`${color}${colors.bold}[${prefix}]${colors.reset} ${line}`);
    }
  });
}

const isWindows = process.platform === 'win32';

// 1. Locate root directories
const scriptDir = __dirname;
const mobileDir = path.resolve(scriptDir, '..');

function findDir(candidatePaths) {
  for (const p of candidatePaths) {
    if (fs.existsSync(p)) return p;
  }
  return null;
}

const backendDir = findDir([
  path.resolve(mobileDir, '../../backend'),
  path.resolve(mobileDir, '../backend'),
  path.resolve(mobileDir, '../backend-spring-boot'),
  path.resolve(process.env.HOME || '', 'Desktop/real-time-public-transport/backend'),
  path.resolve(process.env.HOME || '', 'Documents/antigravity/blissful-bardeen/backend-spring-boot'),
]);

const mlDir = findDir([
  path.resolve(mobileDir, '../../ml'),
  path.resolve(mobileDir, '../ml'),
  path.resolve(process.env.HOME || '', 'Desktop/real-time-public-transport/ml'),
  path.resolve(process.env.HOME || '', 'Documents/antigravity/blissful-bardeen/ml'),
]);

console.log(`${colors.bold}${colors.blue}================================================================${colors.reset}`);
console.log(`${colors.bold}${colors.green}  HARYANA ROADWAYS — UNIFIED SYSTEM RUNNER${colors.reset}`);
console.log(`${colors.bold}${colors.blue}================================================================${colors.reset}`);
console.log(`  ${colors.bold}Mobile Dir:${colors.reset}  ${mobileDir}`);
console.log(`  ${colors.bold}Backend Dir:${colors.reset} ${backendDir || 'NOT FOUND'}`);
console.log(`  ${colors.bold}AI/ML Dir:${colors.reset}   ${mlDir || 'NOT FOUND'}`);
console.log(`${colors.bold}${colors.blue}----------------------------------------------------------------${colors.reset}\n`);

// 2. Kill any stale processes on 8081, 8082, 5000 (except macOS ControlCenter)
function freePort(port) {
  try {
    if (!isWindows) {
      const output = execSync(`lsof -ti :${port} 2>/dev/null`, { encoding: 'utf8' }).trim();
      if (output) {
        const pids = output.split('\n');
        for (const pid of pids) {
          try {
            // Check process name
            const procName = execSync(`ps -p ${pid} -o comm= 2>/dev/null`, { encoding: 'utf8' }).trim();
            if (procName.includes('ControlCenter')) {
              // Skip macOS AirPlay ControlCenter
              continue;
            }
            process.kill(parseInt(pid, 10), 'SIGTERM');
            log('SYSTEM', colors.yellow, `Cleaned up stale process ${pid} (${procName}) on port ${port}`);
          } catch (_) {}
        }
      }
    }
  } catch (_) {}
}

freePort(8081);
freePort(8082);

const children = [];

function killAllChildren() {
  log('SYSTEM', colors.yellow, 'Shutting down all background services...');
  for (const child of children) {
    if (child && child.pid && !child.killed) {
      try {
        if (isWindows) {
          execSync(`taskkill /pid ${child.pid} /T /F 2>nul`);
        } else {
          process.kill(-child.pid, 'SIGINT');
        }
      } catch (_) {
        try {
          child.kill('SIGTERM');
        } catch (__) {}
      }
    }
  }
}

process.on('SIGINT', () => {
  killAllChildren();
  process.exit(0);
});

process.on('SIGTERM', () => {
  killAllChildren();
  process.exit(0);
});

// 3. Start Spring Boot Backend
if (backendDir && (fs.existsSync(path.join(backendDir, 'pom.xml')) || fs.existsSync(path.join(backendDir, 'mvnw')))) {
  log('SYSTEM', colors.cyan, 'Starting Spring Boot REST & WebSocket Server (Port 8081)...');
  const mvnCmd = isWindows ? 'mvnw.cmd' : './mvnw';
  const backendProc = spawn(isWindows ? mvnCmd : 'bash', isWindows ? ['spring-boot:run'] : ['./mvnw', 'spring-boot:run'], {
    cwd: backendDir,
    detached: !isWindows,
    shell: true,
    env: { ...process.env, SERVER_PORT: '8081' },
  });

  backendProc.stdout.on('data', (d) => log('BACKEND', colors.cyan, d));
  backendProc.stderr.on('data', (d) => log('BACKEND', colors.cyan, d));
  children.push(backendProc);
} else {
  log('BACKEND', colors.yellow, 'Backend directory not found; skipping Spring Boot.');
}

// 4. Start AI/ML Microservice (Flask)
if (mlDir && fs.existsSync(path.join(mlDir, 'app.py'))) {
  log('SYSTEM', colors.magenta, 'Starting Python/Flask AI/ML Microservice (Port 5000)...');
  let pyPath = 'python3';
  const venvPy = isWindows
    ? path.join(mlDir, 'venv', 'Scripts', 'python.exe')
    : path.join(mlDir, 'venv', 'bin', 'python3');

  if (fs.existsSync(venvPy)) {
    pyPath = venvPy;
  }

  const mlProc = spawn(pyPath, ['app.py'], {
    cwd: mlDir,
    detached: !isWindows,
    shell: true,
    env: { ...process.env, PYTHONUNBUFFERED: '1', PORT: '5000' },
  });

  mlProc.stdout.on('data', (d) => log('AI/ML', colors.magenta, d));
  mlProc.stderr.on('data', (d) => log('AI/ML', colors.magenta, d));
  children.push(mlProc);
} else {
  log('AI/ML', colors.yellow, 'ML directory not found; skipping Flask ETA microservice.');
}

// 5. Increase macOS watch descriptor limit and Start Expo Metro Bundler
log('SYSTEM', colors.green, 'Starting Expo Mobile Metro Bundler (Port 8082, Cache Cleared)...');

// Run ulimit if unix
if (!isWindows) {
  try {
    execSync('ulimit -n 10240 2>/dev/null');
  } catch (_) {}
}

// Pass -c to automatically clear cache and prevent SDK 51 mismatch glitches!
const expoArgs = ['start', '-c', '--port', '8082'];

const mobileProc = spawn('npx', ['expo', ...expoArgs], {
  cwd: mobileDir,
  detached: !isWindows,
  shell: true,
  stdio: 'inherit',
  env: { ...process.env, EXPO_NO_TELEMETRY: '1' },
});

children.push(mobileProc);

mobileProc.on('exit', (code) => {
  log('MOBILE', colors.green, `Expo Metro exited with code ${code}`);
  killAllChildren();
  process.exit(code || 0);
});
