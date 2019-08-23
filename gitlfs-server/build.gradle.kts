description = "Java Git-LFS server implementation-free library"

dependencies {
    compile(project(":gitlfs-common"))
    compile("javax.servlet:javax.servlet-api:4.0.1")

    testCompile(project(":gitlfs-client"))
    testCompile("org.eclipse.jetty:jetty-servlet:9.4.18.v20190429")
    testRuntimeOnly("org.slf4j:slf4j-simple:1.7.28")
}
