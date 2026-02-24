# Security Policy

## Supported Versions

Only the latest stable release receives security fixes.  
Older versions are not patched - upgrade to the latest release.

| Version | Supported |
|---------|-----------|
| 1.x.x (latest) | ✅ |
| < 1.0.0 | ❌ |

---

## Reporting a Vulnerability

**Do not open a public GitHub issue for security vulnerabilities.**

Report privately via one of these channels:

- **GitHub Private Advisory** - [Security Advisories](https://github.com/thenolle/pl3x-api/security/advisories/new) *(preferred)*
- **Email** - nolly@thenolle.com

### What to Include

A useful report has:

- A clear description of the vulnerability
- The affected version(s)
- Steps to reproduce - minimal code snippet if applicable
- The potential impact (data exposure, crash, bypass, etc.)
- A suggested fix if you have one

---

## Response Timeline

| Stage | Target |
|-------|--------|
| Acknowledgement | Within 48 hours |
| Initial assessment | Within 5 days |
| Fix released | Depends on severity (see below) |

### Severity Targets

| Severity | Fix Target |
|----------|------------|
| Critical | 3 days |
| High | 7 days |
| Medium | 14 days |
| Low | Next release cycle |

---

## Scope

This policy covers the `com.nolly:pl3x-api` library itself.

**In scope:**
- Logic bugs that could cause silent data corruption or incorrect state in the registry
- Unsafe reflection usage in `Pl3xBootstrap` or `Pl3xEventBridge` that could be exploited
- Dependency vulnerabilities in shaded artifacts

**Out of scope:**
- Vulnerabilities in Pl3xMap itself - report those to the [Pl3xMap project](https://github.com/granny/Pl3xMap)
- Vulnerabilities in Spigot/Paper - report those to their respective projects
- Issues in your own plugin code that uses this library
- Minecraft server exploits unrelated to this library

---

## Disclosure Policy

This project follows **coordinated disclosure**:

1. You report privately
2. We confirm and assess
3. We develop and test a fix
4. We release the fix and publish a [GitHub Security Advisory](https://github.com/thenolle/pl3x-api/security/advisories)
5. You are credited in the advisory unless you request otherwise

Public disclosure before a fix is available will be treated as a breach of this policy.

---

## Credits

Responsibly disclosed vulnerabilities will be credited in the release notes and security advisory by default.  
If you prefer to remain anonymous, state that in your report.
