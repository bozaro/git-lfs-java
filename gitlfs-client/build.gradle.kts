description = "Java Git-LFS client library"

dependencies {
    api(project(":gitlfs-common"))
    api("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.slf4j:slf4j-api:1.7.30")

    testImplementation("org.yaml:snakeyaml:1.27")
    testRuntimeOnly("org.slf4j:slf4j-simple:1.7.30")
}
