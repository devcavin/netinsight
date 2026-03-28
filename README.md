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
# when online
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

# when offline
{
    "localIp": {},
    "publicIp": {},
    "status": "ERROR",
    "message": "Failed to retrieve any IP information."
}
```

------------------------------------------------------------------------

## Project Structure
Dropping soon...

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
[MIT LICENSE](/LICENSE)