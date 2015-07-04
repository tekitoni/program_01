<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.github</groupId>
  <artifactId>finder</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>File Finder</name>
  <url>http://github.com/ksuse/FileFinder/</url>
  <inceptionYear>2015</inceptionYear>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>args4j</groupId>
      <artifactId>args4j</artifactId>
      <version>2.32</version>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.0</version>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
          <encoding>utf-8</encoding>
        </configuration>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <executions>
          <execution>
            <id>copy-dependencies</id>
            <phase>compile</phase>
            <goals>
              <goal>copy-dependencies</goal>
            </goals>
            <configuration>
              <outputDirectory>${project.build.directory}</outputDirectory>
              <includeScope>runtime</includeScope>
            </configuration>
          </execution>
        </executions>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-resources-plugin</artifactId>
        <version>2.4.3</version>
        <configuration>
          <encoding>utf-8</encoding>
        </configuration>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>2.3.1</version>
        <configuration>
          <archive>
            <manifest>
              <mainClass>com.github.finder.Main</mainClass>
              <addClasspath>true</addClasspath>
              <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
            </manifest>
          </archive>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>


package com.github.finder;

import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.util.ArrayList;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class Args implements Iterable<String>{
    @Argument(metaVar="DIRS")
    private List<String> targets;

    @Option(name="-name", metaVar="<NAME>")
    private String name;

    @Option(name="-type", metaVar="<TYPE>")
    private String type;

    @Option(name="-size", metaVar="<SIZE>")
    private String size;

    @Option(name="-grep", metaVar="<PATTERN>")
    private String grep;

    @Override
    public Iterator<String> iterator(){
        return targets.iterator();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public String getGrep() {
        return grep;
    }
}

  private Args parseArguments(String[] arguments){
        Args args = new Args();
        try {
            CmdLineParser parser = new CmdLineParser(args);
            parser.parseArgument(arguments);
        } catch (CmdLineException e) {
        }
        return args;
    }
    

public class Main{
    public Main(String[] arguments){
    }

    public static void main(String[] args){
        new Main(args);
    }
}

public class Finder {
    private Args args;

    public Finder(Args args){
        this.args = args;
    }

    public String[] find(String target){
        List<String> list = new ArrayList<>();

        traverse(list, new File(target));

        return list.toArray(new String[list.size()]);
    }

    private boolean isTarget(File file){
    	boolean flag = true;
        	if(args.getName() != null){
            	flag &= checkTargetName(file, args.getName());
        	}
        return flag; 
   }

    private boolean checkTargetName(File file, String pattern){
        String name = file.getName();
        return name.indexOf(pattern) >= 0;
    }
    
    
    private void traverse(List<String> list, File dir){
        if(isTarget(dir)){
            list.add(dir.getPath());
        }
        if(dir.isDirectory()){
            for(File file: dir.listFiles()){
                traverse(list, file);
            }
        }
    }
}
