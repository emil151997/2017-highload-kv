# 3 этап - тестирование с Нагрузкой
Нагрузочное тестирование было проведено с помощью wrk в режимах 1/2/4 потока/соединения. Длительность 1 минута. Тестирование производилось следующим образом: Сначала запускаем тест "PUT" затем тест "GET без повторов"/"GET с повторами", затем перезапускаем сервер и делаем все тоже самое для другого кол-ва потоков/соединений. После этого делаем тест "PUT с перезаписью" презагружая каждый раз сервер, когда необходим изменить кол-во потоков/соединений"  
## До оптимизации

### PUT без перезаписи
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.08ms   13.81ms 288.30ms   98.65%
    Req/Sec   107.69     19.46   131.00     81.55%
  Latency Distribution
     50%    1.61ms
     75%    1.86ms
     90%    2.31ms
     99%   43.48ms
  6421 requests in 1.00m, 589.43KB read
Requests/sec:    107.00
Transfer/sec:      9.82KB
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.64ms    3.82ms  96.16ms   97.36%
    Req/Sec    67.32     17.66   111.00     68.26%
  Latency Distribution
     50%    2.00ms
     75%    2.52ms
     90%    3.54ms
     99%   13.34ms
  8039 requests in 1.00m, 737.96KB read
Requests/sec:    133.79
```
##### 4 потока, 4 соединения
```
 sudo wrk --latency -t4 -c4 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     6.06ms    5.46ms 131.15ms   90.69%
    Req/Sec    42.93      9.83    70.00     71.61%
  Latency Distribution
     50%    4.70ms
     75%    7.26ms
     90%   11.21ms
     99%   24.70ms
  10277 requests in 1.00m, 0.92MB read
Requests/sec:    170.98
Transfer/sec:     15.70KB
```
### GET без повторов
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     0.86ms  729.39us  20.38ms   90.55%
    Req/Sec     1.20k     0.91k    4.63k    86.98%
  Latency Distribution
     50%  791.00us
     75%    1.21ms
     90%    1.56ms
     99%    2.73ms
  71488 requests in 1.00m, 31.52MB read
  Non-2xx or 3xx responses: 65066
Requests/sec:   1191.42
Transfer/sec:    537.93KB
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     0.89ms  797.18us  24.62ms   87.73%
    Req/Sec     1.24k   728.56     3.71k    59.97%
  Latency Distribution
     50%  565.00us
     75%    1.20ms
     90%    1.80ms
     99%    3.83ms
  148043 requests in 1.00m, 44.86MB read
  Non-2xx or 3xx responses: 139994
Requests/sec:   2464.42
Transfer/sec:    764.64KB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.32ms    1.28ms  30.66ms   90.69%
    Req/Sec     0.86k   344.10     1.42k    52.55%
  Latency Distribution
     50%    0.93ms
     75%    1.54ms
     90%    2.52ms
     99%    6.25ms
  205317 requests in 1.00m, 58.87MB read
  Non-2xx or 3xx responses: 195014
Requests/sec:   3417.81
Transfer/sec:      0.98MB
```
### PUT c перезаписью
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s put_rewriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.23ms    2.02ms  61.83ms   93.85%
    Req/Sec    74.72     17.39   110.00     72.46%
  Latency Distribution
     50%    1.76ms
     75%    2.19ms
     90%    3.22ms
     99%    9.12ms
  4453 requests in 1.00m, 408.77KB read
Requests/sec:     74.19
Transfer/sec:      6.81KB
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s put_rewriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.48ms    2.10ms  64.29ms   95.91%
    Req/Sec    67.69      9.02    84.00     73.44%
  Latency Distribution
     50%    2.05ms
     75%    2.59ms
     90%    3.45ms
     99%    7.42ms
  8137 requests in 1.00m, 746.95KB read
Requests/sec:    135.41
Transfer/sec:     12.43KB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s put_rewriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     7.47ms    6.30ms  87.50ms   89.74%
    Req/Sec    37.85      9.18    60.00     67.06%
  Latency Distribution
     50%    5.70ms
     75%    9.10ms
     90%   13.90ms
     99%   33.52ms
  9077 requests in 1.00m, 833.24KB read
Requests/sec:    151.07
Transfer/sec:     13.87KB
```
### GET с повторами
##### 1 поток, 1 соединение
```
 sudo wrk --latency -t1 -c1 -d1m -s get_repeat.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   337.62us  597.33us  18.43ms   95.97%
    Req/Sec     3.51k   653.50     4.21k    90.50%
  Latency Distribution
     50%  206.00us
     75%  289.00us
     90%  515.00us
     99%    2.41ms
  209746 requests in 1.00m, 832.94MB read
  Non-2xx or 3xx responses: 510
Requests/sec:   3494.12
Transfer/sec:     13.88MB
```
##### 2 потока, 2 соединения
```
 sudo wrk --latency -t2 -c2 -d1m -s get_repeat.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   685.68us  820.70us  19.43ms   92.26%
    Req/Sec     1.73k   298.22     2.30k    72.79%
  Latency Distribution
     50%  427.00us
     75%  649.00us
     90%    1.26ms
     99%    4.25ms
  207084 requests in 1.00m, 824.33MB read
Requests/sec:   3451.06
Transfer/sec:     13.74MB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s get_repeat.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.10ms    0.92ms  16.92ms   89.50%
    Req/Sec     1.00k   122.45     1.24k    62.06%
  Latency Distribution
     50%  827.00us
     75%    1.21ms
     90%    2.08ms
     99%    4.95ms
  239215 requests in 1.00m, 0.93GB read
Requests/sec:   3984.29
Transfer/sec:     15.86MB
```
## После оптимизации

Оптимизация была выполнена путем добавления кэша в хранилище

### PUT без перезаписи
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s put.lua http://localhost:8080
[sudo] password for emil151997:
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.32ms    4.03ms  86.63ms   97.99%
    Req/Sec    99.37     13.29   128.00     71.28%
  Latency Distribution
     50%    1.76ms
     75%    1.95ms
     90%    2.34ms
     99%   20.62ms
  5956 requests in 1.00m, 546.74KB read
Requests/sec:     99.10
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.99ms    5.52ms 109.74ms   96.99%
    Req/Sec    73.05     13.16   101.00     81.20%
  Latency Distribution
     50%    2.00ms
     75%    2.40ms
     90%    3.25ms
     99%   29.79ms
  8759 requests in 1.00m, 804.05KB read
Requests/sec:    145.78
Transfer/sec:     13.38KB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     8.69ms   15.03ms 259.12ms   92.04%
    Req/Sec    36.08     22.83    86.00     46.86%
  Latency Distribution
     50%    4.08ms
     75%    7.65ms
     90%   19.39ms
     99%   74.29ms
  7673 requests in 1.00m, 704.36KB read
Requests/sec:    127.67
Transfer/sec:     11.72KB
```
### GET без повторов
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   627.52us  644.66us  22.20ms   89.95%
    Req/Sec     1.75k     1.26k    4.53k    81.17%
  Latency Distribution
     50%  433.00us
     75%    0.86ms
     90%    1.27ms
     99%    2.56ms
  104646 requests in 1.00m, 32.75MB read
  Non-2xx or 3xx responses: 98689
Requests/sec:   1742.03
Transfer/sec:    558.23KB
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.02ms    1.00ms  23.10ms   91.37%
    Req/Sec     1.10k   556.52     2.19k    61.67%
  Latency Distribution
     50%  701.00us
     75%    1.26ms
     90%    1.89ms
     99%    5.13ms
  131660 requests in 1.00m, 46.13MB read
  Non-2xx or 3xx responses: 122897
Requests/sec:   2190.87
Transfer/sec:    786.11KB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.23ms    1.27ms  26.82ms   92.83%
    Req/Sec     0.93k   254.02     1.84k    76.21%
  Latency Distribution
     50%    0.91ms
     75%    1.29ms
     90%    2.07ms
     99%    7.10ms
  222993 requests in 1.00m, 50.37MB read
  Non-2xx or 3xx responses: 215290
Requests/sec:   3711.02
```
### PUT c перезаписью
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s put_rewriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.79ms    1.85ms  59.78ms   99.05%
    Req/Sec   104.64     10.92   128.00     66.72%
  Latency Distribution
     50%    1.57ms
     75%    1.75ms
     90%    2.12ms
     99%    3.59ms
  6277 requests in 1.00m, 576.21KB read
Requests/sec:    104.43
Transfer/sec:      9.59KB
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s put_rewriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.33ms    3.22ms  67.70ms   98.30%
    Req/Sec    76.15     11.48   100.00     76.29%
  Latency Distribution
     50%    1.88ms
     75%    2.18ms
     90%    2.83ms
     99%    8.19ms
  9140 requests in 1.00m, 839.02KB read
Requests/sec:    152.09
Transfer/sec:     13.96KB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s put_rewriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     5.71ms    4.78ms  64.47ms   89.23%
    Req/Sec    44.59     10.80    70.00     68.37%
  Latency Distribution
     50%    4.11ms
     75%    6.82ms
     90%   10.82ms
     99%   25.18ms
  10676 requests in 1.00m, 0.96MB read
Requests/sec:    177.64
Transfer/sec:     16.31KB 
```
### GET с повторами
##### 1 поток, 1 соединение
```
 sudo wrk --latency -t1 -c1 -d1m -s get_repeat.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   287.44us    1.74ms  77.36ms   98.46%
    Req/Sec     6.29k     1.29k    7.51k    84.67%
  Latency Distribution
     50%  111.00us
     75%  138.00us
     90%  263.00us
     99%    3.31ms
  375368 requests in 1.00m, 1.46GB read
  Non-2xx or 3xx responses: 913
Requests/sec:   6253.20
Transfer/sec:     24.83MB
```
##### 2 потока, 2 соединения
```
 sudo wrk --latency -t2 -c2 -d1m -s get_repeat.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   596.60us    3.87ms 138.35ms   98.43%
    Req/Sec     3.58k     0.98k    4.89k    73.71%
  Latency Distribution
     50%  201.00us
     75%  274.00us
     90%  602.00us
     99%    6.45ms
  427541 requests in 1.00m, 1.66GB read
Requests/sec:   7115.32
Transfer/sec:     28.32MB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s get_repeat.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   585.09us  797.57us  21.84ms   93.60%
    Req/Sec     2.05k   489.99     2.81k    63.58%
  Latency Distribution
     50%  387.00us
     75%  548.00us
     90%    1.01ms
     99%    4.19ms
  489847 requests in 1.00m, 1.90GB read
Requests/sec:   8152.54
Transfer/sec:     32.45MB
```

Таким, образом оптимизация дала прирост производительности. Как видно из результатов тестирования, после оптимизации возросло кол-во запросов ( особенно заметно в тестировании Get с повторами) и уменьшилась задержка 
