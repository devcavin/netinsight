# NetInsight

**NetInsight** provides simple and structured network insights via a REST API. It exposes useful system and network information in a JSON format, making it **extensible, container-ready, and easy to integrate**.

---

## Features

* Retrieve local network interface IPs
* Retrieve public IP address
* Structured JSON responses
* Container-ready setup
* Designed for incremental feature additions

---

## API

### Get Network IP Information

Returns all local interface IPs that are up and the public IP address.

*Endpoint*

```http request
GET /api/v1/network/ip
```

*Response*

**When network interfaces are up**

```json
{
  "localIps": {
    "wlan0": {
      "ipv4": "10.10.43.221",
      "ipv6": "fe80:0:0:0:2b7a:ad03:a5c9:2cfc"
    },
    "eth0": {
      "ipv4": "10.10.100.61",
      "ipv6": "fe80:0:0:0:f639:9ff:fee2:7339"
    }
  },
  "publicIp": {
    "ipv4": "41.x.x.x",
    "ipv6": "not configured by ISP"
  },
  "status": "SUCCESS",
  "message": "IP information retrieved successfully."
}
```

**When at least a single network interfaces is up**
```json
{
    "localIp": {
        "eth0": {
            "ipv4": "10.10.100.61",
            "ipv6": "fe80:0:0:0:f639:9ff:fee2:7339"
        }
    },
    "publicIp": {
        "ipv4": "41.x.x.x",
        "ipv6": "not configured by ISP"
    },
    "status": "SUCCESS",
    "message": "IP information retrieved successfully."
}
```

```json
{
    "localIp": {
        "wlan0": {
            "ipv4": "10.10.43.221",
            "ipv6": "fe80:0:0:0:2b7a:ad03:a5c9:2cfc"
        }
    },
    "publicIp": {
        "ipv4": "41.x.x.x",
        "ipv6": "not configured by ISP"
    },
    "status": "SUCCESS",
    "message": "IP information retrieved successfully."
}
```

**When network interfaces are down**

```json
{
  "localIp": {},
  "publicIp": {},
  "status": "ERROR",
  "message": "Failed to retrieve any IP information."
}
```

---

### Get bandwidth information
Returns the bandwidth information for the active network interface

*Endpoint*

```http request
GET /api/v1/network/bandwidth
```

*Response*

**When network interfaces are up**
```json
{
    "interfaceName": "eth0",
    "uploadSpeedMbps": 0.1310267430754537,
    "downloadRateMbps": 2.9950300859598857,
    "totalSentMB": 229.38504123687744,
    "totalReceivedMB": 1729.1104316711426,
    "status": "SUCCESS",
    "message": "Active network interface detected: eth0"
}
```

**When network interfaces are down**
```json
{
    "uploadSpeedMbps": 0.0,
    "downloadRateMbps": 0.0,
    "totalSentMB": 0.0,
    "totalReceivedMB": 0.0,
    "status": "ERROR",
    "message": "No active network interface detected"
}
```
### Get interface statistics
Returns full snapshot of network interfaces

Later this endpoint will open doors for computing derived metrics like:
  - packet loss %
  - error rate
  - drop rate
  - utilization %
  - uptime

*Endpoint*

```http request
GET /api/v1/network/interfaces
```
*Response*

**When at least a single interface is up**

```json
{
    "interfaces": {
        "wlan0": {
            "name": "wlan0",
            "status": "UP",
            "macAddress": "38:BA:F8:70:73:8A",
            "mtu": 1500,
            "rxBytes": 206661403,
            "txBytes": 22447925,
            "rxPackets": 192381,
            "txPackets": 61474,
            "rxErrors": 0,
            "txErrors": 0,
            "rxDropped": 567,
            "txDropped": 12
        },
        "eth0": {
            "name": "eth0",
            "status": "UP",
            "macAddress": "F4:39:09:E2:73:39",
            "mtu": 1500,
            "rxBytes": 1028221913,
            "txBytes": 63772580,
            "rxPackets": 905546,
            "txPackets": 396908,
            "rxErrors": 0,
            "txErrors": 0,
            "rxDropped": 8137,
            "txDropped": 23
        }
    },
    "status": "SUCCESS",
    "message": "Interface Statistics received successfully."
}
```

**When interfaces are down**

```json
{
    "interfaces": {},
    "status": "ERROR",
    "message": "No network interfaces detected."
}
```

### Stream live bandwidth metrics
Streams live bandwidth measurements using Server-Sent Events (SSE)

*Endpoint*
```http request
GET /api/v1/network/bandwidth/stream?duration=10&interval=1000
```

**Query parameters**

| Parameter | Type | Default | Description                       |
|-----------|------|---------|-----------------------------------|
| duration  | Long | 5       | Stream duration in seconds        |
| interval  | Long | 1000    | Emission interval in milliseconds |

*Response*

**When at least an interface is up**

```json
data:
  {
    "timestamp":1778361504668,
    "interfaceName":"eth0",
    "uploadMbps":6.712708812939024E-4,
    "downloadMbps":0.0052851661649253295,
    "status":"SUCCESS"
  }

data:
  {
    "timestamp":1778361505675,
    "interfaceName":"eth0",
    "uploadMbps":0.002687872763419483,
    "downloadMbps":0.011180914512922465,
    "status":"SUCCESS"
  }

data:
  {
    "timestamp":1778361506679,
    "interfaceName":"eth0",
    "uploadMbps":0.0016398009950248757,
    "downloadMbps":0.005365174129353234,
    "status":"SUCCESS"
  }

data:
  {
    "timestamp":1778361507684,
    "interfaceName":"eth0",
    "uploadMbps":0.002693227091633466,
    "downloadMbps":0.005466135458167331,
    "status":"SUCCESS"
  }

data:
  {
    "timestamp":1778361508697,
    "interfaceName":"eth0",
    "uploadMbps":0.0021523244312561824,
    "downloadMbps":0.007485657764589516,
    "status":"SUCCESS"
  }
```
**When interface is down**

```json
data:
  {
    "timestamp":1778361921956,
    "uploadMbps":0.0,
    "downloadMbps":0.0,
    "status":"ERROR"
  }
```

---

## Project Structure

*Coming soon…*
(Currently, the project is structured to support Gradle or Docker workflows.)

---

## Running NetInsight

### Using Gradle

```bash
./gradlew bootRun
```

### Using Docker

```bash
docker compose up --build
```

---

## Roadmap

* [x] Network IP endpoint
* [x] Bandwidth monitoring
* [x] Interface statistics
* [x] Streaming metrics
* [ ] Latency diagnostics
* [ ] Continuous monitoring
* [ ] Device discovery
* [ ] Integrations

---

## Contributing

Contributions are welcome! You can feel free to check out [CONTRIBUTION](/CONTRIBUTING.md)

---

## License

[MIT LICENSE](/LICENSE)
