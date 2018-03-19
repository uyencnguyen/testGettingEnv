package io.uyencnguyen.tmp.console;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Component
class MyConsole implements CommandLineRunner {
   public static void main(String ...args) throws IOException {
       BiConsumer<Object, Object> showPairAsString = (key, value) -> System.out.println(key + " -> " + value);
       System.getenv().forEach(showPairAsString);
       System.getProperties().forEach(showPairAsString);

       final String tmpLog = "tmp.log";
       Map<String, Object> systemInfo = getSystemEnv();
       Function<Map.Entry<String, Object>, String> mapFun =
               entry -> entry.getKey() + " -> " + String.valueOf(entry.getValue());
       writeFile(tmpLog, systemInfo, mapFun);
   }

   public static Map<String, Object> getSystemEnv() {
       Map<String, Object> systemInfo = new LinkedHashMap<>();
       systemInfo.put("# value from env","\n");
       System.getenv().forEach(systemInfo::put);
       systemInfo.put("\n# value from properties","\n");
       System.getProperties().forEach((key, value) -> systemInfo.put(String.valueOf(key), String.valueOf(value)));
       return systemInfo;
   }

    @Override
    public void run(String... args) throws Exception {
        main(args);
    }

    public static void writeFile(
            String path, Map<String, Object> data,
            Function<Map.Entry<String, Object>, String> mapFun) throws IOException {
        Files.write(Paths.get(path), () -> data.entrySet().stream().<CharSequence>map(mapFun).iterator());
    }
}