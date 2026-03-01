import requests

ipv4_address = requests.get("https://api64.ipify.org?format=json").text

print(ipv4_address)