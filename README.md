# NetInsight

> Network monitoring and insights API built with Java Spring Boot

NetInsight provides real-time network visibility through a clean REST API.
Designed for network engineers who need programmatic access to network
statistics, bandwidth monitoring, and device discovery.

## Why NetInsight?

- **Network + Backend**: Built by someone who understands both fiber optics
  and backend architecture
- **Java-first**: Unlike Python-based tools, NetInsight runs on the JVM for
  enterprise reliability
- **API-first**: Integrate with any dashboard, monitoring system, or automation
  workflow
- **Docker-ready**: Deploy anywhere with zero dependencies

## Use Cases

- ISP network monitoring
- Home network visibility
- Server bandwidth tracking
- Automated network diagnostics
- Integration with existing monitoring stacks

## Quick Start
```bash
docker compose up
curl http://localhost:8080/api/v1/network/ip
```

## Roadmap

- [x] Network IP detection (v0.1)
- [ ] Interface statistics (v0.2) ← IN PROGRESS
- [ ] Bandwidth monitoring (v0.3)
- [ ] Device discovery (v0.4)
- [ ] Historical data (v0.5)
- [ ] WebSocket streaming (v0.6)

## Tech Stack

- Java 17
- Spring Boot 3.x
- Gradle
- Docker
- PostgreSQL (planned)

## Author

Built by [Cavin](https://github.com/devcavin) - Network Technician +
Backend Engineer passionate about infrastructure automation.

## License
[MIT LICENSE](LICENSE)