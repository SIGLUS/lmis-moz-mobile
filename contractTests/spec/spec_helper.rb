require 'rubygems'
require 'rest-client'
require 'json'
require 'base64'

WEB_DEV_URI = "localhost:8081"

def http_basic_auth(username, password)
  auth = Base64.strict_encode64("#{username}:#{password}")
  return "Basic #{auth}"
end
