plugins {
    id("project-conventions")
    id("java-conventions")
    id("spring-conventions")
}

dependencies {
    // spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}