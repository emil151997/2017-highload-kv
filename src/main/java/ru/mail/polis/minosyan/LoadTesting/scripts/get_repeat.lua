
wrk.method = "GET"
cnt = 0

request = function()
   path = "/v0/entity?id=" .. cnt
   cnt = (cnt + 1) % 411
   return wrk.format(nil, path)
end
