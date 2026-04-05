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

**Endpoint**

```http request
GET /api/v1/network/ip
```

**Response**

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
        "ipv4": "41.139.167.17",
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
        "ipv4": "41.139.167.17",
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

**Endpoint**

```http request
GET /api/v1/network/bandwidth
```

**Response**

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
* [ ] Interface statistics
* [ ] Device discovery
* [ ] Continuous monitoring
* [ ] Streaming metrics
* [ ] Integrations

---

## Contributing

Contributions are welcome! You can:

1. Fork the repo
2. Create a branch for your feature or adjustments or fix (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -m "Add new feature"`)
4. Push branch (`git push origin feature/new-feature`)
5. Open a Pull Request

---

## License

[MIT LICENSE](/LICENSE)
