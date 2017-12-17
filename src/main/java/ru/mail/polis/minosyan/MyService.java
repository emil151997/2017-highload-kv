package ru.mail.polis.minosyan;


import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

public class MyService implements KVService {

    private static final String PREFIX = "id=";

    @NotNull
    private final HttpServer server;

    @NotNull
    private final MyDAO dao;

    private static String extractId(@NotNull final String query) {

        if (!query.startsWith(PREFIX)) {
            throw new IllegalArgumentException("Illegal Sting");
        }
        return query.substring(PREFIX.length());
    }

    public MyService(int port, MyDAO dao) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.dao = dao;
        this.server.createContext("/v0/status",
                http -> {
                    String response = "ONLINE";
                    http.sendResponseHeaders(200, response.length());
                    http.getResponseBody().write(response.getBytes());
                    http.close();
                });
        this.server.createContext("/v0/entity",
                http -> {
                    final String id = extractId(http.getRequestURI().getQuery());
                    if ("".equals(id)) {
                        http.sendResponseHeaders(400, 0);
                        http.close();
                        return;
                    }
                    switch (http.getRequestMethod()) {
                        case "GET":
                            try {
                                final byte[] getValue = dao.get(id);
                                http.sendResponseHeaders(200, getValue.length);
                                http.getResponseBody().write(getValue);
                            } catch (NoSuchElementException | IOException e) {
                                http.sendResponseHeaders(404, 0);
                            }
                            break;

                        case "DELETE":
                            dao.delete(id);
                            http.sendResponseHeaders(202, 0);
                            break;

                        case "PUT":
                            ByteArrayOutputStream outStr = new ByteArrayOutputStream();
                            InputStream inpStr = http.getRequestBody();
                            byte[] buffer = new byte[4096];
                            int length;
                            while ((length = inpStr.read(buffer)) > 0) {
                                outStr.write(buffer, 0, length);
                            }
                            final byte[] putValue = outStr.toByteArray();
                            dao.upsert(id, putValue);
                            http.sendResponseHeaders(201, 0);
                            break;
                        default:
                            http.sendResponseHeaders(405, 0);
                    }

                    http.close();


                });
    }

    @Override
    public void start() {
        this.server.start();
    }

    @Override
    public void stop() {
        this.server.stop(0);
    }
}
