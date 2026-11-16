package com.example.supermarket;

import org.apache.catalina.startup.Tomcat;
import java.io.File;
import java.nio.file.Files;

/** Embedded Tomcat for local demo. The same WAR may be deployed to Tomcat 10.1. */
public final class DevServer {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new IllegalArgumentException("WAR path required");
        // Local-only sample credentials/data; deploying the WAR to Tomcat does not enable this.
        System.setProperty("supermarket.demo", "true");
        int port = Integer.parseInt(System.getenv().getOrDefault("SUPERMARKET_PORT", "8090"));
        Tomcat tomcat = new Tomcat();
        File base = new File("build/tomcat").getAbsoluteFile();
        Files.createDirectories(base.toPath().resolve("webapps"));
        tomcat.setBaseDir(base.getAbsolutePath());
        tomcat.setPort(port);
        tomcat.getConnector().setProperty("address", "127.0.0.1");
        tomcat.addWebapp("", new File(args[0]).getAbsolutePath());
        tomcat.start();
        System.out.println("Supermarket JSP: http://localhost:" + port + "/");
        tomcat.getServer().await();
    }
}
