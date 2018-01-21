-- example dynamic request script which demonstrates changing
-- the request path and a header for each request
-------------------------------------------------------------
-- NOTE: each wrk thread has an independent Lua scripting
-- context and thus there will be one counter per thread

wrk.method = "PUT"
cnt = 0

request = function()
   path = "/v0/entity?id=" .. cnt
   d = ""
   for i = 1, 4096 do
      d = d .. math.random(1,9)
   end
   wrk.body = d
   cnt = cnt + 1
   return wrk.format(nil, path)
end
