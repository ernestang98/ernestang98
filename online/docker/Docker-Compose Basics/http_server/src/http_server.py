#!/usr/bin/python3.8
import os
import http.server
import socketserver
import json

from urllib.request import Request, urlopen

HTTP_PORT = int(os.environ.get("PORT"))
API_SERVER_URL = os.environ.get("API_SERVER_URL")

class RequestHandler(http.server.SimpleHTTPRequestHandler):
  def do_GET(self):
    self.send_response(200)
    self.send_header("Content-type", "text/html")
    self.end_headers()

    html = """
      <html>
        <body>
          <h1>{message}</h1>
        </body>
      </html>
    """

    if API_SERVER_URL is not None:
      url = f"{API_SERVER_URL}/person"
      response = urlopen(Request(url))
      person = json.loads(response.read().decode())
      message = f"Hello {person['name']} {person['surname']}!"
    else:
      message = "No connection to API server. API_SERVER_URL not defined!"
      
    self.wfile.write(bytes(html.format(message=message), "utf8"))

    return

socketserver.TCPServer(("", HTTP_PORT), RequestHandler).serve_forever()
