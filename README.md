# NetInsight

NetInsight provides simple network insights through a REST API. It
exposes useful system and network information in a structured and
extensible format.

## Features

-   Retrieve local network interface IPs
-   Retrieve public IP address
-   Structured JSON responses
-   Container-ready setup
-   Designed for incremental feature additions

------------------------------------------------------------------------

## API

### Get Network IP Information

Returns local interface IPs that are up and public IP.

**Endpoint**

```sh
GET api/v1/network/ip
```

**Response**

``` json
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
    "ipv4": "41.x.x.x"
  },
  "status": "SUCCESS"
}
```

------------------------------------------------------------------------

## Project Structure

    src/main/java/.../controller   # REST endpoints
    src/main/java/.../service      # Core logic
    src/main/java/.../dto          # Response objects
    scripts/                       # automation scripts (future)

------------------------------------------------------------------------

## Running

### Using Gradle

``` bash
./gradlew bootRun
```

### Using Docker

``` bash
docker compose up --build
```

------------------------------------------------------------------------

## Roadmap

-   [x] network IP endpoint
-   [ ] bandwidth monitoring
-   [ ] interface statistics
-   [ ] device discovery
-   [ ] continuous monitoring
-   [ ] streaming metrics
-   [ ] integrations

------------------------------------------------------------------------

## License

MIT
