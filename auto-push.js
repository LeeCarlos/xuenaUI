const { execSync } = require('child_process');
const fs = require('fs');
const path = require('path');

const REPO_PATH = __dirname;
const LOG_FILE = path.join(__dirname, 'auto-push.log');
const INTERVAL_MINUTES = 30;
const INTERVAL_MS = INTERVAL_MINUTES * 60 * 1000;
const GIT_PATH = '"E:\\工作文件\\白皙工作文件\\2025年\\Git\\cmd\\git.exe"';

function log(message) {
    const timestamp = new Date().toISOString();
    const logLine = `[${timestamp}] ${message}\n`;
    console.log(logLine.trim());
    fs.appendFileSync(LOG_FILE, logLine, 'utf8');
}

function executeCommand(cmd, options = {}) {
    try {
        const result = execSync(cmd, { ...options, cwd: REPO_PATH, encoding: 'utf8' });
        return { success: true, output: result.trim() };
    } catch (error) {
        return { success: false, output: error.stdout ? error.stdout.trim() : '', error: error.stderr ? error.stderr.trim() : error.message };
    }
}

function hasChanges() {
    const status = executeCommand(`${GIT_PATH} status --porcelain`);
    return status.success && status.output.length > 0;
}

function hasUnpushedCommits() {
    const remoteCheck = executeCommand(`${GIT_PATH} ls-remote --get-url origin`);
    if (!remoteCheck.success) {
        return false;
    }
    
    const fetchResult = executeCommand(`${GIT_PATH} fetch origin`);
    if (!fetchResult.success) {
        log(`无法获取远程分支: ${fetchResult.error}`);
        return false;
    }
    
    const aheadResult = executeCommand(`${GIT_PATH} rev-list --count origin/main..HEAD`);
    if (!aheadResult.success) {
        return false;
    }
    
    return parseInt(aheadResult.output) > 0;
}

function getBranchName() {
    const result = executeCommand(`${GIT_PATH} rev-parse --abbrev-ref HEAD`);
    return result.success ? result.output : 'main';
}

function autoPush() {
    log('=== 开始自动推送检查 ===');
    
    const branch = getBranchName();
    log(`当前分支: ${branch}`);
    
    if (hasChanges()) {
        log('检测到工作区有变更，开始提交...');
        
        const addResult = executeCommand(`${GIT_PATH} add .`);
        if (!addResult.success) {
            log(`git add 失败: ${addResult.error}`);
            return;
        }
        
        const dateStr = new Date().toLocaleString('zh-CN', { hour12: false });
        const commitMsg = `chore: 自动推送 - ${dateStr}`;
        const commitResult = executeCommand(`${GIT_PATH} commit -m "${commitMsg}"`);
        
        if (!commitResult.success) {
            log(`git commit 失败: ${commitResult.error}`);
            return;
        }
        
        log(`提交成功: ${commitMsg}`);
    } else {
        log('工作区没有变更');
    }
    
    if (hasUnpushedCommits()) {
        log('检测到有未推送的提交，开始推送...');
        
        const pushResult = executeCommand(`${GIT_PATH} push origin ${branch}`);
        if (pushResult.success) {
            log('推送成功！');
        } else {
            log(`推送失败: ${pushResult.error}`);
        }
    } else {
        log('没有未推送的提交');
    }
    
    log('=== 自动推送检查结束 ===\n');
}

function main() {
    log('启动自动推送服务...');
    log(`推送间隔: ${INTERVAL_MINUTES} 分钟`);
    log(`仓库路径: ${REPO_PATH}`);
    
    autoPush();
    
    setInterval(() => {
        autoPush();
    }, INTERVAL_MS);
    
    process.on('SIGINT', () => {
        log('收到停止信号，正在关闭...');
        process.exit(0);
    });
}

main();