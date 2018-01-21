
wrk.method = "PUT"
cnt = 0

request = function()
   path = "/v0/entity?id=" .. cnt
   d = ""
   for i = 1, 4096 do
       d = d .. math.random(1,9)
   end
   wrk.body = d
   cnt = (cnt + 1) % 411
   return wrk.format(nil, path)
end
