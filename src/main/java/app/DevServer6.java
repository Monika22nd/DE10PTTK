package app;

import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.nio.file.Files;

/** Chạy thử bằng Tomcat 10.1 nhúng (gradlew runLocal). Có thể triển khai cùng file WAR lên Tomcat 10.1 độc lập. */
public final class DevServer6 {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new IllegalArgumentException("Cần đường dẫn file WAR");
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
        System.out.println("Supermarket 6: http://localhost:" + port + "/");
        tomcat.getServer().await();
    }
}
